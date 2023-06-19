import "bootstrap/dist/css/bootstrap.min.css";
import styles from "styles/common/VideoCardHorizon.module.css";
import * as common from "common.js";
import { Link, useNavigate } from "react-router-dom";
import { useState } from "react";
import DeleteIcon from "./DeleteIcon";

function VideoCardHorizon({ videoInfo, deleteFun }) {
  const [isHover, setIsHover] = useState(false);
  const navigate = useNavigate();
  const movePage = () => {
    navigate("/watch/" + videoInfo.videoId);
  };
  return (
    // <Link to={"/watch/" + videoInfo.videoId}>
    <div
      className={styles.videoBox}
      onMouseOver={() => {
        setIsHover(true);
      }}
      onMouseOut={() => {
        setIsHover(false);
      }}
      onClick={movePage}
    >
      <div className={styles.imgBox}>
        <img src={videoInfo.thumbnailUrl} className={styles.videoImg} />
      </div>
      <div className={styles.infoBox}>
        <div className={styles.title}>{videoInfo.title}</div>
        <div className={styles.channel}>
          <a href={"#"}>{videoInfo.channelName}</a>
        </div>
        <div className={styles.viewsAndDt}>
          조회수 {common.formatViews(videoInfo.views)} •{" "}
          {common.formattime(videoInfo.createdDt)}
        </div>
      </div>
      {deleteFun !== undefined && isHover ? (
        <div className={styles.deleteBtn}>
          <DeleteIcon
            id={videoInfo.videoId}
            delFun={deleteFun}
            width={"20"}
            height={"20"}
          />
        </div>
      ) : null}
    </div>
    // </Link>
  );
}

export default VideoCardHorizon;
