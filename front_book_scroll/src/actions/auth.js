import axios from 'axios';
import { setUser } from "../reducers/userReducer";
import { API_URL } from "../config";

export const registration = (email, username, password) => {
    return async dispatch => {
        try {
            const response = await axios.post(`${API_URL}api/auth/signup`, {
                username,
                email,
                password
            });
            localStorage.setItem('token', response.data.access_token);
            dispatch(auth());
        } catch (e) {
            console.log(e.response?.data);
        }
    };
};

export const login = (username, password) => {
    return async dispatch => {
        try {
            const response = await axios.post(`${API_URL}api/auth/signin`, {
                username,
                password
            });
            localStorage.setItem('token', response.data.access_token);
            dispatch(auth());
        } catch (e) {
            console.error(e.response?.data);
        }
    };
};

export const auth = () => {
    return async dispatch => {
        const token_access = localStorage.getItem('token');
        if (token_access === null || token_access === '') {
            return;
        }
        try {
            const response = await axios.get(`${API_URL}api/auth/authenticate-from-token`, {
                headers: { Authorization: `Bearer ${token_access}` }
            });
            dispatch(setUser(response.data));
        } catch (e) {
            console.error(e);
            await refreshToken(dispatch);
        }
    };
};

const refreshToken = async (dispatch) => {
    try {
        const response = await axios.post(`${API_URL}api/auth/refresh-token`);
        if (response.data.access_token === undefined){
            logout()
            return;
        }
        else {
            localStorage.setItem('token', response.data.access_token);
            dispatch(auth());
        }
    } catch (e) {
        logout()
    }
};

export const logout = () => {
    return async dispatch => {
        try {
            dispatch({ type: 'LOGOUT' });
            localStorage.removeItem('token');
            await axios.get(`${API_URL}api/auth/logout`);
            return;
        } catch (e) {
            console.error(e);
        }
    };
};