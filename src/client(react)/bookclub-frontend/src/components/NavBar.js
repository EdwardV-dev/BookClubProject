import { Link, useHistory } from "react-router-dom";
import AuthContext from "../context/AuthContext";
import { useContext, useEffect } from "react";
import jwtDecode from "jwt-decode";

function NavBar({role}) {
    const [userStatus, setUserStatus] = useContext(AuthContext); 

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
          
              }}
            >
              {/* `sub` is the property from the decoded token */}
               Logout {userStatus.user.sub}
             </button>
           </li>
        ) : (
          <li>
            <Link to="/login">Login</Link>
          </li>
       )}
            <button type="bugit sttustton" className="btn btn-success ml-2">
                Register
            </button>
            <button type="button" className="btn btn-success ml-2">
                My Books
            </button>
            <button type="button" className="btn btn-success ml-2">
                Recommended
            </button>
            <button type="button" className="btn btn-success ml-2">
                Admin
            </button>
        </nav>
    )
}

export default NavBar;