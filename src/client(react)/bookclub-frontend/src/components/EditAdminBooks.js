import { useContext, useEffect, useState, React } from "react";
import AuthContext from "../context/AuthContext";
import { Link, useHistory, useParams } from "react-router-dom";
import ErrorArray  from "./ErrorArray";
import 'bootstrap/dist/css/bootstrap.min.css'
import { Form, Button } from 'react-bootstrap/Form';
import FormCheck from 'react-bootstrap/FormCheck';
import FormControl from 'react-bootstrap/FormControl';
import FormFloating from 'react-bootstrap/FormFloating';
import FormGroup from 'react-bootstrap/FormGroup';
import FormLabel from 'react-bootstrap/FormLabel';
import FormRange from 'react-bootstrap/FormRange';
import FormSelect from 'react-bootstrap/FormSelect';
import FormText from 'react-bootstrap/FormText';
import Container from 'react-bootstrap/Container';
import Row from 'react-bootstrap/Row';
import Col from 'react-bootstrap/Col'

function EditAdminBooks() {

    const currentBook = {
        approvalStatus: "",
        bookTitle: "",
        genre: "",
        author: {
            authorFirstName: "",
            authorLastName: "",
        },
        yearPublished: ""
    }

    const [book, setBook] = useState(currentBook);
    const history = useHistory();
    const { id } = useParams();
    const [errors, setErrors] = useState([]);
     
    useEffect(
        () => {
            // Only do this if there is an `id`
            if (id) {
                fetch(`http://localhost:8080/books/${id}`, {
                  method: "GET",
                  headers: {
                      "Content-Type": "application/json",
                      Authorization: `Bearer ${localStorage.getItem("token")}`
                  }
                })
                .then(response => {
                  if (response.status !== 200) {
                    return Promise.reject("book fetch failed")
                  }
                  return response.json();
                })
                .then(data => setBook(data))
                .catch(console.log);
            }
        }, 
        [id]
  );

    console.log(book);


    const handleChange = (event) => {
        const { name, value } = event.target;
        setBook({...book,
            [name]: value,
        });
    }

    const handleFirstNameChange = (event) => {
        setBook({...book,
            author: {...book.author,
                authorFirstName: event.target.value
            }
        })
    }

    const handleLastNameChange = (event) => {
        setBook({...book,
            author: {...book.author,
                authorLastName: event.target.value
            }
        })
    }

    const handleSubmit = async (event) => {
        event.preventDefault();

        if(book.yearPublished === null || book.yearPublished.toString().length === 0){
            book.yearPublished = 6000;
        }

        const getCorrections = () => {for(const property in book) {
            if (typeof book[property] === "string"){
                book[property].trimStart();
                book[property].trimEnd();
                book[property].toLowerCase();
                book[property].replace(/\s{2,}/g, " " ) //replace 2 or more spaces with one space
                
            }
         }
        }
 
        let correctTitle = book.bookTitle.charAt(0).toUpperCase() + book.bookTitle.substring(1);
        let correctAuthorFirstName = book.author.authorFirstName.charAt(0).toUpperCase() + book.author.authorFirstName.substring(1);
        let correctAuthorLastName = book.author.authorLastName.charAt(0).toUpperCase() + book.author.authorLastName.substring(1);
        let correctCaseGenre = book.genre.replace((/[^a-z0-9]/gmi, ""))
         correctCaseGenre = book.genre.charAt(0).toUpperCase() + book.genre.substring(1);
        
  getCorrections();

        const updatedBook = {
            ...book,
            
        };

        console.log(updatedBook);

        try {
            const init = {
                method: "PUT",
                headers: {
                    "Content-Type": "application/json",
                    Authorization: `Bearer ${localStorage.getItem("token")}`
                },
                body: JSON.stringify(updatedBook)
            };

            const response = await fetch(
                `http://localhost:8080/booksAdmin/${id}`, 
                init);
                if (response.status === 204) {
                    setErrors([]);
                    history.push("/admin");
                } else if (response.status === 400) {
                    const errors = await response.json();
                    setErrors(errors);
                } else if (response.status === 403) {
                    setErrors(["Not logged in."]);
                } else {
                    setErrors(["Unknown error."]);
                }
        } catch (error) {
            console.log(error);
        }    
    }

    const handleCancel = async (event) => {
        history.push("/admin");
    }

    const determineIfYearNull = (yearPublished) => {
        if (yearPublished === 6000){
            return null;
        } else {
            return yearPublished;
        }
    }

    return (
        book && (
        <>

        <ErrorArray errors={errors} />

        <form onSubmit={handleSubmit} className="form-inline mx-2 my-4 text-light" >
             <div class="container">
                 <div class="row justify-content-md-center">

                     <div class="col col-lg-2"></div>

                     <div class="col-md-auto">
                     <div className="form-group mx-4 my-4" sm={10}>
                <label htmlFor="authorFirstName">Author First Name:</label>
                <input 
                    type="text" 
                    id="authorFirstName" 
                    name="authorFirstName" 
                    defaultValue={book.author.authorFirstName}
                    required
                    className="form-control"
                    onChange={handleFirstNameChange}
                />
            </div>
            <div className="form-inline mx-4 my-4">
                <label htmlFor="authorLastName">Author Last Name:</label>
                <input
                    type="text" 
                    id="authorLastName" 
                    name="authorLastName" 
                    defaultValue={book.author.authorLastName} 
                    required
                    className="form-control"
                    onChange={handleLastNameChange}
                />
            </div>
            <div className="form-inline mx-4 my-4">
                <label htmlFor="bookTitle">Book Title:</label>
                <input 
                    className="form-inline mx-"
                    type="text" 
                    id="bookTitle" 
                    name="bookTitle" 
                    value={book.bookTitle}
                    required
                    className="form-control"
                    onChange={handleChange} 
                />
            </div>
            <div className="form-inline mx-4 my-4">
                <label htmlFor="genre">Genre:</label>
                <input 
                    type="text" 
                    id="genre" 
                    name="genre" 
                    value={book.genre}
                    required
                    className="form-control"
                    onChange={handleChange} 
                />
            </div>
            <div className="form-inline mx-4 my-4">
                <label htmlFor="yearPublished">Year Published:</label>
                <input 
                    type="number" 
                    id="yearPublished" 
                    name="yearPublished" 
                    max= "2022"
                    className="form-control"
                    value= {determineIfYearNull(book.yearPublished)}
                    onChange={handleChange} 
                />
            </div>

            <div>
                <button type="submit" className="btn btn-success ml-2">
                    Update
                </button>  &nbsp;
                <button type="button" className="btn btn-warning ml-2" onClick={handleCancel}>
                    Cancel
                </button>        
            </div>
                     </div>

                     <div class="col col-lg-2"></div>

                 </div>
             </div>
             
             
        </form>
        </>
        )
    );
}

export default EditAdminBooks;