import { Link, useHistory } from "react-router-dom";
import React, { useState, useEffect, useContext } from "react";
import AuthContext from "../context/AuthContext";
import CompletionStatus from "./CompletionStatus";
import YearPublished from "./YearPublished";

function Recommend() {
  const [book, setBook] = useState(null);

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
  function addBookToMyList() {
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

    const init = {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${localStorage.getItem("token")}`,
      },
    };
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
