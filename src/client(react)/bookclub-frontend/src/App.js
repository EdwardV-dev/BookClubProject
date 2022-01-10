import React from 'react';
import { BrowserRouter as Router, Route, Switch } from "react-router-dom";
import Home from './components/Home';
import NavBar from './components/NavBar';
import MyBooks from './components/MyBooks';
import Login from './components/Login';
import Register from './components/Register';
import Admin from './components/Admin';
import Recommend from './components/Recommend';
import EditUserBooks from './components/EditUserBooks';
import EditAdminBooks from './components/EditAdminBooks';
import AddBook from './components/AddBook';
import AuthContext from "./context/AuthContext";
import {useState, useEffect, useHistory} from "react";

function App() {
  const [userStatus, setUserStatus] = useState();

    return (
      <Router>
<AuthContext.Provider value={[userStatus, setUserStatus]}>

        <NavBar 
        role={userStatus?.user.authorities}
        />

        <Switch>
          <Route path="/books">
            <MyBooks />
          </Route>

          <Route path="/login">
            <Login />
          </Route>

          <Route path="/register">
            <Register />
          </Route>

          <Route path="/add">
            <AddBook />
          </Route>

          <Route path="/admin">
            <Admin />
          </Route>

          <Route path="/recommend">
            <Recommend />
          </Route>

          <Route path="/booksUser">
            <EditUserBooks />
          </Route>

          <Route path="/booksAdmin">
            <EditAdminBooks />
          </Route>

          <Route exact path="/">
            <Home />
          </Route>

        </Switch>
        </AuthContext.Provider>
      </Router>
    );
  }



export default App;