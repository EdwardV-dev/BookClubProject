import { Link, useHistory } from "react-router-dom";
import AuthContext from "../context/AuthContext";
import { useContext, useEffect } from "react";
import jwtDecode from "jwt-decode";


function NavBar({role}) {
    const [userStatus, setUserStatus] = useContext(AuthContext); 
    const history = useHistory();

      // "user" represents an object with multiple properties
      useEffect(() => {
        const token = localStorage.getItem("token");
        if (token) {
          setUserStatus({ user: jwtDecode(token) });
        }
      }, [setUserStatus]);

   

    return (
        <nav className="nav nav-pills">
            {userStatus?.user ? (
           <li>
            <button
              type="button" className="btn btn-primary"
              onClick={() => {
                setUserStatus(null);
                localStorage.removeItem("token");
                localStorage.removeItem("userId");
                history.push("/")
              }}
            >
              {/* `sub` is the property from the decoded token */}
               Logout {userStatus.user.sub}
             </button>
           </li>
        ) : (
          <>
            <Link to="/login">
                <button type="button" className="btn btn-primary ml-2">
                    Login
                </button>
            </Link>
            &nbsp; &nbsp;
          </>
       )}

       &nbsp; &nbsp;

          <Link to={"/register"}>
                <button type="button" className="btn btn-secondary ml-2">
                    Register
                </button>
          </Link>

            &nbsp; &nbsp;

           {userStatus?.user ? (<Link to={"/books"} onClick={() => document.location.pathname === "/books" ? document.location.reload() : false}>
               <button type="button" className="btn btn-secondary ml-2">
                    My Books
                </button></Link> ) : (<>&nbsp;</>) }

            &nbsp; &nbsp;

            {userStatus?.user ? ( <Link to="/recommend" onClick={() => document.location.pathname === "/recommend" ? document.location.reload() : false}>
                <button type="button" className="btn btn-secondary ml-2">
                    Recommended
                </button></Link>) : (<>&nbsp;</>) }

            &nbsp; &nbsp;

           {role === "ROLE_ADMIN" && ( <Link to="/admin">
                <button type="button" className="btn btn-secondary ml-2">
                    Admin
                </button></Link> )}
        </nav>
    )
}

export default NavBar;