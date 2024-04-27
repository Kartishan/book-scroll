import React, { useEffect, useState, useCallback } from 'react';
import { useLocation } from "react-router-dom";
import MyHeader from "../../../components/header/MyHeader";
import BookList from "../../../components/BookList/BookList";
import MyFooter from "../../../components/footer/MyFooter";
import axios from "axios";
import "./bookByCategory.css"
import { API_URL } from "../../../config";

const BooksByCategory = () => {
    const location = useLocation();
    const category = location.state?.category;
    const [books, setBooks] = useState([]);
    const [currentPage, setCurrentPage] = useState(0);
    const [totalPages, setTotalPages] = useState(0);

    const fetchBooks = useCallback((page) => {
        axios.get(`${API_URL}api/book/category/${encodeURIComponent(category)}?page=${page}`)
            .then(response => {
                if (response.data.content) {
                    setBooks(prevBooks => [...prevBooks, ...response.data.content]);
                    setTotalPages(response.data.totalPages);
                    setCurrentPage(page);
                }
            })
            .catch(error => console.error('Ошибка при загрузке данных:', error));
    }, [category]);

    useEffect(() => {
        fetchBooks(0); // Загружаем первую страницу
    }, [fetchBooks]);

    const handleLoadMore = () => {
        const nextPage = currentPage + 1;
        if (nextPage < totalPages) {
            fetchBooks(nextPage);
        }
    };

    return (
        <div>
            <MyHeader />
            <div>
                <h1 className="categoryName">{category}</h1>
                {books && books.length ? <BookList books={books}/> : <p>Книги не найдены.</p>}
                {currentPage + 1 < totalPages && (
                    <button className="buttonStyle bookByCategoryContainerButton" onClick={handleLoadMore}>
                        Загрузить ещё
                    </button>
                )}

            </div>
            <MyFooter/>
        </div>
    );
};

export default BooksByCategory;