import { Link, useHistory } from "react-router-dom";

function MyBooks() {

    const history = useHistory();

    const handleSubmit = async (event) => {
        event.preventDefault();

        //history.push("/books");
    }

    return (
        <>
            <form onSubmit={handleSubmit}>
                <input type="text" id="search" name="search" placeholder="Author Last/First Name" />
                <button type="submit">Search</button>
            </form>
            <div>
                <button type="button" className="btn btn-primary mx-2 my-4">
                    Add Book
                </button>
            </div>

            <table className="table table-striped table-dark table-hover">
                <thead>
                    <tr>
                        <th scope="col">Book Title</th>
                        <th scope="col">Genre</th>
                        <th scope="col">Year Published</th>
                    </tr>
                </thead>
                <tbody>
                    <tr>
                        <td>test1</td>
                        <td>test1</td>
                        <td>test1</td>
                            <div className="float-right">
                                <button className="btn btn-primary btn-sm">
                                    Edit
                                </button>
                                <button className="btn btn-danger btn-sm ml -2">
                                    Delete
                                </button>
                            </div>
                            
                    </tr>
                </tbody>
            </table>
        </>
    )
}

export default MyBooks;