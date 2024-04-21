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