import React, { useState } from 'react';
import axios from 'axios';
import "../header/MyHeader.css"; // Импорт стилей
import "../../components/FiraSans.css";
import { useNavigate } from 'react-router-dom'; // Импорт необходим для навигации

const BookSearch = ({ onBookFound }) => {
    const navigate = useNavigate(); // Добавляем хук useNavigate
    const [partialName, setPartialName] = useState('');

    const handleSearch = async () => {
        try {
            // Получаем книги с сервера
            const response = await axios.get(`http://localhost:8080/api/book/search/partialName/${partialName}`);
            const books = response.data.content; // Инициализируем переменную books данными с сервера

            if (books && books.length === 1) {
                onBookFound(books[0]); // Если найдена ровно одна книга
            } else if (books && books.length > 1) {
                // Перенаправляем на страницу со списком книг, если результатов больше одного
                navigate('/booksByName', { state: { books, searchQuery: partialName } });
            } else {
                console.log('Книга не найдена');
            }
        } catch (error) {
            console.error('Error searching for books:', error);
        }
    };

    return (
        <div>
            <input
                className="searchInput"
                type="text"
                placeholder="Введите название книги"
                value={partialName}
                onChange={(e) => setPartialName(e.target.value)}
            />
            <button className="searchButton" onClick={handleSearch}>Поиск</button>
        </div>
    );
};

export default BookSearch;