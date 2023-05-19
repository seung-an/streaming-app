import Header from "./Header";
import Menu from "./Menu";
import { Outlet, useLocation } from "react-router-dom";
import styles from "styles/layout/Layout.module.css";

function Layout() {
  const pathName = useLocation().pathname;
  if (pathName.includes("admin")) {
  }
  return (
    <div>
      <Header />
      <Menu isAdmin={pathName.includes("admin")} />
      <div className={styles.contents}>
        <Outlet />
      </div>
    </div>
  );
}

export default Layout;
