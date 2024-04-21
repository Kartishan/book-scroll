import React from 'react';
import './CardComponent.css'; // Убедитесь, что создали файл стилей для карточки

const CardComponent = ({ title, username }) => {
    return (
        <div className="card">
            <h2>{title}</h2>
            <p>@{username}</p>
        </div>
    );
};

export default CardComponent;