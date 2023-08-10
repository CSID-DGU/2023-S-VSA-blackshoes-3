import axios from "axios";

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

// Instance.interceptors.request.use(
//   (config) => {
//     const token = localStorage.getItem("accessToken");
//     if (token) {
//       config.headers["Authorization"] = `Bearer ${token}`;
//     }
//     return config;
//   },
//   (err) => {
//     return Promise.reject(err);
//   }
// );

// Instance.interceptors.response.use(
//   (response) => {
//     return response;
//   },
//   async (error) => {
//     const originalRequest = error.config;
//     if (error.response.status === 401 && !originalRequest._retry) {
//       originalRequest._retry = true;
//       try {
//         // Refresh Token 재발급 로직
//       } catch (err) {
//         console.log(err);
//       }
//     }
//     return Promise.reject(error);
//   }
// );
