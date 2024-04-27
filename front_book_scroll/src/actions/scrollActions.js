import {API_URL} from "../config";
import axios from "axios";


export const fetchStories = async (newOffset) => {
    try {
        const response = await axios.get(`${API_URL}api/scroll/recommendations/?limit=5&offset=${newOffset}`,{
            headers: { Authorization: `Bearer ${localStorage.getItem('token')}` }
        });
        return response.data;
    } catch (error) {
        console.error('Ошибка при загрузке данных:', error);
        throw error;
    }
};

export const postStories = async (bookId, cfiRange, text) => {
    try {
        console.log(bookId)
        const scrollData = {
            bookId: bookId,
            name: text,
            cfiRange: cfiRange,
        };
        const response = await axios.post(`${API_URL}api/scroll/create`,scrollData,{
            headers: { Authorization: `Bearer ${localStorage.getItem('token')}` }
        });
        return response.data;
    } catch (error) {
        console.error('Ошибка при загрузке данных:', error);
        throw error;
    }
};

export const checkLikeStatus = async (scrollId) => {
    try {
        const response = await axios.get(`${API_URL}api/scroll/${scrollId}/like-status`,{
            headers: { Authorization: `Bearer ${localStorage.getItem('token')}` }
        });
        return response.data;
    } catch (error) {
        console.error('Ошибка при загрузке данных:', error);
        throw error;
    }
};

export const putScrollLike = async (scrollId) => {
    try {
        const response = await axios.put(`${API_URL}api/scroll/like/${scrollId}`, {}, {
            headers: { Authorization: `Bearer ${localStorage.getItem('token')}` }
        });
    } catch (error) {
        console.error('Ошибка при загрузке данных:', error);
        throw error;
    }
};