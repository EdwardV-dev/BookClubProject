import { Link, useHistory } from "react-router-dom";
import React, { useState, useEffect, useContext } from "react";
import AuthContext from "../context/AuthContext";

function MyBooks() {
  const history = useHistory();

  const [books, setBooks] = useState([]);

  const [userStatus, setUserStatus] = useContext(AuthContext);



  useEffect(() => {
    
    const init = {
        method: "GET",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${localStorage.getItem("token")}`
        }
    };
   
    // 2. fetch initial books for logged-in user
    fetch(`http://localhost:8080/books/userId?userId=${localStorage.getItem("userId")}`, init)
      .then((response) => {
        if (response.status !== 200) {
          return Promise.reject("books fetch failed");
        }
        return response.json();
      })
      .then((json) => setBooks(json))
      .catch(console.log);
  }, []);

  const handleAuthorNameSubmit = async (event) => {
    event.preventDefault();

    //history.push("/books");
  };

  return (
    <>
      <form onSubmit={handleAuthorNameSubmit}>
        <input
          type="text"
          id="search"
          name="search"
          placeholder="Author Last/First Name"
        />
        <button type="submit">Search</button>
      </form>
      <div>
        <button type="button" className="btn btn-primary mx-2 my-4">
          Add Book
        </button>
      </div>

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
          {books.map((book) => (
            <tr key={book.idBooks}>
              <td>{book.bookTitle}</td>
              <td>{book.genre}</td>
              <td>{book.yearPublished}</td>
              <td>{book.author.authorFirstName}</td>
              <td>{book.author.authorLastName}</td>

              <td>
                <div className="float-right">
                  <Link to={`/booksUserEdit/${book.idBooks}`}>Edit</Link> &nbsp;
                  <Link to={"/books"}>Delete placeholder (routes to booklist)</Link>
                </div>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </>
  );
}


export default MyBooks;
