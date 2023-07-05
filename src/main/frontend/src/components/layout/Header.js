import styles from "styles/layout/Header.module.css";
import searchIcon from "icons/icon-search.png";
import logoutIcon from "icons/icon-logout.png";
import { Link, useNavigate } from "react-router-dom";
import { useEffect, useState } from "react";
import cookie from "react-cookies";

function Header() {
  const isLogin = localStorage.getItem("isLogin");
  const navigate = useNavigate();
  const [searchQuery, setSearchQuery] = useState();

  const changeSearchQuery = (e) => {
    setSearchQuery(e.target.value);
  };

  useEffect(() => {}, []);

  const logout = () => {
    localStorage.removeItem("accessToken");
    cookie.remove("refreshToken");
    localStorage.setItem("isLogin", "F");
  };

  return (
    <header className={styles.header}>
      <div className={styles.logoArea}>
        <Link to={"/"}>
          <div>Logo</div>
        </Link>
      </div>
      <div className={styles.searchArea}>
        <form className={styles.searchBox} action={"/search"} method={"get"}>
          <input
            value={searchQuery}
            type="text"
            name={"searchQuery"}
            className={styles.searchInput}
            onChange={changeSearchQuery}
            spellcheck="false"
          />
          <button type="submit" className={styles.searchBtn}>
            <img src={searchIcon} />
          </button>
        </form>
      </div>
      <div className={styles.accountArea}>
        <Link to={"/login"}>
          {isLogin === "T" ? (
            <img
              className={styles.logoutIcon}
              onClick={logout}
              src={logoutIcon}
            />
          ) : (
            <div>로그인</div>
          )}
        </Link>
      </div>
    </header>
  );
}

export default Header;
