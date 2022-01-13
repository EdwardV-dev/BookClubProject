import { Link, useHistory } from "react-router-dom";
import React, { useState, useEffect, useContext } from "react";
import AuthContext from "../context/AuthContext";

function CompletionStatus({idBooks}) {

  const[completionStatus, setCompletionStatus] = useState();

  useEffect(() => {
    const init = {
       
        method: "GET",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${localStorage.getItem("token")}`
        }
    };
   
    // 2. fetch initial books for logged-in user
    fetch(`http://localhost:8080/books/completionStatusFinder?userId=${localStorage.getItem("userId")}&bookId=${idBooks}`, init)
      .then((response) => {
        if (response.status !== 200) {
          return Promise.reject("books fetch failed");
        }
        return response.text();
      })
      .then((text) => setCompletionStatus(text))
      .catch(console.log);
}, []);



return (<>{completionStatus}</>);
}
export default CompletionStatus;