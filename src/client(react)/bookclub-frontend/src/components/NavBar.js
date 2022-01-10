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
            <button type="bugit sttustton" className="btn btn-success ml-2">
                Register
            </button>

           {userStatus?.user ? (<button type="button" className="btn btn-success ml-2">
                My Books
            </button> ) : (<>&nbsp;</>) }

            {userStatus?.user ? (<button type="button" className="btn btn-success ml-2">
                Recommended
            </button>) : (<>&nbsp;</>) }

           {role === "ROLE_ADMIN" && ( <button type="button" className="btn btn-success ml-2">
                Admin
            </button> )}
        </nav>
    )
}

export default NavBar;