import { Link, useHistory } from "react-router-dom";
import React, { useState, useContext } from "react";
import jwtDecode from "jwt-decode";
import Error from "./Error";
import AuthContext from "../context/AuthContext";

export default function Login({ userStatus }) {
    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");
    const [errors, setErrors] = useState([]);
   
  // The context wraps `useState` from App.js.
    // We only care about updating - not the state itself.
    const [_, setUserStatus] = useContext(AuthContext);
  
    const history = useHistory();
    
    const handleSubmit = async (event) => {
      event.preventDefault();
    
      const response = await fetch("http://localhost:8080/authenticate", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({
          username,
          password,
        }),
      });
    
      // This code executes if the request is successful
      if (response.status === 200) {
        const { jwt_token } = await response.json();
        console.log(jwt_token)
        console.log(jwtDecode(jwt_token));
  
        localStorage.setItem("token", jwt_token);
  
       // Update the user status in the context with the decoded token stuff.
       setUserStatus({ user: jwtDecode(jwt_token) });
        history.push("/");
  
      } else if (response.status === 400) {
        const errors = await response.json();
        setErrors(errors);
      } else if (response.status === 403) {
        setErrors(["Login failed."]);
      } else {
        setErrors(["Unknown error."]);
      }
    };
    
    return (
      <div>
        <h2>Login</h2>
    
        {errors.map((error, i) => (
          <Error key={i} msg={error} />
        ))}
    
        <form onSubmit={handleSubmit}>
          <div>
            <label>Username:</label>
            <input
              type="text"
              onChange={(event) => setUsername(event.target.value)}
            />
          </div>
          <div>
            <label>Password:</label>
            <input
              type="password"
              onChange={(event) => setPassword(event.target.value)}
            />
          </div>
          <div>
            <button type="submit">Login</button>
          </div>
        </form>
      </div>
    );
    
  }

