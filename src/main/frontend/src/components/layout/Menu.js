import { Dropdown } from "react-bootstrap";
import "bootstrap/dist/css/bootstrap.min.css";
import styles from "styles/layout/Menu.module.css";
import { Link } from "react-router-dom";

function Menu() {
  return (
    <Dropdown.Menu show className={styles.menu} variant={"dark"}>
      <Dropdown.Item className={styles.eachMenu} as={Link} to={"/"}>
        홈
      </Dropdown.Item>
      <Dropdown.Item className={styles.eachMenu} as={Link} to={"/test"}>
        시청기록
      </Dropdown.Item>
      <Dropdown.Item className={styles.eachMenu}>재생목록</Dropdown.Item>
      <Dropdown.Item className={styles.eachMenu}>구독</Dropdown.Item>
      <Dropdown.Item className={styles.eachMenu}>설정</Dropdown.Item>
      <Dropdown.Item className={styles.eachMenu}>내 채널</Dropdown.Item>
    </Dropdown.Menu>
  );
}

export default Menu;
