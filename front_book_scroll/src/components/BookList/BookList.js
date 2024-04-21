import React from 'react';
import "./BookList.css";

const BookList = ({ books }) => {
    return (
        <section className="bookByCategoryContainer">
            {books.map((book, index) => (
                <div key={index} className="bookByCategoryItem" onClick={() => window.location.href = `book.html?bookId=${book.id}`}>
                    <div className="leftBookByCategoryItem">
                        <img className="bookImageType1" src={`http://localhost:8080/api/image/${book.id}`} alt={book.name} />
                        <p>Рейтинг: {book.rating}</p>
                    </div>
                    <div className="rightBookByCategoryItem">
                        <p className="bookName">{book.name}</p>
                        <p className="bookAuthor">Автор: {book.author}</p>
                        <p className="bookDescription">{book.description}</p>
                    </div>
                </div>
            ))}
        </section>
    );
};

export default BookList;