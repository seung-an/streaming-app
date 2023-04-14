import React, { useEffect, useState } from "react";
import Header from "./components/Layout/Header";
import Footer from "./components/Layout/Footer";
import Menu from "./components/Layout/Menu";
import Contents from "./components/Layout/Contents";

function App() {
  useEffect(() => {}, []);

  return (
    <div>
      <Header />
      <Menu />
      <Contents />
      <Footer />
    </div>
  );
}

export default App;
