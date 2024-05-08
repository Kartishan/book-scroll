import {API_URL} from "../config";
import axios from "axios";


export const fetchBooksByCategory = async (categoryName) => {
    try {
        const response = await axios.get(`${API_URL}api/book/category/${categoryName}`);
        return response.data.content;
    } catch (error) {
        console.error('Ошибка при загрузке данных:', error);
        throw error;
    }
};

export const fetchBooksByRating = async () => {
    try {
        const response = await axios.get(`${API_URL}api/book/top-rated`);
        return response.data.content;
    } catch (error) {
        console.error('Ошибка при загрузке данных:', error);
        throw error;
    }
};
export const fetchBooksByView = async () => {
    try {
        const response = await axios.get(`${API_URL}api/book/top-viewed`);
        return response.data.content;
    } catch (error) {
        console.error('Ошибка при загрузке данных:', error);
        throw error;
    }
};
export const fetchBookHistory = async () => {
    const token = localStorage.getItem('token');
    try {
        const response = await axios.get(`${API_URL}api/book/history`, {
            headers: {Authorization: `Bearer ${token}`}
        });
        return response.data;
    } catch (error) {
        console.error('Ошибка при загрузке истории:', error);
        throw error;
    }
};

export const fetchBookDetails = async (bookId) => {
    try {
        const token = localStorage.getItem('token');
        const config = token ? { headers: { Authorization: `Bearer ${token}` } } : {};
        const response = await axios.get(`${API_URL}api/book/${bookId}`, config);
        return response.data;
    } catch (error) {
        console.error('Ошибка при загрузке данных книги:', error);
        throw error;
    }
};

export const updateBookRating = async (bookId, rating) => {
    const token = localStorage.getItem('token');
    const ratingData = {
        bookId: bookId,
        rating: rating
    };
    try {
        const response = await axios.post(`${API_URL}api/preference/update`, ratingData,
            {
            headers: {Authorization: `Bearer ${token}`}
        });
        return response.data;
    } catch (error) {
        console.error('Ошибка при обновлении рейтинга книги:', error);
        throw error;
    }
};

export const createBookmark = async (bookId, cfiRange, text, comment = "") => {
    const token = localStorage.getItem('token');
    const bookmarkData = {
        cfiRange: cfiRange,
        text: text,
        comment: comment
    };
    try {
        const response = await axios.post(`${API_URL}api/new/bookmarks/${bookId}`, bookmarkData, {
            headers: {Authorization: `Bearer ${token}`}
        });
        return response.data;
    } catch (error) {
        console.error('Ошибка при создании закладки:', error);
        throw error;
    }
};

export const getBookmarks = async (bookId) => {
    const token = localStorage.getItem('token');
    try {
        const response = await axios.get(`${API_URL}api/new/bookmarks/${bookId}`, {
            headers: {Authorization: `Bearer ${token}`}
        });
        return response.data;
    } catch (error) {
        console.error('Ошибка при получении закладок:', error);
        throw error;
    }
};

export const deleteBookmark = async (bookId, bookmarkId) => {
    const token = localStorage.getItem('token');
    try {
        await axios.delete(`${API_URL}api/new/bookmarks/${bookId}/${bookmarkId}`, {
            headers: {Authorization: `Bearer ${token}`}
        });
    } catch (error) {
        console.error('Ошибка при удалении закладки:', error);
        throw error;
    }
};


export const getBookSimilar = async (bookId) => {
    try {
        const token = localStorage.getItem('token');
        const response = await axios.get(`${API_URL}api/recommendation/forBook`, {
            params: {
                bookId: bookId,
                n: 9
            }
        });
        return response.data;
    } catch (error) {
        console.error('Ошибка при получении рекомендаций для книги:', error);
        throw error;
    }
};


export const shareBookmark = async (bookId) => {
    const token = localStorage.getItem('token');
    try {
        await axios.post(`${API_URL}api/new/bookmarks/share/${bookId}`, {
            headers: {Authorization: `Bearer ${token}`}
        });
    } catch (error) {
        console.error('Ошибка при удалении закладки:', error);
        throw error;
    }
};

export const importBookmark = async (tokenBookMarks) => {
    const token = localStorage.getItem('token');
    try {
        await axios.post(`${API_URL}api/new/bookmarks/import/${tokenBookMarks}`, {
            headers: {Authorization: `Bearer ${token}`}
        });
    } catch (error) {
        console.error('Ошибка при удалении закладки:', error);
        throw error;
    }
};