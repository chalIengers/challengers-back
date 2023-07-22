import logo from './logo.svg';
import './App.css';
import {useEffect, useState} from "react";
function App() {
  const [message, setMessage] = useState("");

  useEffect(() => {
    fetch("/test/react")
        .then((response) => response.json())
        .then((json) => setMessage(json.SUCCESS_TEXT));
  }, []);

  return (
    <div className="App">
      <header className="App-header">
        <img src={logo} className="App-logo" alt="logo" />
        <p>
          {message}
        </p>
      </header>
    </div>
  );
}

export default App;
