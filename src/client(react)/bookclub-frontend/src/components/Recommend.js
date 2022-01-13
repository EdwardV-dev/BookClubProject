import { Link, useHistory } from "react-router-dom";
import React, { useState, useEffect, useContext } from "react";
import AuthContext from "../context/AuthContext";
import CompletionStatus from "./CompletionStatus";
import YearPublished from "./YearPublished";
import ErrorArray  from "./ErrorArray";
import Error from "./Error";

function Recommend() {
  const [book, setBook] = useState(null);
  const [errors, setErrors] = useState([]);//Each error is received as an array
  const [initialFetchError, setInitialFetchError] = useState("");
  const [flag, setFlag] = useState(false);
 const history = useHistory();

  useEffect(() => {
  
    const init = {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${localStorage.getItem("token")}`,
      },
    };

    // 2. fetch initial books for logged-in user
    fetch(
      `http://localhost:8080/books/userIdRecommended?userIdRecommended=${localStorage.getItem(
        "userId"
      )}`,
      init
    )
      .then((response) => {
        if (response.status !== 200 && response.status !== 500) {
          return Promise.reject("books fetch failed");
        }

        if (response.status === 500) {
          return Promise.reject(": recommended books fetch failed because you do not have any books in your list yet. Please add a book to my books first");
          
        }

        setInitialFetchError(""); //Error message will no longer be displayed
        return response.json();
      })
      .then((json) => setBook(json))
      .catch((result) => setInitialFetchError(result)) //catches "books fetch failed"
      .then(() => setFlag(true));
  }, [errors]);


  //add to books table in sql and then association. TRIGGERED BY ONCLICK
  async function addBookToMyList() {
    const bookSend = {
        approvalStatus: book.approvalStatus,
        bookTitle: book.bookTitle,
        genre: book.genre,
        author: {
          authorFirstName: book.author.authorFirstName,
          authorLastName: book.author.authorLastName,
        },
        yearPublished: book.yearPublished,
      };

      const body1 = JSON.stringify(bookSend);
  
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
     completionStatus: "WantToRead", //Set this to default for request to go through 
       book : {
            idBooks: book.idBooks,
            bookTitle: book.bookTitle, //can't rely on first add fetch for the book title (could return duplicate error). Use info from recommendation fetch instead
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
              history.push("/recommend");
            }
        
        } else {
            throw new Error("Something went wrong on our end") 
               
           }
       } catch (error){
           console.log(error);
       }

}
   }
   
  
   if(flag){
    setFlag(false);
     return (
      
<Error msg={initialFetchError} />

     )
   }

  //recommended book must be truthy for rendering to occur
  return (
    (book) && (
        
      <>
      
      <ErrorArray errors={errors} />
        <table className="table table-striped table-dark table-hover">
          <thead>
            <tr>
              <th scope="col">Book Title</th>
              <th scope="col">Genre</th>
              <th scope="col">Year Published</th>
              <th scope="col">Author First Name</th>
              <th scope="col">Author Last Name</th>
              <th scope="col">Actions</th>
            </tr>
          </thead>
          <tbody>
            <tr key={book.idBooks}>
              <td>{book.bookTitle}</td>
              <td>{book.genre}</td>
              <td>
                <YearPublished year={book.yearPublished} />
              </td>
              <td>{book.author.authorFirstName}</td>
              <td>{book.author.authorLastName}</td>

              <td>
                <div className="float-right">
                  <button onClick={addBookToMyList}>Add Book</button> &nbsp;
                </div>
              </td>
            </tr>
          </tbody>
        </table>
      </>
    )
  );
}

export default Recommend;
