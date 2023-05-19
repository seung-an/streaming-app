import styles from "styles/layout/MenuItem.module.css";
function MenuItem({ menu, isActive }) {
  return isActive === true ? (
    <div className={`${styles.eachMenu} ${styles.active}`}>{menu.name}</div>
  ) : (
    <div className={`${styles.eachMenu}`}>{menu.name}</div>
  );
}

export default MenuItem;
