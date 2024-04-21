import React, { useEffect, useState } from 'react';
import { useLocation } from "react-router-dom";
import MyHeader from "../../../components/header/MyHeader";
import BookList from "../../../components/BookList/BookList";
import MyFooter from "../../../components/footer/MyFooter";
import axios from "axios";
import "./bookByCategory.css"
import {API_URL} from "../../../config";
const BooksByCategory = () => {
    const location = useLocation();
    const category = location.state?.category;
    const [books, setBooks] = useState([]);

    useEffect(() => {
        axios.get(`${API_URL}api/book/category/${encodeURIComponent(category)}`)
            .then(response => {
                if (response.data.content) {
                    setBooks(response.data.content);
                }
            })
            .catch(error => console.error('Ошибка при загрузке данных:', error));
    }, []);

    return (
        <div>
            <MyHeader />
            <div>
                <h1 className="categoryName">{category}</h1>
                {books && books.length ? <BookList books={books} /> : <p>Книги не найдены.</p>}
            </div>
            <MyFooter />
        </div>
    );
};

export default BooksByCategory;