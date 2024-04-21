import React from 'react';
import { useLocation } from 'react-router-dom';
import MyHeader from "../../components/header/MyHeader";
import MyFooter from "../../components/footer/MyFooter";
import BookList from "../../components/BookList/BookList";
import "./BooksByName.css"
const BooksByName = () => {
    const location = useLocation();
    const { books, searchQuery } = location.state ?? {};

    return (
        <div>
            <MyHeader></MyHeader>
            <div>
                {searchQuery && <p className="searchInfo">Результаты по поиску: {searchQuery}</p>}
                {books && books.length ? <BookList books={books} /> : <p>Книги не найдены.</p>}
            </div>
            <MyFooter></MyFooter>
        </div>
    );
};

export default BooksByName;