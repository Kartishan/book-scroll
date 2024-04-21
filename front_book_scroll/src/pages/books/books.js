import React, { useState, useEffect } from 'react';
import "./books.css";
import MyHeader from "../../components/header/MyHeader";
import MyFooter from "../../components/footer/MyFooter";
import BookList from "../../components/BookList/BookList";
import {API_URL} from "../../config";

const Books = () => {
    const [categories, setCategories] = useState([]);
    const [selectedCategory, setSelectedCategory] = useState('');
    const [books, setBooks] = useState([]); // Добавлено состояние для книг

    useEffect(() => {
        fetchCategories();
        fetchBooks();
    }, []);

    useEffect(() => {
        fetchBooks(selectedCategory);
    }, [selectedCategory]);

    const fetchCategories = async () => {
        try {
            const response = await fetch(`${API_URL}api/category/categories`);
            const data = await response.json();
            setCategories(data);
        } catch (error) {
            console.error('Error fetching categories:', error);
        }
    };

    const fetchBooks = async (category = '') => {
        let url = 'http://localhost:8080/api/book/all';
        if (category) {
            // Категория есть, используем URL для фильтрации по категории
            url = `http://localhost:8080/api/book/category/${encodeURIComponent(category)}`;
        }
        try {
            const response = await fetch(url);
            const data = await response.json();
            setBooks(data.content); // Предполагаем, что `data` содержит объект `Page`. Иначе просто `setBooks(data)`.
        } catch (error) {
            console.error('Error fetching books:', error);
        }
    };

    const handleCategoryChoice = (category) => {
        setSelectedCategory(prevCategory => prevCategory === category ? '' : category);
    };

    return (
        <div>
            <MyHeader></MyHeader>
            <div className="booksCategoryChoiceContainer">
                {categories.map((category, index) => (
                    <button
                        key={index}
                        className={`categoryChoiceBtn ${category === selectedCategory ? 'selectedCategory' : ''}`}
                        onClick={() => handleCategoryChoice(category)}
                    >
                        {category}
                    </button>
                ))}
            </div>
            <BookList books={books} />
            <MyFooter></MyFooter>
        </div>
    );
};

export default Books;