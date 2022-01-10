import { Link, useHistory } from "react-router-dom";

function EditUserBooks() {

    const history = useHistory();

    const handleSubmit = async (event) => {
        event.preventDefault();

        history.push("/books");
    }

    return (
        <>
        <div>
            <label>Book Title</label>
        </div>
        <div>
            <form onSubmit={handleSubmit}>
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
        </div>
        </>
    );
}

export default EditUserBooks;