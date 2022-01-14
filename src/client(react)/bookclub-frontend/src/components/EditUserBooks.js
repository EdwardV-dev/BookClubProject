import { Link, useHistory, useParams } from "react-router-dom";
import AuthContext from "../context/AuthContext";
import { useContext, useEffect, useState } from "react";

function EditUserBooks() {
    const [userStatus, setUserStatus] = useContext(AuthContext);

    const currentBook = {
        appUserId: localStorage.getItem("userId"),
        completionStatus: "",
        book: {
            idBooks: "",
            bookTitle: ""
        }
    }

    
    const history = useHistory();
    const [userBook, setUserBook] = useState(currentBook);
    const { id } = useParams();
    const [errors, setErrors] = useState([]);
    const [bookTitle, setBookTitle] = useState("")

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
              //.then(data => setUserBook(data))
              .then(function(data) {
                  setUserBook(data);
                  return data.bookTitle;
              }).then(function(title) {
                  setBookTitle(title);
              })
              .catch(console.log);
            }
        }, 
        [id]
    );

    console.log(userBook)

    const handleChange = (event) => {
        setUserBook({ 
            appUserId: localStorage.getItem("userId"),
            completionStatus: event.target.value,
            book: {
                idBooks: id
            }
        });
    }

    const handleSubmit = async (event) => {
        event.preventDefault();

        const updatedBook = {
            ...userBook
        };

        try {
            const init = {
                method: "PUT",
                headers: {
                    "Content-Type": "application/json",
                    Authorization: `Bearer ${localStorage.getItem("token")}`
                },
                body: JSON.stringify(updatedBook)
            };

            const response = await fetch(
                `http://localhost:8080/books/${id}`, 
                init);
                if (response.status === 204) {
                    history.push("/books");
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
    }

    const handleCancel = async (event) => {
        history.push("/books");
    }

    return (
        <form onSubmit={handleSubmit} className="form-inline mx-2 my-4 text-light ">
        <div class="container">
        <div class="row justify-content-md-center">
        <div class="col col-lg-2">
        </div>
        <div class="col-md-auto">

            <h2 class="text-center" >Update completion status for:</h2>
            <div>
                <h3 class="text-center">{bookTitle}</h3>
            </div>

            <div>
                <div class="form-check">
                    <input 
                        class="form-check-input"
                        type="radio" 
                        value="WantToRead" 
                        name="completionStatus"
                        required
                        onChange={handleChange}
                    />
                    Want to Read
                </div>
                <div class="form-check">
                    <input 
                        class="form-check-input"
                        type="radio" 
                        value="Reading"
                        name="completionStatus"
                        onChange={handleChange} 
                    /> 
                    Reading
                </div>
                <div class="form-check">
                    <input 
                        class="form-check-input"
                        type="radio" 
                        value="DoneReading"
                        name="completionStatus"
                        onChange={handleChange} 
                    /> 
                    Done Reading
                </div>

                <div>
                    <button type="submit" className="btn btn-success ml-2">
                        Update
                    </button> &nbsp;
                    <button type="button" className="btn btn-warning ml-2" onClick={handleCancel}>
                        Cancel
                    </button>
                </div>
            </div>
            
        </div>
        <div class="col col-lg-2">
        </div>
        </div>
        </div>
        </form>
    );
}

export default EditUserBooks;