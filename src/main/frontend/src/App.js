import React, { useEffect, useState } from "react";
import { BrowserRouter as Router, Route, Routes } from "react-router-dom";
import Layout from "./components/layout/Layout";
import Login from "./pages/Login";
import Home from "./pages/Home";

function App() {
  useEffect(() => {}, []);

  return (
    <div>
      <Router>
        <Routes>
          <Route element={<Layout />}>
            <Route path={"/"} element={<Home />} />
            <Route path={"/test"} element={<div>test</div>} />
          </Route>
          <Route path={"/login"} element={<Login />} />
        </Routes>
      </Router>
    </div>
  );
}

export default App;
