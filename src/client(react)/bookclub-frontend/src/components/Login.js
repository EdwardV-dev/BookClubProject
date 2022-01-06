import { Link, useHistory } from "react-router-dom";

function Login() {
    
    const history = useHistory();

    const handleSubmit = async (event) => {
        event.preventDefault();

        history.push("/books");
    }

    return (
        <>
        <div>
            <h2>Login</h2>

            <form onSubmit={handleSubmit}>
                <div>
                    <labe>Username</labe>
                    <input type="text" id="username" />
                </div>

                <div>
                    <label>Password</label>
                    <input type="text" id="password" />
                </div>

                <div>
                    <button type="submit">Login</button>
                </div>
            </form>
        </div>
        </>
    );
}

export default Login;