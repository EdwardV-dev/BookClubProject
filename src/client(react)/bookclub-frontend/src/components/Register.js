import { Link, useHistory } from "react-router-dom";

function Register() {
    
    const history = useHistory();

    const handleSubmit = async (event) => {
        event.preventDefault();

        history.push("/");
    }

    return (
        <>
        <div>
            <h2>Register</h2>

            <form onSubmit={handleSubmit}>
                <div>
                    <label>Username</label>
                    <input type="text" id="username" />
                </div>

                <div>
                    <label>Password</label>
                    <input type="text" id="password" />
                </div>

                <div>
                    <button type="submit">Register</button>
                </div>
            </form>
        </div>
        </>
    );
}

export default Register;