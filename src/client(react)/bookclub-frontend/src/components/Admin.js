

function Admin() {


    return (
        <>
            <table className="table table-striped table-dark table-hover">
                <thead>
                    <tr>
                        <th scope="col">Book Title</th>
                        <th scope="col">Book Author</th>
                        <th scope="col">Genre</th>
                        <th scope="col">Year Published</th>
                        <th scope="col">Approval Status</th>
                    </tr>
                </thead>
                <tbody>
                    <tr>
                        <td>test1</td>
                        <td>test1</td>
                        <td>test1</td>
                        <td>test1</td>
                            <div className="float-right">
                                <button className="btn btn-primary btn-sm">
                                    Edit
                                </button>
                                <button className="btn btn-success btn-sm ml -2">
                                    Approve
                                </button>
                            </div>
                            
                    </tr>
                </tbody>
            </table>
        </>
    )
}

export default Admin;