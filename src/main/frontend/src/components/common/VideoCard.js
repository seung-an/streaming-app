import { Card } from "react-bootstrap";
import "bootstrap/dist/css/bootstrap.min.css";
import styles from "styles/common/VideoCard.module.css";
import * as common from "common.js";
import { useNavigate } from "react-router-dom";

function VideoCard({ videoInfo, effHover, viewChannel }) {
  const navigate = useNavigate();

  const goWatch = () => {
    navigate("/watch/" + videoInfo.videoId);
  };

  const goChannel = (e) => {
    e.stopPropagation();
    navigate("/channel/" + videoInfo.channelHandle);
  };

  return (
    <Card
      className={effHover === true ? styles.video : styles.noHvVideo}
      onClick={goWatch}
    >
      <Card.Header className={"ratio ratio-16x9"}>
        <img src={videoInfo.thumbnailUrl} className={styles.videoImg} />
      </Card.Header>
      <Card.Body className={styles.videoBody}>
        {viewChannel === true ? (
          <img className={styles.channelImg} src={videoInfo.channelImage} />
        ) : null}
        <div
          className={
            viewChannel === true ? styles.videoInfo : styles.noChVideoInfo
          }
        >
          <div className={styles.videoTitle}>{videoInfo.title}</div>
          <div>
            {viewChannel === true ? (
              <div onClick={goChannel} className={styles.channelName}>
                {videoInfo.channelName}
              </div>
            ) : null}
            <div className={styles.viewsAndDt}>
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
