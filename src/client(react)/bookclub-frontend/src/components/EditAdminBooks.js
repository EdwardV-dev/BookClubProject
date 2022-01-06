import { Link, useHistory } from "react-router-dom";

function EditAdminBooks() {

    const history = useHistory();

    const handleSubmit = async (event) => {
        event.preventDefault();

        history.push("/admin");
    }

    return (
        <form onSubmit={handleSubmit}>
            <div>
                <input type="text" id="author" name="author" value="Current Author" />
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
                <button type="submit">Submit</button>
            </div>
        </form>
    );
}

export default EditAdminBooks;