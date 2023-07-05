import Header from "./Header";
import Menu from "./Menu";
import { Outlet, useLocation } from "react-router-dom";
import styles from "styles/layout/Layout.module.css";
import { useEffect, useState } from "react";
import { authApi } from "../../api/api";

function Layout() {
  const [subscribeList, setSubscribeList] = useState([]);

  const getSubscribeList = async () => {
    await authApi.get("/api/subscribe/getSubscribeList").then((response) => {
      setSubscribeList(response.data.data);
    });
  };

  useEffect(() => {
    getSubscribeList().then();
  }, []);

  return (
    <div>
      <Header />
      <Menu subscribeList={subscribeList} />
      <div className={styles.contents}>
        <Outlet context={{ getSubscribeList }} />
      </div>
    </div>
  );
}

export default Layout;
