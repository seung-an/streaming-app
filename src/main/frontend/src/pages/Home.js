import { Container, Row, Col } from "react-bootstrap";
import "bootstrap/dist/css/bootstrap.min.css";
import VideoCard from "components/common/VideoCard";
import { useEffect, useState } from "react";
import { authApi } from "../api/api";

function Home() {
  const [videos, setVideos] = useState([]);

  useEffect(() => {
    authApi.get("/api/video/getVideos").then((response) => {
      setVideos(response.data.data);
    });
  }, []);
  return (
    <div>
      <Container fluid>
        <Row xs={1} sm={1} md={2} lg={3} xxl={4} className="g-4">
          {videos.map((video) => (
            <Col key={video.videoId}>
              <VideoCard videoInfo={video} effHover={true} viewChannel={true} />
            </Col>
          ))}
        </Row>
      </Container>
    </div>
  );
}
export default Home;
