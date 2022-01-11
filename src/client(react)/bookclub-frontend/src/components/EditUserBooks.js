import { Link, useHistory, useParams } from "react-router-dom";
import AuthContext from "../context/AuthContext";
import { useContext, useEffect, useState } from "react";

function EditUserBooks() {
    const [userStatus, setUserStatus] = useContext(AuthContext);

    const currentBook = {
        appUserId: "",
        completionStatus: "",
        book: {
            idBooks: "",
            bookTitle: ""
        }
    }

    const history = useHistory();
    const [book, setBook] = useState(currentBook);
    //const { id } = useParams();
    const [errors, setErrors] = useState([]);
    const id = 1;

    useEffect(
        () => {
            // Only do this if there is an `id`
            if (id) {
              fetch(`http://localhost:8080/books/1`, {
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

    const handleChange = (event) => {
        setBook({ ...book, 
            appUserId: userStatus?.userId,
            completionStatus: event.target.value,
            book: {
                idBooks: 1,
                bookTitle: "Winnie the Pooh"
            }
        });
    }

    const handleSubmit = async (event) => {
        event.preventDefault();

        const updatedBook = {
            ...book
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
                `http://localhost:8080/books/1`, 
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

    return (
        <>
        <div>
            <label>{book.bookTitle}</label>
        </div>
        <div>
            <form onSubmit={handleSubmit} className="form-inline mx-2 my-4">
                <div>
                    <input 
                        type="radio" 
                        value="WantToRead" 
                        name="completionStatus"
                        onChange={handleChange}
                    />
                    Want to Read
                </div>
                <div>
                    <input 
                        type="radio" 
                        value="Reading"
                        name="completionStatus"
                        onChange={handleChange} 
                    /> 
                    Reading
                </div>
                <div>
                    <input 
                        type="radio" 
                        value="DoneReading"
                        name="completionStatus"
                        onChange={handleChange} 
                    /> 
                    Done Reading
                </div>

                <div>
                    <button type="submit">Submit</button>
                </div>
            </form>
        </div>
        </>
    );
}

export default EditUserBooks;