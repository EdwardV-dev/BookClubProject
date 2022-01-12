import { Link, useHistory } from "react-router-dom";
import React, { useState, useEffect, useContext } from "react";
import AuthContext from "../context/AuthContext";
import CompletionStatus from "./CompletionStatus";
import YearPublished from "./YearPublished";

function Recommend() {
  const [book, setBook] = useState(null);
  const [error, setErrors] = useState([]);

  useEffect(() => {
    console.log("starting useeffect in recommended");
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
        if (response.status !== 200) {
          return Promise.reject("books fetch failed");
        }
        return response.json();
      })
      .then((json) => setBook(json))
      .catch(console.log);
  }, []);

  //add to books table in sql and then association
  async function addBookToMyList() {
    const bookSend = {
        approvalStatus: book.approvalStatus,
        bookTitle: book.bookTitle,
        genre: book.genre,
        author: {
          authorFirstName: book.author.authorFirstName,
          authorLastName: book.author.authorLastName,
        },
        yearPublished: 2005,
      };
  
      const init1 = {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${localStorage.getItem("token")}`,
        },
        bookSend,
      };

   try{
   
    const response = await fetch("http://localhost:8080/api/todos", init1);

      if (response.status === 201 || response.status === 400) {
        const data = await response.json();

        // determine if I have a book or errors...
        if (data.idBooks) {
          // if I have a book, then do a POST request to the bridge table make an association. Also, clear any pre-existing error
          
          activateSecondFetch();
          
      
          
        } else {
          // otherwise display the errors from the first fetch
          setErrors(data);
        }
    
    } else {
        throw new Error("Something went wrong on our end") 
           
       }
   } catch (error){
       console.log(error);
   }
   }
   
  

  //book must be truthy for rendering to occur
  return (
    book && (
      <>
        <table className="table table-striped table-dark table-hover">
          <thead>
            <tr>
              <th scope="col">Book Title</th>
              <th scope="col">Genre</th>
              <th scope="col">Year Published</th>
              <th scope="col">Author First Name</th>
              <th scope="col">Author Last Name</th>
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
