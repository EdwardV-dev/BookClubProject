
import { Link, useHistory} from "react-router-dom";
import { useContext, useEffect, useState } from "react";
import ErrorArray from "./ErrorArray"
function AddBook() {

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

    const [errors, setErrors] = useState([]);
    const [completionStatus, setCompletionStatus] = useState("");
     


    const handleChange = (event) => {
        const { name, value } = event.target;
        setBook({...book,
            [name]: value,
        });
    }

    
   const handleButtonChange = (event) => {
    setCompletionStatus(event.target.value);
    console.log("hey there " + completionStatus);
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
    //    let genrePunctuationRemoved = 
       let correctCaseGenre = book.genre.charAt(0).toUpperCase() + book.genre.substring(1);


    //    book[property].charAt(0).toUpperCase + book[property].substring(1);

 getCorrections();
       
       

        const updatedBook = {

            ...book, bookTitle: correctTitle, genre: correctCaseGenre,
            author: {...book.author,
                authorFirstName: correctAuthorFirstName,
                authorLastName: correctAuthorLastName
            }
            
        };

        const body1 = JSON.stringify(updatedBook);
  
      const init1 = {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${localStorage.getItem("token")}`,
        },
       
        body: body1,
      };

      console.log(init1);

   try{
   
    const response = await fetch("http://localhost:8080/booksAdmin", init1);

 //   Only 2 outcomes: 201 (non-duplicate) or 400 (duplicate). Either way, associate the book with the user account
      if (response.status === 201 || response.status === 400) {
        const data1 = await response.json();


        activateSecondFetch(data1);
    
    } else {
        throw new Error("Something went wrong on our end") 
           
       }
   } catch (error){
       console.log(error);
   }


async function activateSecondFetch(data1){

    const bookAssociationSend = {
     appUserId: localStorage.getItem("userId"),
     completionStatus: completionStatus, //Set this to default for request to go through 
       book : {
            idBooks: book.idBooks,
            bookTitle: book.bookTitle, 
            author : {
                 authorFirstName: book.author.authorFirstName,
                 authorLastName: book.author.authorLastName
            }
        }
      };

      const body2 = JSON.stringify(bookAssociationSend);

      const init2 = {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${localStorage.getItem("token")}`,
        },
        body: body2,
      };

    try{
        const response = await fetch("http://localhost:8080/books", init2);
    
        //The book is either already associated with the user (201) or the user + book combinantion already exists
          if (response.status === 201 || response.status === 500) {
                
            // determine if I have a book association or errors...
            if (response.status === 201) {
              // if I have a book, then redirect user to their book list. Clear any errors first.
              setErrors([]);
             
              history.push("/books");
              
            } else {
              // otherwise display the errors from the second fetch
             
              setErrors(["Unable to add book. The entered book is most likely already associated with your account"]);
            //   history.push("/books");
            }
        
        } else {
            throw new Error("Something went wrong on our end") 
               
           }
       } catch (error){
           console.log(error);
       }

}
        console.log(updatedBook);
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
        
<form onSubmit={handleSubmit} className="form-inline mx-2 my-4">
     <ErrorArray errors={errors} />
             <div>
                <label htmlFor="authorFirstName">Author First Name:</label>
                <input 
                    type="text" 
                    id="authorFirstName" 
                    name="authorFirstName"            
                    required
                    onChange={handleFirstNameChange}
                />
            </div>
            <div>
                <label htmlFor="authorLastName">Author Last Name:</label>
                <input
                    type="text" 
                    id="authorLastName" 
                    name="authorLastName"        
                    required
                    onChange={handleLastNameChange}
                />
            </div>
            <div>
                <label htmlFor="bookTitle">Book Title:</label>
                <input 
                    type="text" 
                    id="bookTitle" 
                    name="bookTitle"            
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
                    max= "2022"
                    onChange={handleChange} 
                />
            </div>

            <div>
                    <input 
                        type="radio" 
                        value="WantToRead" 
                        name="completionStatus"
                        required
                        onClick={handleButtonChange}
                    />
                    Want to Read
                </div>
                <div>
                    <input 
                        type="radio" 
                        value="Reading"
                        name="completionStatus"
                        onClick={handleButtonChange} 
                    /> 
                    Reading
                </div>
                <div>
                    <input 
                        type="radio" 
                        value="DoneReading"
                        name="completionStatus"
                        onClick={handleButtonChange} 
                    /> 
                    Done Reading
                </div>

            <div>
                <button type="submit" className="btn btn-success ml-2">
                    Add
                </button>  &nbsp;
                <button type="button" className="btn btn-warning ml-2" onClick={handleCancel}>
                    Cancel
                </button>        
            </div>
        </form>
        );
  
  
}
  
export default AddBook;
