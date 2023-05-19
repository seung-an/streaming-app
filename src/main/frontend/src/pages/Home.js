import { Container, Row, Col } from "react-bootstrap";
import "bootstrap/dist/css/bootstrap.min.css";
import Video from "components/common/Video";

function tempVideo() {
  const arr = [];

  for (let i = 0; i < 20; i++) {
    arr.push(
      <Col key={i}>
        <Video
          title={"테스트 제목"}
          imgUrl={
            "https://helpx.adobe.com/content/dam/help/ko/illustrator/how-to/icon-design/jcr_content/main-pars/image_1765517237/icon-design-main.jpg.img.jpg"
          }
        />
      </Col>
    );
  }

  return arr;
}

function Home() {
  return (
    <div>
      <Container fluid>
        <Row xs={1} sm={1} md={2} lg={3} xxl={4} className="g-4">
          {tempVideo()}
        </Row>
      </Container>
    </div>
  );
}
export default Home;
