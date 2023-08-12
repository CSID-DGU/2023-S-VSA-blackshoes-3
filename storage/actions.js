export const SET_ALL = 'SET_ALL';

export const SET_TOKEN = 'SET_TOKEN';
export const SET_USER_ID = 'SET_USER_ID';
export const SET_ACCESS_TOKEN = 'SET_ACCESS_TOKEN';
export const SET_REFRESH_TOKEN = 'SET_REFRESH_TOKEN';
export const DELETE_USER_ID = 'DELETE_USER_ID';

export function setAll(payload) {
  return {type: SET_ALL, payload: payload};
}

export function setToken(payload) {
  return {type: SET_TOKEN, payload: payload};
}

export function setUserId(payload) {
  return {type: SET_USER_ID, payload: payload};
}

export function setAccessToken(payload) {
  return {type: SET_ACCESS_TOKEN, payload: payload};
}

export function setRefreshToken(payload) {
  return {type: SET_REFRESH_TOKEN, payload: payload};
}

export function deleteUserId() {
  return {type: DELETE_USER_ID};
}
