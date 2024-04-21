import React, { useState, useEffect } from 'react';
import axios from 'axios';

const Slider = () => {
    const [data, setData] = useState([]);
    const [currentIndex, setCurrentIndex] = useState(0);

    useEffect(() => {
        // Загрузка данных с API
        axios.get('http://localhost:8080/api/book/category/Роман')
            .then(response => setData(response.data))
            .catch(error => console.error('Ошибка при загрузке данных:', error));
        console.log(data)
    }, []);

    const handlePrev = () => {
        setCurrentIndex(prevIndex => (prevIndex > 0 ? prevIndex - 1 : 0));
    };

    const handleNext = () => {
        setCurrentIndex(prevIndex => (prevIndex < data.length - 6 ? prevIndex + 1 : prevIndex));
    };

    return (
        <div>
            <h2>Slider</h2>
            <div style={{ display: 'flex', overflow: 'hidden' }}>
                <button onClick={handlePrev}>&lt;</button>
                <div style={{ display: 'flex' }}>
                    {data.slice(currentIndex, currentIndex + 6).map(item => (
                        <div key={item.id} style={{ width: '100px', margin: '5px', border: '1px solid #ddd', padding: '10px' }}>
                            {/* Ваш код для отображения элемента */}
                            {item.name}
                            {item.author}
                        </div>
                    ))}
                </div>
                <button onClick={handleNext}>&gt;</button>
            </div>
        </div>
    );
};

export default Slider;
