import axios from "axios";

const api = axios.create({
  headers: {
    "Content-Type": `application/json;charset=UTF-8`,
  },
});

const authApi = axios.create({
  headers: {
    "Content-Type": `application/json;charset=UTF-8`,
  },
});

//요청 인터셉터
authApi.interceptors.request.use(
  function (config) {
    const token = localStorage.getItem("accessToken");

    if (!token) {
      return config;
    }

    if (config.headers && token) {
      config.headers.authorization = `Bearer ${token}`;
      return config;
    }
    // Do something before request is sent
  },
  function (error) {
    // Do something with request error
    return Promise.reject(error);
  }
);

// 응답 인터셉터
authApi.interceptors.response.use(
  function (response) {
    //정상응답 -> 리턴
    return response;
  },
  async (error) => {
    //에러
    const config = error.config;
    const status = error.response.status;
    if (status === 401) {
      if (error.response.data.code === 1005) {
        const originalRequest = config;
        // token refresh 요청
        await axios
          .post(
            `/member/refreshToken`, // token refresh api
            {}
          )
          .then((res) => {
            // 새로운 토큰 저장
            originalRequest.headers.authorization = `Bearer ${res.data.accessToken}`;
            localStorage.setItem("accessToken", res.data.accessToken);
          })
          .catch((error) => {
            window.location.href = "/login";
          });
        // 요청 실패했던 요청 새로운 accessToken으로 재요청
        return axios(originalRequest);
      }
    }

    return Promise.reject(error);
  }
);

export { api, authApi };
