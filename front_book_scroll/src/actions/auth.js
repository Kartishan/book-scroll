import axios from 'axios'
import {setUser} from "../reducers/userReducer";
import {API_URL} from "../config";

export const registration = (email, username, password) => {
    return async dispatch => {
        try {
            const response = await axios.post(`${API_URL}api/auth/signup`, {
                username,
                email,
                password
            });
            localStorage.setItem('token', response.data.access_token)
            localStorage.setItem('refreshToken',response.data.refresh_token)
            dispatch(auth());
        } catch (e) {
            console.log(e.response.data);
        }
    };
};

export const login =  (username, password) => {
    return async dispatch => {
        try {
            const response = await axios.post(`${API_URL}api/auth/signin`, {
                username,
                password
            })
            localStorage.removeItem('token')
            localStorage.removeItem('refreshToken')
            localStorage.setItem('token', response.data.access_token)
            localStorage.setItem('refreshToken',response.data.refresh_token)
            dispatch(auth());

        } catch (e) {

        }
    }
}

export const auth = () => {
    return async dispatch => {

        const token_access = localStorage.getItem('token');
        const token_refreshToken = localStorage.getItem('refreshToken');
        if (!token_access || !token_refreshToken) {
            return;
        }
        try {
            console.log(localStorage.getItem('token'))
            const response = await axios.get(`${API_URL}api/auth/authenticate-from-token`, {
                headers: { Authorization: `Bearer ${localStorage.getItem('token')}` }
            });
            dispatch(setUser(response.data));
        } catch (e) {
            try {
                const newToken = await refreshToken();
                const response = await axios.get(`${API_URL}api/auth/authenticate-from-token`, {
                    headers: {Authorization: `Bearer ${newToken}`}
                });
                dispatch(setUser(response.data));
            } catch (error) {
                localStorage.removeItem('token');
                console.error(error);
            }
        }
    };
};
const refreshToken = async () => {
    try {
        const response = await axios.get(`${API_URL}api/auth/refresh-token`, {
            headers: { Authorization: `Bearer ${localStorage.getItem('refreshToken')}` }
        });
        localStorage.setItem('token', response.data.access_token);
        return response.data.access_token;
    } catch (e) {
        localStorage.removeItem('token')
        localStorage.removeItem('refreshToken')
        console.error(e);
    }
};
export const logout = () => {
    return async dispatch => {
        try {
            await axios.get(`${API_URL}api/auth/logout`, {
                headers: { Authorization: `Bearer ${localStorage.getItem('token')}` }
            });
            dispatch({type: 'LOGOUT'})
            localStorage.removeItem('token')
            localStorage.removeItem('refreshToken')
        } catch (e) {
            console.error(e);
        }
    }
}