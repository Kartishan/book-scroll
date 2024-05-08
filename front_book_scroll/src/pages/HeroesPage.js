import React, { useEffect, useState } from 'react';
import axios from 'axios';
import './HeroesPage.css'; // Предположим, что стили находятся в этом файле

const HeroesPage = ({ bookId }) => {
    const [heroes, setHeroes] = useState([]);

    useEffect(() => {
        const fetchHeroes = async () => {
            try {
                const response = await axios.get(`http://localhost:8080/api/heroes/all/${bookId}`, {
                    headers: { Authorization: `Bearer ${localStorage.getItem('token')}` }
                });
                setHeroes(response.data);
            } catch (error) {
                console.error('Ошибка при получении данных о героях:', error);
            }
        };

        fetchHeroes();
    }, [bookId]);

    return (
        <div className="heroes-container">
            {heroes.map((hero, index) => (
                <div key={index} className="hero-card">
                    <h2>{hero.name}</h2>
                    <p><strong>Пол:</strong> {hero.gender}</p>
                    <p><strong>Физическое описание:</strong> {hero.physicalDescription}</p>
                    <p><strong>Образование:</strong> {hero.education}</p>
                    <p><strong>Занятие/Профессия:</strong> {hero.occupation}</p>
                    <p><strong>Характер:</strong> {hero.character}</p>
                    <p><strong>Родители:</strong> {hero.parents.map(parent => parent.name).join(', ')}</p>
                    <p><strong>Дети:</strong> {hero.children.map(child => child.name).join(', ')}</p>
                    <p><strong>Главный герой:</strong> {hero.mainCharacter ? 'Да' : 'Нет'}</p>
                    {/* Краткое описание перемещено вниз */}
                    <p className="short-description"><strong>Краткое описание:</strong> {hero.shortDescription}</p>
                </div>
            ))}
        </div>
    );
};

export default HeroesPage;