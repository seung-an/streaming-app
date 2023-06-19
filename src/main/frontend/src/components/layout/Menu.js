import styles from "styles/layout/Menu.module.css";
import { Link, useLocation } from "react-router-dom";
import MenuItem from "./MenuItem";
function Menu() {
  const pathName = useLocation().pathname;

  const menus = [
    { name: "홈", path: "/" },
    { name: "시청 기록", path: "/history" },
    { name: "내 동영상", path: "/myVideos" },
    { name: "테스트 메뉴", path: "/test" },
  ];
  return (
    <div className={styles.menu}>
      {menus.map((menu, index) => {
        return (
          <Link to={menu.path} key={index}>
            <MenuItem
              menu={menu}
              isActive={pathName === menu.path ? true : false}
            />
          </Link>
        );
      })}
    </div>
  );
}

export default Menu;
