import { Link, useParams } from "react-router-dom";
import { useEffect, useRef, useState } from "react";
import { authApi } from "../api/api";
import CustomVideo from "../components/common/CustomVideo";
import styles from "styles/page/WatchVideo.module.css";
import * as common from "../common";
import VariableText from "../components/common/VariableText";
import DeleteIcon from "../components/common/DeleteIcon";
import iconAddPlaylist from "icons/icon-add-playlist.png";
import Modal from "components/common/Modal";
import AddPlaylistVideo from "./AddPlaylistVideo";

function WatchVideo() {
  const { id } = useParams();
  const memberCode = Number(localStorage.getItem("memberCode"));
  const memberName = localStorage.getItem("memberName");
  const memberImage = localStorage.getItem("memberImage");

  const [videoInfo, setVideoInfo] = useState(null);
  const [channelInfo, setChannelInfo] = useState(null);

  const [comments, setComments] = useState([]);
  const [newComment, setNewComment] = useState("");
  const newCommentRef = useRef();

  const [state, setState] = useState(false);

  const openModal = () => {
    setState(true);
  };

  const closeModal = () => {
    setState(false);
  };

  const getVideoInfo = async () => {
    await authApi.get("/api/video/getVideoInfo/" + id).then((response) => {
      const info = response.data.data;
      setVideoInfo(info);
      getChannelInfo(info.channel.channelCode).then();
    });
  };

  const getChannelInfo = async (code) => {
    await authApi
      .get("/api/member/getChannelInfoByCode/" + code)
      .then((response) => {
        setChannelInfo(response.data.data);
      });
  };

  const increaseViews = async () => {
    await authApi.get("/api/video/increaseViews/" + id).then((response) => {});
  };

  const getComments = async () => {
    await authApi.get("/api/comment/getComments/" + id).then((response) => {
      setComments(response.data.data);
    });
  };

  const changeNewComment = (e) => {
    setNewComment(e.target.value);
    newCommentRef.current.style.height = "auto";
    newCommentRef.current.style.height =
      newCommentRef.current.scrollHeight + "px";
  };

  const addComment = () => {
    authApi
      .post("/api/comment/addComment/" + id, { content: newComment })
      .then((response) => {
        setNewComment("");
        newCommentRef.current.style.height = "auto";
        getComments().then();
      });
  };

  const deleteComment = (e, commentId) => {
    e.stopPropagation();
    authApi.get("/api/comment/deleteComment/" + commentId).then((response) => {
      getComments().then();
    });
  };

  const saveHistory = async () => {
    await authApi.get("/api/history/saveHistory/" + id).then((response) => {});
  };

  useEffect(() => {
    getVideoInfo().then(() => {
      increaseViews().then(() => {
        getComments().then(() => {
          saveHistory().then();
        });
      });
    });
  }, []);

  return (
    <div>
      {videoInfo != null ? (
        <div>
          <div className={styles.videoBox}>
            <CustomVideo
              videoUrl={videoInfo.videoUrl}
              thumbnailUrl={videoInfo.thumbnailUrl}
            />
          </div>
          <div className={styles.titleBox}>
            <div className={styles.videoTitle}>{videoInfo.title}</div>
            <div className={styles.addPlaylist} onClick={openModal}>
              <img className={styles.iconAddPlaylist} src={iconAddPlaylist} />
              <span>저장</span>
            </div>
          </div>
          <Modal
            open={state}
            close={closeModal}
            title={"저장하기"}
            size={"tiny"}
          >
            <AddPlaylistVideo videoId={id} close={closeModal} />
          </Modal>
          {channelInfo != null ? (
            <div className={styles.channelBox}>
              <img
                className={styles.channelImage}
                src={channelInfo.channelImage}
              />
              <div className={styles.channelInfoBox}>
                <Link
                  className={styles.channelName}
                  to={"/channel/" + channelInfo.channelHandle}
                >
                  {channelInfo.channelName}
                </Link>
                <div className={styles.subscribeCount}>
                  구독자 {common.formatSub(channelInfo.channelSubscribeCount)}
                </div>
              </div>
            </div>
          ) : null}
          <div className={styles.detailBox}>
            <div className={styles.viewsAndDt}>
              조회수 {common.formatViews(videoInfo.views)} •{" "}
              {common.formattime(videoInfo.createdDt)}
            </div>
            <VariableText text={videoInfo.description} limit={"4"} />
          </div>
        </div>
      ) : null}
      <div className={styles.newCommentBox}>
        <img className={styles.channelImage} src={memberImage} />
        <div className={styles.newCommentInfo}>
          <div className={styles.commentChannelName}>{memberName}</div>
          <textarea
            rows={1}
            className={styles.inputComment}
            type={"text"}
            value={newComment}
            onChange={changeNewComment}
            placeholder={"댓글 추가"}
            ref={newCommentRef}
          />
        </div>
        <button className={styles.addCommentBtn} onClick={addComment}>
          추가
        </button>
      </div>
      <div className={styles.commentsBox}>
        {comments.map((comment) => (
          <div key={comment.commentId} className={styles.eachComment}>
            <img
              className={styles.channelImage}
              src={comment.channel.channelImage}
            />
            <div className={styles.commentInfo}>
              <Link
                className={styles.commentChannelName}
                to={"/channel/" + comment.channel.channelHandle}
              >
                {comment.channel.channelName}
              </Link>
              <div className={styles.commentDt}>
                {common.formattime(comment.createdDt)}
              </div>
              <VariableText text={comment.content} limit={"2"} />
            </div>
            {comment.channelCode === memberCode ? (
              <div>
                <DeleteIcon
                  id={comment.commentId}
                  delFun={deleteComment}
                  width={"20"}
                  height={"20"}
                />
              </div>
            ) : null}
          </div>
        ))}
      </div>
    </div>
  );
}

export default WatchVideo;
