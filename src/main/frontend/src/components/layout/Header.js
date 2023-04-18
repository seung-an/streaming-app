import styles from "styles/layout/Header.module.css";
import searchIcon from "icons/icon_search.png";
import { Link } from "react-router-dom";
import { useEffect } from "react";

function Header() {
  useEffect(() => {}, []);

  return (
    <header className={styles.header}>
      <div className={styles.logoArea}>
        <Link to={"/"}>
          <div>Logo</div>
        </Link>
      </div>
      <div className={styles.searchArea}>
        <form className={styles.searchBox}>
          <input type="text" className={styles.searchInput} />
          <button type="submit" className={styles.searchBtn}>
            <img src={searchIcon} />
          </button>
        </form>
      </div>
      <div className={styles.accountArea}>
        <Link to={"/login"}>
          <div>로그인</div>
        </Link>
      </div>
    </header>
  );
}

export default Header;
