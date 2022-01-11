import { Link, useHistory } from "react-router-dom";
import React, { useState, useEffect, useContext } from "react";
import ApprovalStatus from "./ApprovalStatus";
import AuthContext from "../context/AuthContext";

function Admin() {

    const [books, setBooks] = useState([]);

    useEffect(() => {
    
        const init = {
            method: "GET",
            headers: {
              "Content-Type": "application/json",
              Authorization: `Bearer ${localStorage.getItem("token")}`
            }
        };
       
        // 2. fetch initial books from boooks table
        fetch("http://localhost:8080/booksAdmin", init)
          .then((response) => {
            if (response.status !== 200) {
              return Promise.reject("books fetch failed");
            }
            return response.json();
          })
          .then((json) => setBooks(json))
          .catch(console.log);
        }, []);

    return (
        <>
  
          <table className="table table-striped table-dark table-hover">
            <thead>
              <tr>
                <th scope="col">Book Title</th>
                <th scope="col">Genre</th>
                <th scope="col">Year Published</th>
                <th scope="col">Author First Name</th>
                <th scope="col">Author Last Name</th>
                <th scope="col">Approval Status</th>
              </tr>
            </thead>
            <tbody>
            {/*for every book in books, grab their id and set it as being the key*/}
              {books.map((book) => (
                <tr key={book.idBooks}>
                  <td>{book.bookTitle}</td>
                  <td>{book.genre}</td>
                  <td>{book.yearPublished}</td>
                  <td>{book.author.authorFirstName}</td>
                  <td>{book.author.authorLastName}</td>
                  <td><ApprovalStatus boolean={book.approvalStatus} /></td>
                  <td>
                    <div className="float-right">
                      <Link to={`/booksUserEdit/${book.idBooks}`}>Edit</Link> &nbsp;
                      <Link to={"/books"}>Approve</Link>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </>
      );
}

export default Admin;