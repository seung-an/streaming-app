import React, { useEffect, useState } from "react";
import Header from "components/layout/Header";
import Menu from "components/layout/Menu";
import Contents from "components/layout/Contents";

function App() {
  useEffect(() => {}, []);

  return (
    <div>
      <Header />
      <Menu />
      <Contents />
    </div>
  );
}

export default App;
