import styles from "styles/layout/Menu.module.css";

function Menu() {
  const temp = () => {
    const arr = [];

    for (let i = 0; i < 20; i++) {
      arr.push(
        <div className={styles.eachMenu} key={i}>
          Menu {i}
        </div>
      );
    }

    return arr;
  };

  return <div className={styles.menu}>{temp()}</div>;
}

export default Menu;
