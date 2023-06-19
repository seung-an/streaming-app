import delIcon from "icons/icon-delete.png";
import delIconHover from "icons/icon-delete-hover.png";
import { useRef, useState } from "react";

function DeleteIcon({ id, delFun, width, height }) {
  const [isHover, setIsHover] = useState();

  const style = {
    width: width + "px",
    height: height + "px",
    cursor: "pointer",
  };

  return (
    <img
      style={style}
      src={isHover ? delIconHover : delIcon}
      onMouseOver={() => setIsHover(true)}
      onMouseOut={() => setIsHover(false)}
      onClick={(e) => delFun(e, id)}
    />
  );
}

export default DeleteIcon;
