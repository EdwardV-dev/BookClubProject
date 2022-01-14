import { useHistory } from "react-router-dom";

function Error({ msg }) {
  const history = useHistory();

  return (
    <p className="text-light"style={{display: 'flex', justifyContent: 'center'}}>
      🙅🏾‍♂️ {" "}
      {history.location.state ? ` - ${history.location.state.msg}` : ""}
      {msg}
    </p>
  );
}

export default Error;