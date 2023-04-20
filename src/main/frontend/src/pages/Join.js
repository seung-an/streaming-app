import styles from "styles/page/Join.module.css";
import { useState } from "react";
import { api, authAPi } from "api/api";
import { useNavigate } from "react-router-dom";

function Join() {
  const navigate = useNavigate();
  const [inputID, setInputID] = useState("");
  const [inputPW, setInputPW] = useState("");
  const [inputEmail, setInputEmail] = useState("");

  const [idError, setIdError] = useState("");
  const [pwError, setPwError] = useState("");
  const [emailError, setEmailError] = useState("");
  const [totalError, setTotalError] = useState("");

  const changeID = (event) => {
    setInputID(event.target.value);
  };

  const changePW = (event) => {
    setInputPW(event.target.value);
  };

  const changeEmail = (event) => {
    setInputEmail(event.target.value);
  };

  //영문, 숫자
  const idRegex = /^[a-zA-Z0-9]*$/;
  //염문, 숫자, 특수문자 하나이상 사용하여 8자리 이상
  const pwRegex =
    /^(?=.*[A-Za-z])(?=.*\d)(?=.*[$@$!%*#?&])[A-Za-z\d$@$!%*#?&]{8,}$/;
  //이메일 형식
  const emailRegex =
    /^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$/i;

  const checkId = (event) => {
    if (event.target.value !== "" && !idRegex.test(event.target.value)) {
      setIdError("아이디는 영문, 숫자만 사용");
    } else {
      setIdError("");
    }
  };

  const checkPw = (event) => {
    if (event.target.value !== "" && !pwRegex.test(event.target.value)) {
      setPwError("8자 이상 대 소문자, 숫자, 특수문자 사용");
    } else {
      setPwError("");
    }
  };

  const checkEmail = (event) => {
    if (event.target.value !== "" && !emailRegex.test(event.target.value)) {
      setEmailError("올바른 이메일 형식이 아닙니다.");
    } else {
      setEmailError("");
    }
  };

  const checkTotalAndJoin = (event) => {
    if (idError === "" && pwError === "" && emailError === "") {
      setTotalError("");
      api
        .post("/member/join", {
          memberId: inputID,
          password: inputPW,
          email: inputEmail,
        })
        .then((response) => {
          navigate("/login");
        })
        .catch((error) => {
          setTotalError("알맞은 정보를 입력 후 가입해주세요");
        });
    } else {
      setTotalError("알맞은 정보를 입력 후 가입해주세요");
    }
  };

  return (
    <div className={styles.join}>
      <div className={styles.joinBox}>
        <h2>회원 가입</h2>
        <div className={styles.elmBox}>
          <input
            value={inputID}
            type={"text"}
            className={styles.joinInput}
            placeholder={"아이디"}
            onChange={changeID}
            onBlur={checkId}
          />
          {idError !== "" ? (
            <div className={styles.errorMsg}>{idError}</div>
          ) : null}
        </div>
        <div className={styles.elmBox}>
          <input
            value={inputPW}
            type={"password"}
            className={styles.joinInput}
            placeholder={"비밀번호"}
            onChange={changePW}
            onBlur={checkPw}
          />
          {pwError !== "" ? (
            <div className={styles.errorMsg}>{pwError}</div>
          ) : null}
        </div>

        <div className={styles.elmBox}>
          <input
            value={inputEmail}
            type={"text"}
            className={styles.joinInput}
            placeholder={"이메일"}
            onChange={changeEmail}
            onBlur={checkEmail}
          />
          {emailError !== "" ? (
            <div className={styles.errorMsg}>{emailError}</div>
          ) : null}
        </div>
        <div className={styles.elmBox}>
          <button className={styles.joinBtn} onClick={checkTotalAndJoin}>
            회원가입
          </button>
          {totalError !== "" ? (
            <div className={styles.errorMsg}>{totalError}</div>
          ) : null}
        </div>
      </div>
    </div>
  );
}

export default Join;
