import styles from "styles/layout/Contents.module.css";
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import Home from "pages/Home";

function Contents() {
  return (
    <div className={styles.contents}>
      <Router>
        <Routes>
          <Route path={`${process.env.PUBLIC_URL}/`} element={<Home />}></Route>
        </Routes>
      </Router>
    </div>
  );
}

export default Contents;
