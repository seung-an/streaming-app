import styles from "styles/page/Login.module.css";
import { api, authAPi } from "api/api";
import { useState } from "react";
import { useNavigate } from "react-router-dom";

function Login() {
  const navigate = useNavigate();
  const [inputID, setInputID] = useState("");
  const [inputPW, setInputPW] = useState("");
  const [errorMsg, setErrorMsg] = useState("");

  const changeID = (event) => {
    setInputID(event.target.value);
  };

  const changePW = (event) => {
    setInputPW(event.target.value);
  };

  const changeErrorMsg = (msg) => {
    setErrorMsg(msg);
  };

  const checkLogin = () => {
    changeErrorMsg("테스트 에러");

    //영문, 숫자
    const idRegex = /^[a-zA-Z0-9]*$/;
    //염문, 숫자, 특수문자 하나이상 사용하여 8자리 이상
    const pwRegex =
      /^(?=.*[A-Za-z])(?=.*\d)(?=.*[$@$!%*#?&])[A-Za-z\d$@$!%*#?&]{8,}$/;

    if (!idRegex.test(inputID) || !pwRegex.test(inputPW)) {
      changeErrorMsg("아이디 또는 비밀번호를 잘못 입력 하였습니다.");
    } else {
      changeErrorMsg("");
    }

    api()
      .post("/member/getToken", { memberId: inputID, password: inputPW })
      .then((response) => {
        localStorage.setItem("accessToken", response.data.accessToken);
        changeErrorMsg("");
        navigate("/");
      })
      .catch((error) => {
        changeErrorMsg("아이디 또는 비밀번호를 잘못 입력 하였습니다.");
      });
  };

  return (
    <div className={styles.login}>
      <div className={styles.loginBox}>
        <h2>로그인</h2>
        <div className={styles.elmBox}>
          <input
            value={inputID}
            type={"text"}
            className={styles.loginInput}
            placeholder={"아이디"}
            onChange={changeID}
          />
        </div>
        <div className={styles.elmBox}>
          <input
            value={inputPW}
            type={"password"}
            className={styles.loginInput}
            placeholder={"비밀번호"}
            onChange={changePW}
          />
        </div>
        <div className={styles.elmBox}>
          <button className={styles.loginBtn} onClick={checkLogin}>
            로그인
          </button>
        </div>
        {errorMsg !== "" ? (
          <div className={styles.errorMsg}>{errorMsg}</div>
        ) : null}
        <div className={styles.elmBox}>
          <a className={styles.signUp} href={"#"}>
            회원가입
          </a>
          <a className={styles.findPw} href={"#"}>
            비밀번호 찾기
          </a>
        </div>
      </div>
    </div>
  );
}
export default Login;
