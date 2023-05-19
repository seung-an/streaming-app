import styles from "styles/page/MyVideos.module.css";
import { useEffect, useState } from "react";
import Modal from "components/common/Modal";
import UploadVideo from "pages/UploadVideo";
import UpdateVideo from "pages/UpdateVideo";
import { authApi } from "../api/api";
function MyVideos() {
  const [state, setState] = useState(false);
  const [type, setType] = useState("upload");
  const [videoId, setVideoId] = useState("");
  const [videos, setVideos] = useState([]);

  const openModal = () => {
    setState(true);
  };

  const closeModal = () => {
    setState(false);
    setType("upload");
    getMyVideos();
  };

  const openUpdateModal = (id) => {
    openModal();
    setVideoId(id);
    setType("update");
  };

  const getMyVideos = () => {
    authApi.get("/api/video/getMyVideos").then((response) => {
      setVideos(response.data.data);
    });
  };

  useEffect(() => {
    getMyVideos();
  }, []);

  console.log(type);

  return (
    <div className={styles.videoList}>
      <div className={styles.uploadBox}>
        <div className={styles.pageTitle}>동영상 관리</div>
        <button className={styles.uploadBtn} onClick={openModal}>
          동영상 업로드
        </button>
        <Modal
          open={state}
          close={closeModal}
          title={type == "upload" ? "동영상 업로드" : "동영상 수정"}
        >
          {type == "upload" ? (
            <UploadVideo update={openUpdateModal} />
          ) : (
            <UpdateVideo id={videoId} />
          )}
        </Modal>
      </div>
      <table className={styles.videoTable}>
        <colgroup>
          <col width="5%" />
          <col width="40%" />
          <col width="10%" />
          <col width="15%" />
          <col width="10%" />
          <col width="10%" />
          <col width="10%" />
        </colgroup>
        <thead>
          <tr>
            <th className={styles.checkBoxCol}>
              <input type="checkbox" id="allCheck" />
              <label
                htmlFor="allCheck"
                className={styles.checkBoxLabel}
              ></label>
            </th>
            <th>동영상</th>
            <th>공개상태</th>
            <th>날짜</th>
            <th>조회수</th>
            <th>댓글</th>
            <th>좋아요</th>
          </tr>
        </thead>
        <colgroup>
          <col width="10%" />
          <col width="40%" />
          <col width="10%" />
          <col width="10%" />
          <col width="10%" />
          <col width="10%" />
          <col width="10%" />
        </colgroup>
        <tbody>
          {videos.map((video, index) => (
            <tr className={styles.videoRow}>
              <td className={styles.checkBoxCol}>
                <input type="checkbox" id={`checkBox_video_${index}`} />
                <label
                  htmlFor={`checkBox_video_${index}`}
                  className={styles.checkBoxLabel}
                ></label>
              </td>
              <td className={styles.videoBox}>
                <video controls className={styles.video}>
                  <source type="video/mp4" src={video.url} />
                </video>
                <div className={styles.TitleAndDescription}>
                  <div className={styles.videoTitle}>{video.title}</div>
                  <div className={styles.videoDescription}>
                    {video.description}
                  </div>
                </div>
              </td>
              <td>{video.state}</td>
              <td>{video.createdDt}</td>
              <td>{video.views}</td>
              <td></td>
              <td>{video.likes}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}

export default MyVideos;
