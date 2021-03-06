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

    // This code executes if the login request is successful
    if (response.status === 200) {
      const { jwt_token } = await response.json();
     
      localStorage.setItem("token", jwt_token);

      let userId = 0;
      
      const user = jwtDecode(jwt_token); //You can now have access to jwtToken properties, like sub for username

      const getData = async () => {
        try {
          const init = {
            method: "GET",
            headers: {
              Authorization: `Bearer ${jwt_token}`,
            },
          };

          const response = await fetch(
            `http://localhost:8080/books/userName?userName=${user.sub}`,
            init
          );
          // assume that it's a 200
          userId = await response.json();
          console.log("Current user id: " + userId);
          console.log(jwt_token);

          // Update the user status in the context with the decoded token stuff.
          setUserStatus({ user: jwtDecode(jwt_token)});
          localStorage.setItem("userId", userId);

          history.push("/books");
          
        } catch (error) {
          console.log(error);
        }
      };

      getData();
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
      <h2 className="text-light"style={{display: 'flex', justifyContent: 'center'}}>Login</h2>

      {errors.map((error, i) => (
        <Error key={i} msg={error} />
      ))}

      <form onSubmit={handleSubmit} style={{display: 'flex', justifyContent: 'center'}} className="form-inline mx-2 my-4 text-light">
        <div class="container">
        <div class="row justify-content-md-center">
        <div class="col col-lg-2">
        </div>
        <div class="col-md-auto">
        <label>Username:</label>
          <input
            class="form-control"
            type="text"
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
            type="password"
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
    <button type="submit" class="btn btn-info text-light">Login</button>
    </div>

    <div class="col col-lg-2">
      
    </div>
  </div>
        </div>
      </form>
    </div>
  );
  
}
