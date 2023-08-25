import axios from 'axios';
import {SERVER_IP} from '../config';
import Store from '../storage/store';
import {setToken} from '../storage/actions';

const axiosInstance = axios.create({
  baseURL: `${SERVER_IP}`,
  headers: {
    'Content-Type': 'application/json',
    Authorization: `Bearer ${Store.getState().ACCESS}`,
  },
});

let isRequestInProgress = false;
const requestQueue = [];

const executeNextRequest = () => {
  if (requestQueue.length === 0) {
    isRequestInProgress = false;
    return;
  }
  const nextRequest = requestQueue.shift();
  nextRequest();
};

axiosInstance.interceptors.request.use(config => {
  config.headers.Authorization = `Bearer ${Store.getState().ACCESS}`;
  console.log('엑세스 토큰 : ', Store.getState().ACCESS);
  return new Promise((resolve, reject) => {
    const requestCall = () => {
      resolve(config);
    };

    if (isRequestInProgress) {
      requestQueue.push(requestCall);
    } else {
      isRequestInProgress = true;
      requestCall();
    }
  });
});

axiosInstance.interceptors.response.use(
  function (response) {
    executeNextRequest();
    return response;
  },
  async error => {
    console.log('에러 : ', error);
    const originalRequest = error.config;
    console.log('originalRequest : ', originalRequest);
    if (error.response.status === 401 && !originalRequest._retry) {
      originalRequest._retry = true;

      const refreshToken = Store.getState().REFRESH;
      console.log('리프레쉬 토큰 : ', refreshToken);

      const response = await axios.post(`${SERVER_IP}user-service/refresh`, {
        refreshToken: refreshToken,
      });

      console.log('리프레쉬 토큰 재발급 요청 응답 : ', response);

      const newAccessToken = response.data.payload.accessToken;
      Store.dispatch(setToken(response.data.payload));
      originalRequest.headers.Authorization = 'Bearer ' + newAccessToken;

      executeNextRequest();

      return axiosInstance(originalRequest);
    }

    executeNextRequest();
    return Promise.reject(error);
  },
);

export default axiosInstance;
