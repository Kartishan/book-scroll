import React, { useState } from 'react';
import { useNavigate } from "react-router-dom";
import "./BookSlider.css";
import {API_URL} from "../../config";

const BookSlider = ({ books, categoryName }) => {
    const [currentIndex, setCurrentIndex] = useState(0);
    const navigate = useNavigate();

    const handlePrev = () => {
        setCurrentIndex(prevIndex => (prevIndex > 0 ? prevIndex - 1 : 0));
    };

    const handleClick = (bookId) => {
        navigate(`/book/${bookId}`);
    };

    const handleAllBooks = () => {
        navigate('/books-by-category', { state: { category: categoryName } });
    };

    const handleNext = () => {
        setCurrentIndex(prevIndex => (prevIndex < books.length - 6 ? prevIndex + 1 : prevIndex));
    };

    return (
        <div className="SlaiderContainer">
            <div className="categoryHeader">
                <h2 className="categoryName">{categoryName}</h2>
                {books.length > 9 && (
                    <p className="allButton" onClick={handleAllBooks}>Все ></p>
                )}
            </div>
            <div className="slider">
                <div className="categoryContent">
                    {currentIndex > 0 && (
                        <button className="sliderButton" onClick={handlePrev}>&lt;</button>
                    )}
                    {books.slice(currentIndex, currentIndex + 6).map((item) => (
                        <div className="content" key={item.id} onClick={() => handleClick(item.id)}>
                            <img src={`${API_URL}api/image/${item.id}`} className="categoryImage"
                                 alt={item.name}/>
                            <p className="categoryBookName">{item.name}</p>
                            <p className="categoryBookAuthor">{item.author}</p>
                        </div>
                    ))}
                    {currentIndex < books.length - 6 && (
                        <button className="sliderButton" onClick={handleNext}>&gt;</button>
                    )}
                </div>
            </div>
        </div>
    );
};

export default BookSlider;