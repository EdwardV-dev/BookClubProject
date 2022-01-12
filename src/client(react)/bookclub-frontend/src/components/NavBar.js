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
        <nav>
            {userStatus?.user ? (
           <li>
            <button
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
            <Link to="/login">Login</Link>
          </>
       )}

          <Link to={"/register"}>Register</Link>

            &nbsp; &nbsp;

           {userStatus?.user ? (<Link to={"/books"}>My Books</Link> ) : (<>&nbsp;</>) }

            &nbsp; &nbsp;

            {userStatus?.user ? ( <Link to="/recommend">Recommended</Link>) : (<>&nbsp;</>) }

            &nbsp; &nbsp;

           {role === "ROLE_ADMIN" && ( <Link to="/admin">Admin</Link> )}
        </nav>
    )
}

export default NavBar;