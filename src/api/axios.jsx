import axios from "axios";
import { getCookie, removeCookie } from "../Cookie";

export const UploadInstance = axios.create({
  baseURL: "http://210.94.179.19:9127",
  headers: {
    "Content-Type": "application/json",
  },
});

export const SlaveInstance = axios.create({
  baseURL: "http://13.125.69.94:8011",
  headers: {
    "Content-Type": "application/json",
  },
});

export const UserInstance = axios.create({
  baseURL: "http://13.125.69.94:8001",
  headers: {
    "Content-Type": "application/json",
  },
});

let accessToken = localStorage.getItem("accessToken");
const refreshToken = getCookie("refreshToken");

UserInstance.interceptors.request.use(
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

UserInstance.interceptors.response.use(
  (response) => {
    return response;
  },
  async (error) => {
    console.log(error);
    const originalRequest = error.config;
    if (error.response.status === 401 && !originalRequest._retry) {
      originalRequest._retry = true;
      try {
        await UserInstance.post("/user-service/refresh", {
          refreshToken,
        }).then(async (res) => {
          accessToken = res.data.accessToken;
          localStorage.removeItem("accessToken");
          localStorage.setItem("accessToken", accessToken);
          UserInstance.defaults.headers.common["Authorization"] = `Bearer ${accessToken}`;
          originalRequest.headers["Authorization"] = `Bearer ${accessToken}`;
          return await UserInstance(originalRequest);
        });
      } catch (err) {
        console.log("리프레쉬 실패", err);
        // if (err.response.status === 401) {
        //   localStorage.removeItem("accessToken");
        //   removeCookie("refreshToken");
        //   window.location.replace("/");
        // }
      }
    }
    return Promise.reject(error);
  }
);
