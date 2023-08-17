export const SET_ALL = 'SET_ALL';

export const SET_TOKEN = 'SET_TOKEN';
export const SET_USER_ID = 'SET_USER_ID';
export const SET_NICK_NAME = 'SET_NICK_NAME';
export const SET_ACCESS_TOKEN = 'SET_ACCESS_TOKEN';
export const SET_REFRESH_TOKEN = 'SET_REFRESH_TOKEN';
export const SET_TAG = 'SET_TAG';
export const DELETE_USER_ID = 'DELETE_USER_ID';
export const FIRST_EMAIL = 'FIRST_EMAIL';
export const SET_LIKE = 'SET_LIKE';
export const ADD_LIKE = 'ADD_LIKE';
export const DELETE_LIKE = 'DELETE_LIKE';

export function setAll(payload) {
  return {type: SET_ALL, payload: payload};
}

export function setToken(payload) {
  return {type: SET_TOKEN, payload: payload};
}

export function setUserId(payload) {
  return {type: SET_USER_ID, payload: payload};
}

export function setNickName(payload) {
  return {type: SET_NICK_NAME, payload: payload};
}

export function setAccessToken(payload) {
  return {type: SET_ACCESS_TOKEN, payload: payload};
}

export function setRefreshToken(payload) {
  return {type: SET_REFRESH_TOKEN, payload: payload};
}

export function setTag(payload) {
  return {type: SET_TAG, payload: payload};
}

export function setLike(payload) {
  return {type: SET_LIKE, payload: payload};
}

export function addLike(payload) {
  return {type: ADD_LIKE, payload: payload};
}

export function deleteLike(payload) {
  return {type: DELETE_LIKE, payload: payload};
}

export function deleteUserId() {
  return {type: DELETE_USER_ID};
}

export function firstEmail(payload) {
  return {type: FIRST_EMAIL, payload: payload};
}
