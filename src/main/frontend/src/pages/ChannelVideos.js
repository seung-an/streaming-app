import { Col, Row } from "react-bootstrap";
import VideoCard from "../components/common/VideoCard";
import { useEffect, useState } from "react";
import { authApi } from "../api/api";
import styles from "styles/page/ChannelVideos.module.css";
import { useOutletContext } from "react-router-dom";

function ChannelVideos() {
  const [videos, setVideos] = useState([]);
  const { handle } = useOutletContext();

  useEffect(() => {
    authApi.get("/api/video/getChannelVideos/" + handle).then((response) => {
      setVideos(response.data.data);
    });
  }, [handle]);

  return (
    <div className={styles.contentsBox}>
      <Row xs={1} sm={1} md={2} lg={3} xxl={4} className="g-4">
        {videos.map((video) => (
          <Col key={video.videoId}>
            <VideoCard videoInfo={video} />
          </Col>
        ))}
      </Row>
    </div>
  );
}

export default ChannelVideos;
