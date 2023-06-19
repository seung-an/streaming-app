import { Card } from "react-bootstrap";
import "bootstrap/dist/css/bootstrap.min.css";
import styles from "styles/common/VideoCard.module.css";
import * as common from "common.js";
import { Link } from "react-router-dom";

function VideoCard({ videoInfo }) {
  return (
    <Card className={styles.video} as={Link} to={"/watch/" + videoInfo.videoId}>
      <Card.Header className={"ratio ratio-16x9"}>
        <img src={videoInfo.thumbnailUrl} className={styles.videoImg} />
      </Card.Header>
      <Card.Body className={styles.videoBody}>
        <img className={styles.channelImg} src={videoInfo.channelImage}></img>
        <div className={styles.videoInfo}>
          <div className={styles.videoTitle}>{videoInfo.title}</div>
          <div className={styles.channelName}>
            <a href={"#"}>{videoInfo.channelName}</a>
            <div className={styles.views}>
              조회수 {common.formatViews(videoInfo.views)} •{" "}
              {common.formattime(videoInfo.createdDt)}
            </div>
          </div>
        </div>
      </Card.Body>
    </Card>
  );
}

export default VideoCard;
