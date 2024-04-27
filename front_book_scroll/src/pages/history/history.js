import React, {useEffect, useState} from 'react';
import MyHeader from "../../components/header/MyHeader";
import MyFooter from "../../components/footer/MyFooter";
import {fetchBookHistory} from "../../actions/bookActions";
import BookList from "../../components/BookList/BookList"; // Убедитесь, что путь к файлу CSS корректный

const History = () => {
    const [history, setHistory] = useState([]);

    useEffect(() => {
        fetchBookHistory()
            .then(data => {
                setHistory(data);
            })
            .catch(error => {
                console.error('Ошибка при загрузке истории:', error);
            });
    }, [setHistory]);

    return (
        <div>
            <MyHeader/>
                <BookList books={history}/>
            <MyFooter/>
        </div>
    );
};

export default History;