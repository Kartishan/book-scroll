import React, { useState } from 'react';
import axios from 'axios';
import { Search } from 'lucide-react';
import "../header/MyHeader.css";
import "../../components/FiraSans.css";
import { useNavigate } from 'react-router-dom';

const BookSearch = ({ onBookFound }) => {
    const navigate = useNavigate();
    const [partialName, setPartialName] = useState('');

    const handleSearch = async (e) => {
        e.preventDefault(); // Предотвратить перезагрузку страницы
        try {
            const response = await axios.get(`http://localhost:8080/api/book/search/partialName/${partialName}`);
            const books = response.data.content;

            if (books && books.length === 1) {
                onBookFound(books[0]);
            } else if (books && books.length > 1) {
                navigate('/booksByName', { state: { books, searchQuery: partialName } });
            } else {
                console.log('Книга не найдена');
            }
        } catch (error) {
            console.error('Error searching for books:', error);
        }
    };

    return (
        <form onSubmit={handleSearch} className="searchForm">
            <input
                className="searchInput"
                type="text"
                placeholder="Введите название"
                value={partialName}
                onChange={(e) => setPartialName(e.target.value)}
            />
            <button type="submit" className="searchButton">
                <Search className="searchIcon" />
            </button>
        </form>
    );
};

export default BookSearch;