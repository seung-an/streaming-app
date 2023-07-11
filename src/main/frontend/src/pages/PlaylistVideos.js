import styles from "styles/page/PlaylistVideos.module.css";
import { useParams } from "react-router-dom";
import { useEffect, useState } from "react";
import { authApi } from "../api/api";
import * as common from "../common";
import VideoCardHorizon from "../components/common/VideoCardHorizon";

function PlaylistVideos() {
  const { id } = useParams();
  const [playlistInfo, setPlaylistInfo] = useState(null);
  const [playlistVideos, setPlaylistVideos] = useState([]);

  console.log(id);

  const getPlaylistInfo = async () => {
    await authApi
      .get("/api/playlist/getPlaylistInfo/" + id)
      .then((response) => {
        setPlaylistInfo(response.data.data);
      });
  };

  const getPlaylistVideos = async () => {
    await authApi
      .get("/api/playlist/getPlaylistVideos/" + id)
      .then((response) => {
        setPlaylistVideos(response.data.data);
      });
  };

  useEffect(() => {
    getPlaylistInfo().then(() => {
      getPlaylistVideos().then();
    });
  }, [id]);

  return (
    <div className={styles.content}>
      {playlistInfo !== null ? (
        <div className={styles.playlistInfoBox}>
          <div className={styles.playlistImageBox}>
            <img
              className={styles.playlistImage}
              src={playlistInfo.playlistImageUrl}
            />
            <div className={styles.imageGradient}></div>
          </div>
          <div className={styles.playlistTitle}>
            {playlistInfo.playlistTitle}
          </div>
          <div className={styles.videoCountAndUpdateDt}>
            동영상 {playlistInfo.playlistVideoCount}개{"   "}
            {common.formattime(playlistInfo.playlistUpdateDt)} 업데이트됨
          </div>
        </div>
      ) : null}
      <div className={styles.playlistVideoBox}>
        {playlistVideos.map((video) => (
          <div className={styles.video}>
            <VideoCardHorizon videoInfo={video} />
          </div>
        ))}
      </div>
    </div>
  );
}

export default PlaylistVideos;
