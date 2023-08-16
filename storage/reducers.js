import {
  SET_ALL,
  SET_TOKEN,
  SET_USER_ID,
  SET_ACCESS_TOKEN,
  SET_REFRESH_TOKEN,
  SET_TAG,
  SET_LIKE,
  ADD_LIKE,
  DELETE_LIKE,
  DELETE_USER_ID,
  FIRST_EMAIL,
} from './actions';

const initialState = {
  USER: null,
  ACCESS: null,
  REFRESH: null,
  TAG: null,
  LIKE: null,
  FIRST_EMAIL: null,
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
    case SET_TOKEN:
      return {
        ...state,
        ACCESS: action.payload.accessToken,
        REFRESH: action.payload.refreshToken,
      };
    case SET_USER_ID:
      return {...state, USER: action.payload};
    case SET_ACCESS_TOKEN:
      return {...state, ACCESS: action.payload};
    case SET_REFRESH_TOKEN:
      return {...state, REFRESH: action.payload};
    case SET_TAG:
      return {...state, TAG: action.payload};
    case SET_LIKE:
      return {...state, LIKE: action.payload};
    case ADD_LIKE:
      return {...state, LIKE: [...state.LIKE, action.payload]};
    case DELETE_LIKE:
      return {
        ...state,
        LIKE: state.LIKE.filter(item => item !== action.payload),
      };
    case DELETE_USER_ID:
      return {
        USER: null,
        ACCESS: null,
        REFRESH: null,
        TAG: null,
        LIKE: null,
        FIRST_EMAIL: null,
      };
    case FIRST_EMAIL:
      return {...state, FIRST_EMAIL: action.payload};
    default:
      return state;
  }
}

export default rootReducer;
