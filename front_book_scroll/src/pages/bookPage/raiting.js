import React, { useState } from 'react';
import './Rating.css';
import {updateBookRating} from "../../actions/bookActions";

const Rating = ({ bookId, currentRate }) => {
    const [rating, setRating] = useState(currentRate);
    const [hoverRating, setHoverRating] = useState(undefined);

    const handleClick = async (rate) => {
        setRating(rate);
        console.log(rate)
        try {
            const data = await updateBookRating(bookId, rate);
            console.log('Результат обновления рейтинга:', data);
        } catch (error) {
            console.error('Ошибка при отправке оценки:', error);
        }
    };

    const handleMouseOver = (rate) => {
        setHoverRating(rate);
    };

    const handleMouseLeave = () => {
        setHoverRating(undefined);
    };

    return (
        <div className="rating" onMouseLeave={handleMouseLeave}>
            {[1, 2, 3, 4, 5].map((star) => (
                <span
                    key={star}
                    onClick={() => handleClick(star)}
                    onMouseOver={() => handleMouseOver(star)}
                    className={star <= (hoverRating || rating) ? 'filled' : 'empty'}
                >
                    {star <= (hoverRating || rating) ? '★' : '☆'}
                </span>
            ))}
        </div>
    );
};

export default Rating;