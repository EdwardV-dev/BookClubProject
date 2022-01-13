import React, { useEffect, useState } from "react";
import { Link, useHistory, useParams } from "react-router-dom";
import CompletionStatus from "./CompletionStatus";
import YearPublished from "./YearPublished";

function DeleteUserBook() {

    const currentBook = {
        approvalStatus: "",
        bookTitle: "",
        genre: "",
        author: {
          authorFirstName: "",
          authorLastName: ""
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

    const handleSubmit = async event => {
        event.preventDefault();
        
        try {
            const response = await fetch(`http://localhost:8080/books/${localStorage.getItem("userId")}/${id}`, {
                method: "DELETE",
                headers: {
                    "Content-Type": "application/json",
                    Authorization: `Bearer ${localStorage.getItem("token")}`
                },
            });

            if(response.status === 204) {
                history.push("/books")
            } else if (response.status === 403) {
                setErrors(["Not logged in."]);
            } else if (response.status === 404) {
                setErrors([`Agent ID #${id} not found.`]);
            } else {
                setErrors(["Unknown error."]);
            }
        } catch (error) {
            console.log(error);
        }
    };

    const handleCancel = async (event) => {
        history.push("/books");
    }

    return (
        <form onSubmit={handleSubmit} className="form-inline  mx-2 my-4">
            <h2>Delete this book from "My Books"?</h2>
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
                    <tr key={book.idBooks}>
                    <td>{book.bookTitle}</td>
                    <td>{book.genre}</td>
                    <td><YearPublished year={book.yearPublished}/></td>
                    <td>{book.author.authorFirstName}</td>
                    <td>{book.author.authorLastName}</td>
                    <td><CompletionStatus idBooks={id}/></td>
                    </tr>
            </tbody>
            </table>

            <div className="alert alert-danger">
            WARNING:
            THIS ACTION IS IRREVERSIBLE, ARE YOU SURE?
            </div>
            <div>
                <button type="submit" className="btn btn-danger btn ml-2">
                    Delete Book
                </button> &nbsp;
                <button type="button" className="btn btn-warning ml-2" onClick={handleCancel}>
                    Cancel
                </button>   
            </div>
        </form>
    )

}

export default DeleteUserBook;