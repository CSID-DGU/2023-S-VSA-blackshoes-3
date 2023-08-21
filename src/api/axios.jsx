import axios from "axios";
import { getCookie, removeCookie, setCookie } from "../Cookie";

export const BASE_URL = "https://api.roberniro-projects.xyz/";

export const Instance = axios.create({
  baseURL: BASE_URL,
  headers: {
    "Content-Type": "application/json",
  },
});

// Refresh Token 관련 변수
let isRefreshing = false;
let refreshQueue = [];

const processQueue = (error, token = null) => {
  refreshQueue.forEach((prom) => {
    // 에러가 전달된 경우 해당 프로미스의 reject 함수를 호출하여 에러를 전달
    if (error) {
      prom.reject(error);
    } else {
      // 에러가 없는 경우 해당 프로미스의 resolve 함수를 호출하여 토큰을 전달
      prom.resolve(token);
    }
  });
  refreshQueue = [];
};

Instance.interceptors.request.use(
  (config) => {
    const accessToken = localStorage.getItem("accessToken");
    const refreshToken = getCookie("refreshToken");
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
      if (!isRefreshing) {
        isRefreshing = true;
        try {
          const refreshToken = getCookie("refreshToken");
          await Instance.post("user-service/refresh", {
            refreshToken,
          }).then(async (res) => {
            const newAccessToken = res.data.payload.accessToken;
            const newRefreshToken = res.data.payload.refreshToken;
            // Access Token 재발급
            localStorage.removeItem("accessToken");
            localStorage.setItem("accessToken", newAccessToken);
            // Refresh Token 재발급
            removeCookie("refreshToken", { path: "/" });
            setCookie("refreshToken", newRefreshToken);
            // 헤더 새 토큰 반영
            // Instance.defaults.headers.common[
            //   "Authorization"
            // ] = `Bearer ${newAccessToken}`;
            originalRequest.headers[
              "Authorization"
            ] = `Bearer ${newAccessToken}`;
            // refreshToken 성공 후 대기 중인 요청 처리
            processQueue(null, newAccessToken);
            originalRequest._retry = true;
            return await Instance(originalRequest);
          });
        } catch (err) {
          console.log("리프레쉬 실패", err);
          // refreshToken 실패 후 대기 중인 요청 처리
          processQueue(err, null);
          if (
            err.response.status === 400 &&
            err.response.data.error ===
              "토큰 갱신 오류: 리프레시 토큰이 일치하지 않습니다."
          ) {
            alert(err.response.data.error);
            localStorage.removeItem("accessToken");
            removeCookie("refreshToken", { path: "/" });
            window.location.replace("/");
          }
        } finally {
          isRefreshing = false;
        }
      } else {
        // refreshToken 발급 요청 중인 경우 queue에 대기
        return new Promise((resolve, reject) => {
          refreshQueue.push({ resolve, reject });
        });
      }
    }
    return Promise.reject(error);
  }
);
