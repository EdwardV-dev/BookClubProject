import { useContext, useEffect, useState } from "react";
import AuthContext from "../context/AuthContext";
import { Link, useHistory, useParams } from "react-router-dom";

function EditAdminBooks() {

    const currentAuthor = {
        authorFirstName: "",
        authorLastName: ""
    }
    const currentBook = {
        approvalStatus: "",
        bookTitle: "",
        genre: "",
        author: currentAuthor,
        yearPublished: ""
    }
    const [book, setBook] = useState(currentBook);
    const [author, setAuthor] = useState(currentAuthor);
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
    const handleAuthorChange = (event) => {
        const { name, value } = event.target;
        // setBook({...book,
        //     author: setAuthor({...author,
        //         [name]: value,
        //     })
        // });
        setAuthor({...author, [name]: value});
    }

    const handleSubmit = async (event) => {
        event.preventDefault();

        const updatedBook = {
            ...book
        };

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
                    history.push("/admin");
                } else if (response.status == 400) {
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

    return (
        book&& (
        <form onSubmit={handleSubmit} className="form-inline mx-2 my-4">
             <div>
                <label htmlFor="authorFirstName">Author First Name:</label>
                <input 
                type="text" 
                id="authorFirstName" 
                name="authorFirstName" 
                value={author.authorFirstName}
                required
                onChange={handleAuthorChange}
                />
            </div>
            <div>
                <label htmlFor="authorLastName">Author Last Name:</label>
                <input 
                type="text" 
                id="authorLastName" 
                name="authorLastName" 
                value={author.authorLastName} 
                required
                onChange={handleAuthorChange}
                />
            </div>
            <div>
                <label htmlFor="bookTitle">Book Title:</label>
                <input 
                type="text" 
                id="bookTitle" 
                name="bookTitle" 
                value={book.bookTitle}
                required
                onChange={handleChange} 
                />
            </div>
            <div>
                <label htmlFor="genre">Genre:</label>
                <input 
                type="text" 
                id="genre" 
                name="genre" 
                value={book.genre}
                required
                onChange={handleChange} 
                />
            </div>
            <div>
                <label htmlFor="yearPublished">Year Published:</label>
                <input 
                type="number" 
                id="yearPublished" 
                name="yearPublished" 
                max="2022"
                value={book.yearPublished}
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
        </form>
        )
    );
}

export default EditAdminBooks;