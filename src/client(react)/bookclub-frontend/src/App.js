import React from 'react';
import Home from './components/Home';
import NavBar from './components/NavBar';
import MyBooks from './components/MyBooks';

class App extends React.Component {
  render() {

    const path = window.location.pathname;

    switch (path) {
      case "/myBooks":
        return <MyBooks />;
      default:
        return (
          <>
          <h1>Book Club</h1>
          <NavBar />
          <Home />
          </>
        );  
    }

  
  }
}

export default App;