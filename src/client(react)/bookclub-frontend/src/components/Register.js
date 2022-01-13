import { Link, useHistory } from "react-router-dom";
import Error from "./Error";
import React, { useState, useContext } from "react";


function Register() {
    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");
    const [errors, setErrors] = useState([]);
    
    const history = useHistory();



    const handleSubmit = async (event) => {
        event.preventDefault();

        const response = await fetch("http://localhost:8080/create_account", {
            method: "POST",
            headers: {
              "Content-Type": "application/json",
            },
            body: JSON.stringify({
              username,
              password,
            }),
          });

          if(response.status === 201){
              console.log("Success!")
              history.push("/");
             //Anything other than a 201 is an error
          } else { 
            const errors = await response.json();
            setErrors(errors);
          }

        
    }

    return (
        <>
        <div>
            <h2>Register</h2>

            {errors.map((error, i) => (
        <Error key={i} msg={error} />
      ))}

            <form onSubmit={handleSubmit}>
                <div>
                    <label htmlFor = "username">Username</label>
                    <input type="text" id="username" onChange={(event) => setUsername(event.target.value)}/>
                </div>

                <div>
                    <label>Password</label>
                    <input type="text" id="password" onChange={(event) => setPassword(event.target.value)}/>
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