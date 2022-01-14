import React from "react";
import logo from "../logo.png"

function Home() {



    return (
        <>
        <h2 class="text-light text-center">Welcome!</h2>
        <div style={{display: 'flex', justifyContent: 'center'}}>
            <img src={logo} alt="Logo" />
        </div>
        </>
    );
}

export default Home;