import { useOutletContext } from "react-router-dom";
import { useEffect, useState } from "react";
import { authApi } from "../api/api";
import { Col, Row } from "react-bootstrap";
import PlaylistCard from "../components/common/PlaylistCard";
import styles from "styles/page/ChannelPlaylist.module.css";

function ChannelPlaylist() {
  const { handle } = useOutletContext();
  const [playlist, setPlaylist] = useState([]);

  useEffect(() => {
    authApi
      .get("/api/playlist/getPlaylistByHandle/" + handle)
      .then((response) => {
        setPlaylist(response.data.data);
      });
  }, [handle]);

  return (
    <div className={styles.contentsBox}>
      <Row xs={1} sm={1} md={2} lg={3} xxl={4} className="g-4">
        {playlist.map((p) => (
          <Col key={p.playlistId}>
            <PlaylistCard info={p} />
          </Col>
        ))}
      </Row>
    </div>
  );
}

export default ChannelPlaylist;
