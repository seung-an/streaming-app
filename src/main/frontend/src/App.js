import React, { useEffect, useState } from "react";
import { BrowserRouter as Router, Route, Routes } from "react-router-dom";
import Layout from "./components/layout/Layout";
import Login from "./pages/Login";
import Home from "./pages/Home";
import Join from "./pages/Join";
import TestPage from "./pages/TestPage";
import MyVideos from "./pages/MyVideos";

function App() {
  useEffect(() => {
    console.log("render App");
  }, []);

  return (
    <div>
      <Router>
        <Routes>
          <Route element={<Layout />}>
            <Route path={"/"} element={<Home />} />
            <Route path={"/test"} element={<TestPage />} />
            <Route path={"/myVideos"} element={<MyVideos />} />
          </Route>
          <Route path={"/login"} element={<Login />} />
          <Route path={"/join"} element={<Join />} />
        </Routes>
      </Router>
    </div>
  );
}

export default App;
