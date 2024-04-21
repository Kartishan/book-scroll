import React, { useState, useEffect } from 'react';
import axios from "axios";
import './History.css';
import MyHeader from "../../components/header/MyHeader";
import MyFooter from "../../components/footer/MyFooter"; // Убедитесь, что путь к файлу CSS корректный

const History = () => {
    const [history, setHistory] = useState([]);
    const userId = "c1489191-4ef1-4e98-9bc6-b0ed8404e4e9";

    useEffect(() => {
        axios.get(`http://localhost:8080/api/book/history/${userId}`)
            .then(response => {
                setHistory(response.data);
            })
            .catch(error => {
                console.error('Ошибка при загрузке истории:', error);
            });
    }, [userId]);

    return (
        <div>
            <MyHeader/>
            <section className="history-content">
                {history.map((item, index) => (
                    <div key={index} className="history-item"
                         onClick={() => window.location.href = `book.html?bookId=${item.book.id}`}>
                        <img className="history-item-img" src={`http://localhost:8080/api/image/${item.book.id}`}
                             alt={item.book.name}/>
                        <div className="history-item-info">
                            <p className="bookName">{item.book.name}</p>
                            <p className="bookAuthor">Автор: {item.book.author}</p>
                            <p className="bookDescription">{item.book.description}</p>
                            <p className="history-view-time">Просмотрено: {new Date(item.viewTime).toLocaleString()}</p>
                        </div>
                    </div>
                ))}
            </section>
            <MyFooter/>
        </div>
    );
};

export default History;