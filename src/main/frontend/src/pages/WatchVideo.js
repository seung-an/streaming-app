import { useParams } from "react-router-dom";
import { useCallback, useEffect, useRef, useState } from "react";
import { authApi } from "../api/api";
import CustomVideo from "../components/common/CustomVideo";
import styles from "styles/page/WatchVideo.module.css";
import * as common from "../common";
import { getConvertTime } from "../common";
import VariableText from "../components/common/VariableText";

function WatchVideo() {
  const { id } = useParams();
  const [title, setTitle] = useState("");
  const [description, setDescription] = useState("");
  const [state, setState] = useState("");
  const [views, setViews] = useState("");
  const [createdDt, setCreatedDt] = useState("");
  const [thumbnailUrl, setThumbnailUrl] = useState("");
  const [videoUrl, setVideoUrl] = useState("");

  const [comments, setComments] = useState([]);
  const [newComment, setNewComment] = useState("");
  const newCommentRef = useRef();

  // const [more, setMore] = useState(false);
  // const [isOverflow, setIsOverflow] = useState(null);
  // const descriptionRef = useRef();

  const getInfo = async () => {
    await authApi.get("/api/video/getVideoInfo/" + id).then((response) => {
      const info = response.data.data;
      setTitle(info.title);
      setDescription(info.description);
      setState(info.state);
      setViews(info.views);
      setCreatedDt(info.createdDt);
      setThumbnailUrl(info.thumbnailUrl);
      setVideoUrl(info.videoUrl);
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

  // const changeMore = () => {
  //   setMore((curr) => !curr);
  // };

  useEffect(() => {
    getInfo().then(() => {
      increaseViews().then(() => {
        getComments().then();
      });
    });
  }, []);

  // const mounted = useRef(false);
  // useEffect(() => {
  //   if (!mounted.current) {
  //     mounted.current = true;
  //   } else if (isOverflow == null) {
  //     setIsOverflow(
  //       descriptionRef.current.offsetHeight <
  //         descriptionRef.current.scrollHeight
  //     );
  //   }
  // });

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
        getComments().then();
      });
  };

  return (
    <div>
      <div className={styles.videoBox}>
        <CustomVideo videoUrl={videoUrl} thumbnailUrl={thumbnailUrl} />
      </div>
      <div className={styles.videoTitle}>{title}</div>
      <div className={styles.detailBox}>
        <div className={styles.viewsAndDt}>
          조회수 {common.formatViews(views)} • {common.formattime(createdDt)}
        </div>
        {description !== "" ? (
          <VariableText text={description} limit={"4"} />
        ) : null}
      </div>
      <div className={styles.newCommentBox}>
        <textarea
          rows={1}
          className={styles.inputComment}
          type={"text"}
          value={newComment}
          onChange={changeNewComment}
          placeholder={"댓글 추가"}
          ref={newCommentRef}
        />
        <button className={styles.addCommentBtn} onClick={addComment}>
          추가
        </button>
      </div>
      <div className={styles.commentsBox}>
        {comments.map((comment) => (
          <div key={comment.commentId} className={styles.eachComment}>
            <img
              className={styles.commentMemberImage}
              src={comment.memberImage}
            />
            <div className={styles.commentInfo}>
              <div className={styles.commentMember}>{comment.memberName}</div>
              <div className={styles.commentDt}>
                {common.formattime(comment.createdDt)}
              </div>
              <VariableText text={comment.content} limit={"2"} />
            </div>
          </div>
        ))}
      </div>
    </div>
  );
}

export default WatchVideo;