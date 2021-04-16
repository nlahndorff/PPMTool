import axios from "axios";
import setJwtToken from "../SecurityUtils/setJwtToken";
import { GET_ERRORS, SET_CURRENT_USER } from "./types";
import jwt_decode from "jwt-decode";

export const createNewUser = (newUser, history) => async (dispatch) => {
  try {
    await axios.post("/api/users/register", newUser);
    history.push("/login");
    dispatch({
      type: GET_ERRORS,
      payload: {},
    });
  } catch (err) {
    dispatch({
      type: GET_ERRORS,
      payload: err.response.data,
    });
  }
};

export const login = (LoginRequest) => async (dispatch) => {
  //post login request
  //extract token from response
  //store token in local storage
  //set token in headers to send on subsequent requests
  //decode token in react
  //dispatch to security reducer

  try {
    const resp = await axios.post("/api/users/login", LoginRequest);
    const { token } = resp.data;
    localStorage.setItem("jwtToken", token);
    setJwtToken(token);
    const decoded = jwt_decode(token);

    dispatch({
      type: SET_CURRENT_USER,
      payload: decoded,
    });
  } catch (err) {
    dispatch({
      type: GET_ERRORS,
      payload: err.response.data,
    });
  }
};

export const logout = () => (dispatch) => {
  //clear out the stored token from local storage and state
  localStorage.removeItem("jwtToken");
  setJwtToken(false);
  dispatch({
    type: SET_CURRENT_USER,
    payload: {},
  });
};
