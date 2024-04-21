import React, {useEffect} from 'react';
import {useDispatch} from "react-redux";
import {BrowserRouter} from "react-router-dom";
import AppRouter from "./components/AppRouter";
import {auth} from "./actions/auth";
import "./App.css"

function App() {
    const dispatch = useDispatch()

    useEffect(() => {
        dispatch(auth())
    }, [])
  return (
      <BrowserRouter>
          <AppRouter></AppRouter>
      </BrowserRouter>
  );
}

export default App;
