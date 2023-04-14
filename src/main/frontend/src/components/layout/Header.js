import styles from "styles/layout/Header.module.css";

function Header() {
  return (
    <header className={styles.header}>
      <div className={styles.logo}>Logo</div>
      <div className={styles.search}>Search</div>
      <div className={styles.account}>Account</div>
    </header>
  );
}

export default Header;
