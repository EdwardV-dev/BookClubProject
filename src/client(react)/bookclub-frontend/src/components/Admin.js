import { Link, useHistory } from "react-router-dom";
import React, { useState, useEffect, useContext } from "react";
import ApprovalStatus from "./ApprovalStatus";
import AuthContext from "../context/AuthContext";
import YearPublished from "./YearPublished";
function Admin() {

    const [books, setBooks] = useState([]);
    const [flag, setFlag] = useState(false);
    const history = useHistory();

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
    }, [flag]);

    const handleApprove = async (book) => {
      setFlag(true);

      const approvedBook = {
          ...book,
          approvalStatus: true
      };

      console.log(approvedBook);

      try {
          const init = {
              method: "PUT",
              headers: {
                  "Content-Type": "application/json",
                  Authorization: `Bearer ${localStorage.getItem("token")}`
              },
              body: JSON.stringify(approvedBook)
          };

          const response = await fetch(
              `http://localhost:8080/booksAdmin/${book.idBooks}`, 
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
      setFlag(false); 
    }

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
                <th scope="col">Actions</th>
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
                  <td><ApprovalStatus boolean={book.approvalStatus} /></td>
                  <td>
                    <div className="float-right">
                      <Link to={`/booksAdminEdit/${book.idBooks}`}>Edit</Link> &nbsp;
                      {book.approvalStatus ? (
                        null
                      ) : (
                        <button type="button" className="btn btn-success ml-2" onClick={() => handleApprove(book)}>
                          Approve
                        </button>
                      )}
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