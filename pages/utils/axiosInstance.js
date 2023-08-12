import axios from 'axios';
import {SERVER_IP} from '../../config';
import AsyncStorage from '@react-native-async-storage/async-storage';
import Store from '../../storage/store';
import {setToken} from '../../storage/actions';

const axiosInstance = axios.create({
  baseURL: `${SERVER_IP}`,
  headers: {
    'Content-Type': 'application/json',
  },
});

axiosInstance.interceptors.response.use(
  response => {
    return response;
  },
  async error => {
    const originalRequest = error.config;
    console.log(error.response);
    if (
      // error.response.status === 401 &&
      error.response.data.error === 'Invalid token' &&
      !originalRequest._retry
    ) {
      originalRequest._retry = true;

      const refreshToken = await AsyncStorage.getItem('refreshToken');
      const response = await axiosInstance.post('8001//user-service/refresh', {
        refreshToken: refreshToken,
      });

      const newAccessToken = response.data.payload.accessToken;
      await AsyncStorage.setItem('accessToken', newAccessToken);
      await AsyncStorage.setItem(
        'refreshToken',
        response.data.payload.refreshToken,
      );
      Store.dispatch(setToken(response.data.payload));
      originalRequest.headers.Authorization = 'Bearer ' + newAccessToken;

      return axiosInstance(originalRequest);
    }

    return Promise.reject(error);
  },
);

export default axiosInstance;
