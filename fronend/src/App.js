import './App.css';
import Login from './components/Login/Login';
import Main from './components/Main/Main';

function App() {
  return (
    <div className="App">
        {(localStorage.getItem("user")===undefined ||  localStorage.getItem("user")== null)?
          <Login/> : <Main/>}
    </div>
  );
}

export default App;
