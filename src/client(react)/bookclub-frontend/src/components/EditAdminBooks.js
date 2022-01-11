import { useContext, useEffect, useState } from "react";
import AuthContext from "../context/AuthContext";
import { Link, useHistory, useParams } from "react-router-dom";

function EditAdminBooks() {

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
    //const { id } = useParams();
    const [errors, setErrors] = useState([]);
    const id = 2;

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
        const { name, value } = event.target;
        setBook({...book, [name]: value});
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
                `http://localhost:8080/booksAdmin/1`, 
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
        }    }

    return (
        book&& (
        <form onSubmit={handleSubmit} className="form-inline mx-2 my-4">
             <div>
                <input 
                type="text" 
                id="authorFirstName" 
                name="authorFirstName" 
                value={book.author.authorFirstName}
                required
                onChange={handleChange}
                />
            </div>
            <div>
                <input 
                type="text" 
                id="authorLastName" 
                name="authorLastName" 
                value={book.author.authorLastName} 
                required
                onChange={handleChange}
                />
            </div>
            <div>
                <input 
                type="text" 
                id="bookTitle" 
                name="bookTitle" 
                value={book.bookTitle}
                required
                onChange={handleChange} 
                />
            </div>
            <div>
                <input 
                type="text" 
                id="genre" 
                name="genre" 
                value={book.genre}
                required
                onChange={handleChange} 
                />
            </div>
            <div>
                <input 
                type="text" 
                id="yearPublished" 
                name="yearPublished" 
                value={book.yearPublished}
                onChange={handleChange} 
                />
            </div>

            <div>
                <button type="submit" className="btn btn-success ml-2">
                    Update
                </button>            
            </div>
        </form>
        )
    );
}

export default EditAdminBooks;