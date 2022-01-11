import React, { useEffect, useState } from "react";
import { Link, useHistory, useParams } from "react-router-dom";

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

    const [book, setBook] = useState(init);
    const history = useHistory();
    //const { id } = useParams();
    const id = 1;

    useEffect(
        () => {
            // Only do this if there is an `id`
            if (id) {
                fetch(`http://localhost:8080/books/2`, {
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
            const response = await fetch(`http://localhost:8080/2/2`, {
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
                throw new Error(`Agent ID #${id} not found.`);
            } else {
                throw new Error("Soemthing went wrong");
            }
        } catch (error) {
            console.log(error);
        }
    };

    return (
        <form onSubmit={handleSubmit} className="form-inline  mx-2 my-4">
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
                    <td>{book.yearPublished}</td>
                    <td>{book.author.authorFirstName}</td>
                    <td>{book.author.authorLastName}</td>
                    </tr>
            </tbody>
            </table>

            <div className="alert alert-danger">
            WARNING:
            THIS ACTION IS IRREVERSIBLE, ARE YOU SURE?
            </div>
            <div>
                <button type="submit" className="btn btn-danger btn ml-2">
                    Delete Agent
                </button>
            </div>

            <div>
                <Link to="/books">Cancel</Link>
            </div>
        </form>
    )

}

export default DeleteUserBook;