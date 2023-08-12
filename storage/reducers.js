import {
  SET_ALL,
  SET_USER_ID,
  SET_ACCESS_TOKEN,
  SET_REFRESH_TOKEN,
  DELETE_USER_ID,
} from './actions';

const initialState = {
  USER: null,
  ACCESS: null,
  REFRESH: null,
};
function rootReducer(state = initialState, action) {
  switch (action.type) {
    case SET_ALL:
      return {
        ...state,
        USER: action.payload.userId,
        ACCESS: action.payload.accessToken,
        REFRESH: action.payload.refreshToken,
      };
    case SET_USER_ID:
      return {...state, USER: action.payload};
    case SET_ACCESS_TOKEN:
      return {...state, ACCESS: action.payload};
    case SET_REFRESH_TOKEN:
      return {...state, REFRESH: action.payload};
    case DELETE_USER_ID:
      return {USER: null, ACCESS: null, REFRESH: null};
    default:
      return state;
  }
}

export default rootReducer;
