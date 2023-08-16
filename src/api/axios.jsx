import axios from "axios";
import { getCookie, removeCookie } from "../Cookie";

export const BASE_URL = "https://api.roberniro-projects.xyz/";

export const Instance = axios.create({
  baseURL: BASE_URL,
  headers: {
    "Content-Type": "application/json",
  },
});

let accessToken = localStorage.getItem("accessToken");
let refreshToken = getCookie("refreshToken");

Instance.interceptors.request.use(
  (config) => {
    if (accessToken && refreshToken) {
      config.headers["Authorization"] = `Bearer ${accessToken}`;
    }
    return config;
  },
  (err) => {
    return Promise.reject(err);
  }
);

Instance.interceptors.response.use(
  (response) => {
    return response;
  },
  async (error) => {
    const originalRequest = error.config;
    if (error.response.status === 401 && !originalRequest._retry) {
      originalRequest._retry = true;
      try {
        await Instance.post("user-service/refresh", {
          refreshToken,
        }).then(async (res) => {
          console.log(res);
          localStorage.removeItem("accessToken");
          accessToken = res.data.payload.accessToken;
          localStorage.setItem("accessToken", accessToken);
          Instance.defaults.headers.common["Authorization"] = `Bearer ${accessToken}`;
          originalRequest.headers["Authorization"] = `Bearer ${accessToken}`;
          return await Instance(originalRequest);
        });
      } catch (err) {
        console.log("리프레쉬 실패", err);
        if (
          err.response.status === 400 &&
          err.response.data.error === "토큰 갱신 오류: 리프레시 토큰이 일치하지 않습니다."
        ) {
          alert(err.response.data.error);
          localStorage.removeItem("accessToken");
          removeCookie("refreshToken", { path: "/" });
          window.location.replace("/");
        }
      }
    }
    return Promise.reject(error);
  }
);
