import React from 'react';
import "./BookList.css";
import {useNavigate} from "react-router-dom";

const BookList = ({ books }) => {
    const navigate = useNavigate();
    const handleClick = (bookId) => {
        navigate(`/book/${bookId}`);
    };
    return (
        <section className="bookByCategoryContainer">
            {books.map((book, index) => (
                <div key={index} className="bookByCategoryItem" onClick={() => handleClick(book.id)}>
                    <div className="leftBookByCategoryItem">
                        <img className="bookImageType1" src={`http://localhost:8080/api/image/${book.id}`} alt={book.name} />
                        <p>Рейтинг: {book.rating}</p>
                    </div>
                    <div className="rightBookByCategoryItem">
                        <p className="bookName">{book.name}</p>
                        <p className="bookAuthor">Автор: {book.author}</p>
                        <p className="bookDescription">{book.description}</p>
                        {book.viewTime && (
                            <p className="history-view-time">Просмотрено: {new Date(book.viewTime).toLocaleString()}</p>
                        )}
                    </div>
                </div>
            ))}
        </section>
    );
};

export default BookList;