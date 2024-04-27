import {API_URL} from "../config";
import axios from "axios";


export const fetchBookComments = async (bookId) => {
    try {
        const response = await axios.get(`${API_URL}api/comments/book/${bookId}`);
        return response.data;
    } catch (error) {
        console.error('Ошибка при загрузке комментариев:', error);
        throw error;
    }
};

export const postBookComment = async (bookId, commentText, parentId = null) => {
    const token = localStorage.getItem('token');
    const commentData = {
        bookId: bookId,
        tittle: commentText,
        parentCommentId: parentId
    };

    try {
        const response = await axios.post(`${API_URL}api/comments/add`, commentData,
            {
                headers: {Authorization: `Bearer ${token}`}
            });
        return response.data;
    } catch (error) {
        console.error(error);
        throw error;
    }
};