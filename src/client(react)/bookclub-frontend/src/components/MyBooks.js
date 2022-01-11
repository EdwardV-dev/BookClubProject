import { Link, useHistory } from "react-router-dom";
import React, { useState, useEffect, useContext } from "react";
import AuthContext from "../context/AuthContext";
import CompletionStatus from "./CompletionStatus";
import YearPublished from "./YearPublished";

function MyBooks() {
  const history = useHistory();

  const [books, setBooks] = useState([]);

  const [userStatus, setUserStatus] = useContext(AuthContext);

  const [authorName, setAuthorName] = useState("");


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

    const handleInputChange = (event) => {
    
        setAuthorName(event.target.value); //Same property name that "...agent" contains is replaced by the new value. Id is not one of those values
        console.log(authorName);
    };

  const handleAuthorNameSubmit = async (event) => {
    event.preventDefault();

    const init = {
        method: "GET",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${localStorage.getItem("token")}`
        },

    };

    fetch(`http://localhost:8080/authorBooks/${authorName}/${localStorage.getItem("userId")}`, init)
      .then((response) => {
        if (response.status !== 200) {
          return Promise.reject("books fetch failed");
        }
        return response.json();
      })
      .then((json) => setBooks(json))
      .catch(console.log);
 
  };


  return (
    <>
      <form onSubmit={handleAuthorNameSubmit}>
        <input
          type="text"
          id="search"
          name="search"
          value={authorName} //Initial value is blank via useState but changes onChange
          onChange={handleInputChange}
          placeholder="Author Last/First Name"
        />
        <button type="submit">Search</button>
      </form>

      <div>
      <br></br>
      <Link to={"/add"}>Add Book</Link>
      </div>

      <table className="table table-striped table-dark table-hover">
        <thead>
          <tr>
            <th scope="col">Book Title</th>
            <th scope="col">Genre</th>
            <th scope="col">Year Published</th>
            <th scope="col">Author First Name</th>
            <th scope="col">Author Last Name</th>
            <th scope="col">Completion Status</th>
          </tr>
        </thead>
        <tbody>
        {/*for every book in books, grab their id and set it as being the key*/}
          {books.map((book) => (
            <tr key={book.idBooks}>
              <td>{book.bookTitle}</td>
              <td>{book.genre}</td>
              <td><YearPublished year={book.yearPublished}/></td>
              <td>{book.author.authorFirstName}</td>
              <td>{book.author.authorLastName}</td>
              <td><CompletionStatus idBooks={book.idBooks}/></td>
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
