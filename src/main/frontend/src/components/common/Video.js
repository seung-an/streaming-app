import { Card } from "react-bootstrap";
import "bootstrap/dist/css/bootstrap.min.css";
import styles from "styles/common/Video.module.css";

function Video({ imgUrl, title }) {
  return (
    <Card className={styles.video}>
      <Card.Img
        className={`${styles.videoImg} ratio ratio-16x9`}
        variant="top"
        src={imgUrl}
      />
      <Card.Body className={styles.videoBody}>
        <div className={styles.channelImg}></div>
        <div className={styles.videoInfo}>
          <div className={styles.videoTitle}>{title}</div>
          <div className={styles.channelName}>
            <a href={"#"}>마넌</a>
            <div className={styles.views}>조회수 0회 • 1일전</div>
          </div>
        </div>
      </Card.Body>
    </Card>
  );
}

export default Video;
