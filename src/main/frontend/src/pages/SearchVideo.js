import { useSearchParams } from "react-router-dom";
import { useEffect, useState } from "react";
import { authApi } from "../api/api";
import VideoCardHorizon from "../components/common/VideoCardHorizon";
import styles from "styles/page/SearchVideo.module.css";

function SearchVideo() {
  const [searchParams, setSearchParams] = useSearchParams();
  const [videos, setVideos] = useState([]);

  useEffect(() => {
    authApi
      .get(
        "/api/video/getVideos?searchQuery=" + searchParams.get("searchQuery")
      )
      .then((response) => {
        setVideos(response.data.data);
      });
  }, []);

  return (
    <div>
      {videos.map((video) => (
        <div className={styles.video}>
          <VideoCardHorizon videoInfo={video} />
        </div>
      ))}
    </div>
  );
}

export default SearchVideo;
