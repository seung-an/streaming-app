import Header from "./Header";
import Menu from "./Menu";
import { Outlet } from "react-router-dom";
import styles from "styles/layout/Layout.module.css";

function Layout() {
  return (
    <div>
      <Header />
      <Menu />
      <div className={styles.contents}>
        <Outlet />
      </div>
    </div>
  );
}

export default Layout;
