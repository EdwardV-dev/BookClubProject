import { Link, useHistory } from "react-router-dom";

function AddBook() {

    const history = useHistory();

    const handleSubmit = async (event) => {
        event.preventDefault();

        history.push("/books");
    }

    return (
        <form onSubmit={handleSubmit}>
        <div>
            <input type="text" id="authorFirstName" name="authorFirstName" value="Author First Name" />
        </div>
        <div>
            <input type="text" id="authorLastName" name="authorLastName" value="Author Last Name" />
        </div>
        <div>
            <input type="text" id="title" name="title" value="Current Title" />
        </div>
        <div>
            <input type="text" id="genre" name="genre" value="Current Genre" />
        </div>
        <div>
            <input type="text" id="year" name="year" value="Year published" />
        </div>
        
        <div>
            <input type="radio" value="WantToRead" name="status" /> Want to Read
        </div>
        <div>
            <input type="radio" value="Reading" name="status" /> Reading
        </div>
        <div>
            <input type="radio" value="DoneReading" name="status" /> Done Reading
        </div>

        <div>
            <button type="submit">Submit</button>
        </div>
    </form>
    );
}

export default AddBook;