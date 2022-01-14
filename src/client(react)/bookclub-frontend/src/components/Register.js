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
        <h2 className="text-light"style={{display: 'flex', justifyContent: 'center'}}>Register</h2>

      {errors.map((error, i) => (
        <Error key={i} msg={error} />
      ))}

      <form onSubmit={handleSubmit} className="form-inline mx-2 my-4 text-light">
        <div class="container">
        <div class="row justify-content-md-center">
        <div class="col col-lg-2">
        </div>
        <div class="col-md-auto">
        <label htmlFor = "username">Username:</label>
          <input
            class="form-control"
            type="text"
            id="username"
            onChange={(event) => setUsername(event.target.value)}
          />
    </div>
    <div class="col col-lg-2">
    </div>
        </div>
        
        <div class="row justify-content-md-center">
    <div class="col col-lg-2">
    </div>
    <div class="col-md-auto">
      <label>Password:</label>
          <input
            class="form-control"
            type="text"
            id="password"
            onChange={(event) => setPassword(event.target.value)}
          />
    </div>
    <div class="col col-lg-2">
    </div>
  </div>
<br></br>
        <div class="row justify-content-md-center">
    <div class="col col-lg-2">
    
    </div>
    <div class="col-md-auto">
    <button type="submit" class="btn btn-info text-light">Register</button>

    </div>
    <div class="col col-lg-2">
      
    </div>
  </div>
        </div>
      </form>
        </>
    );
}

export default Register;