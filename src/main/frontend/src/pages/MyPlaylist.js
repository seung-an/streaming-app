import styles from "styles/page/MyPlaylist.module.css";
import Modal from "../components/common/Modal";
import { useEffect, useState } from "react";
import SetPlaylist from "./SetPlaylist";
import * as common from "../common";
import Pagination from "../components/common/Pagination";
import { authApi } from "../api/api";

function MyPlaylist() {
  const [state, setState] = useState(false);
  const [playlist, setPlaylist] = useState([]);
  const [page, setPage] = useState(1);
  const offset = (page - 1) * 10;

  const [selected, setSelected] = useState(null);

  const openModal = () => {
    setState(true);
  };

  const openUpdateModal = (id) => {
    setSelected(id);
    setState(true);
  };

  const closeModal = () => {
    setState(false);
    setSelected(null);
    getPlayList().then();
  };

  const selectAll = (e) => {
    const checkboxes = document.getElementsByName("playlist");

    checkboxes.forEach((checkbox) => {
      checkbox.checked = e.target.checked;
    });
  };

  const getPlayList = async () => {
    await authApi.get("/api/playlist/getPlaylist").then((response) => {
      setPlaylist(response.data.data);
    });
  };

  useEffect(() => {
    getPlayList().then();
  }, []);

  return (
    <div>
      <div className={styles.headerBox}>
        <div className={styles.pageTitle}>재생목록</div>
        <button className={styles.addBtn} onClick={openModal}>
          재생목록 생성
        </button>
        <Modal
          open={state}
          close={closeModal}
          title={selected === null ? "재생목록 생성" : "재생목록 수정"}
          size={"mini"}
        >
          <SetPlaylist id={selected} afterFn={closeModal} />
        </Modal>
      </div>
      <table className={styles.table}>
        <colgroup>
          <col width="5%" />
          <col width="50%" />
          <col width="15%" />
          <col width="15%" />
          <col width="15%" />
        </colgroup>
        <thead>
          <tr>
            <th className={styles.checkBoxCol}>
              <input type="checkbox" id="allCheck" onClick={selectAll} />
              <label
                htmlFor="allCheck"
                className={styles.checkBoxLabel}
              ></label>
            </th>
            <th>재생목록</th>
            <th>공개상태</th>
            <th>업데이트 날짜</th>
            <th>동영상 개수</th>
          </tr>
        </thead>
        <tbody>
          {playlist.slice(offset, offset + 10).map((item, index) => (
            <tr className={styles.row} key={item.playlistId}>
              <td className={styles.checkBoxCol}>
                <input
                  type="checkbox"
                  id={`checkBox_video_${index}`}
                  name={"playlist"}
                />
                <label
                  htmlFor={`checkBox_playlist_${index}`}
                  className={styles.checkBoxLabel}
                ></label>
              </td>
              <td
                className={styles.imageBox}
                onClick={() => {
                  openUpdateModal(item.playlistId);
                }}
              >
                {item.playlistImageUrl != null &&
                item.playlistImageUrl != "" ? (
                  <img className={styles.image} src={item.playlistImageUrl} />
                ) : (
                  <div className={styles.noImage}>동영상 없음</div>
                )}
                <div className={styles.title}>{item.playlistTitle}</div>
              </td>
              <td>{common.getVideoState(item.playlistState)}</td>
              <td>{item.playlistUpdateDt}</td>
              <td>{item.playlistVideoCount}</td>
            </tr>
          ))}
        </tbody>
      </table>
      <footer>
        <Pagination
          total={playlist.length}
          limit={10}
          page={page}
          setPage={setPage}
        />
      </footer>
    </div>
  );
}

export default MyPlaylist;
