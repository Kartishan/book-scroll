import React, { useEffect, useState } from 'react';
import axios from 'axios';
import './SummariesPage.css'; // Предположим, что стили находятся в этом файле

const SummariesPage = ({ bookId }) => {
    const [chapters, setChapters] = useState([]);

    function toRoman(num) {
        const romanMap = {
            M: 1000,
            CM: 900,
            D: 500,
            CD: 400,
            C: 100,
            XC: 90,
            L: 50,
            XL: 40,
            X: 10,
            IX: 9,
            V: 5,
            IV: 4,
            I: 1,
        };
        let roman = '';
        for (let key in romanMap) {
            while (num >= romanMap[key]) {
                roman += key;
                num -= romanMap[key];
            }
        }
        return roman;
    }

    useEffect(() => {
        const fetchChapters = async () => {
            try {
                const response = await axios.get(`http://localhost:8080/api/chapters/${bookId}`, {
                    headers: { Authorization: `Bearer ${localStorage.getItem('token')}` },
                });
                setChapters(response.data);
            } catch (error) {
                console.error('Ошибка при получении пересказов:', error);
            }
        };

        fetchChapters();
    }, [bookId]);

    return (
        <div className="summaries-container">
            {chapters.map((chapter, index) => (
                <div key={index} className="chapter-summary">
                    <h2 className="chapter-number">Глава {toRoman(chapter.number)}</h2>
                    <p className="chapter-summary-text">{chapter.summary || 'Пересказ отсутствует.'}</p>
                </div>
            ))}
        </div>
    );
};

export default SummariesPage;