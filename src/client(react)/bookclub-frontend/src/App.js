import React from 'react';
import { BrowserRouter as Router, Route, Switch } from "react-router-dom";
import Home from './components/Home';
import NavBar from './components/NavBar';
import MyBooks from './components/MyBooks';
import Login from './components/Login';
import Register from './components/Register';
import Admin from './components/Admin';

class App extends React.Component {
  render() {
    return (
      <Router>

        <NavBar />

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

          <Route path="/admin">
            <Admin />
          </Route>

          <Route exact path="/">
            <Home />
          </Route>

        </Switch>
      </Router>
    );
  }

}

export default App;