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
  console.log('들어가네?');
  const nextRequest = requestQueue.shift();
  nextRequest();
};

axiosInstance.interceptors.request.use(config => {
  config.headers.Authorization = `Bearer ${Store.getState().ACCESS}`;
  console.log('hi : ', Store.getState().ACCESS);
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
    isRequestInProgress = false;
    executeNextRequest();
    return response;
  },
  async error => {
    console.log('에러');
    const originalRequest = error.config;

    if (error.response.status === 401 && !originalRequest._retry) {
      originalRequest._retry = true;

      const refreshToken = Store.getState().REFRESH;
      const response = await axiosInstance.post('/user-service/refresh', {
        refreshToken: refreshToken,
      });

      const newAccessToken = response.data.payload.accessToken;
      Store.dispatch(setToken(response.data.payload));
      originalRequest.headers.Authorization = 'Bearer ' + newAccessToken;
      const retriedResponse = await axiosInstance(originalRequest);

      isRequestInProgress = false;
      executeNextRequest();
      return retriedResponse;
    }

    isRequestInProgress = false;
    executeNextRequest();
    return Promise.reject(error);
  },
);

// const executeNextRequest = () => {
//   if (requestQueue.length === 0) {
//     isRequestInProgress = false;
//     return;
//   }
//   console.log('들어가네?');
//   const nextRequest = requestQueue.shift();

//   nextRequest();
//   isRequestInProgress = false;
//   executeNextRequest();
// };

// axiosInstance.interceptors.request.use(config => {
//   config.headers.Authorization = `Bearer ${Store.getState().ACCESS}`;
//   console.log('hi : ', Store.getState().ACCESS);
//   return new Promise((resolve, reject) => {
//     const requestCall = () => {
//       resolve(config);
//     };

//     if (isRequestInProgress) {
//       requestQueue.push(requestCall);
//     } else {
//       isRequestInProgress = true;
//       requestCall();
//     }
//   });
// });

// axiosInstance.interceptors.response.use(
//   function (response) {
//     isRequestInProgress = false;
//     executeNextRequest();
//     return response;
//   },
//   async error => {
//     console.log('에러');
//     const originalRequest = error.config;

//     if (error.response.status === 401 && !originalRequest._retry) {
//       originalRequest._retry = true;
//       isRequestInProgress = true;

//       const refreshToken = Store.getState().REFRESH;
//       const response = await axiosInstance.post('/user-service/refresh', {
//         refreshToken: refreshToken,
//       });

//       const newAccessToken = response.data.payload.accessToken;
//       Store.dispatch(setToken(response.data.payload));
//       originalRequest.headers.Authorization = 'Bearer ' + newAccessToken;
//       const retriedResponse = await axiosInstance(originalRequest);

//       isRequestInProgress = false;
//       executeNextRequest();

//       return retriedResponse;
//     }

//     isRequestInProgress = false;
//     executeNextRequest();
//     return Promise.reject(error);
//   },
// );
// axiosInstance.interceptors.response.use(
//   function (response) {
//     isRequestInProgress = false;
//     executeNextRequest();

//     console.log('응답', response);
//     return response;
//   },
//   async error => {
//     console.log('에러');
//     console.log(error.response.status);
//     const originalRequest = error.config;
//     console.log('originalRequest : ', originalRequest);

//     if (error.response.status === 401 && !originalRequest._retry) {
//       originalRequest._retry = true;
//       const refreshToken = Store.getState().REFRESH;

//       console.log('love', refreshToken);
//       const response = await axiosInstance.post('/user-service/refresh', {
//         refreshToken: refreshToken,
//       });
//       console.log('after');
//       const newAccessToken = response.data.payload.accessToken;
//       console.log('/n바뀐 요청 응답 : ', response);

//       console.log('응답 토큰 : ', newAccessToken);
//       console.log('응답 리프레쉬 토큰 : ', response.data.payload.refreshToken);

//       Store.dispatch(setToken(response.data.payload));
//       originalRequest.headers.Authorization = 'Bearer ' + newAccessToken;
//       console.log('/nsecondoriginal Request', originalRequest);

//       const retriedResponse = await axiosInstance(originalRequest);

//       originalRequest._retry = false;

//       return retriedResponse;
//     }

//     originalRequest._retry = false;
//     isRequestInProgress = false;
//     executeNextRequest();

//     return Promise.reject(error);
//   },
// );

export default axiosInstance;
