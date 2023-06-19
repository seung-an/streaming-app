import "bootstrap/dist/css/bootstrap.min.css";
import styles from "styles/common/VideoCardHorizon.module.css";
import * as common from "common.js";
import { Link } from "react-router-dom";

function VideoCardHorizon({ videoInfo }) {
  return (
    <Link to={"/watch/" + videoInfo.videoId}>
      <div className={styles.videoBox}>
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
      </div>
    </Link>
  );
}

export default VideoCardHorizon;
