import axios from "axios";

const api = () => {
  const Axios = axios.create({
    baseURL: "http://localhost:3000",
    headers: {
      "Content-Type": `application/json;charset=UTF-8`,
    },
  });
  return Axios;
};

const authApi = () => {
  const token = localStorage.getItem("accessToken");
  const authAxios = axios.create({
    baseURL: "http://localhost:3000",
    headers: {
      Authorization: "Bearer " + token,
      "Content-Type": `application/json;charset=UTF-8`,
    },
  });

  return authAxios;
};

export { api, authApi };
