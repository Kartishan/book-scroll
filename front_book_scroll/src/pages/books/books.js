import React, { useState, useEffect } from 'react';
import "./books.css";
import MyHeader from "../../components/header/MyHeader";
import MyFooter from "../../components/footer/MyFooter";
import BookList from "../../components/BookList/BookList";
import {API_URL} from "../../config";

const Books = () => {
    const MAX_VISIBLE_CATEGORIES = 12; // Максимальное количество категорий, показываемых по умолчанию

    const [categories, setCategories] = useState([]);
    const [selectedCategory, setSelectedCategory] = useState('');
    const [books, setBooks] = useState([]);
    const [isExpanded, setIsExpanded] = useState(false); // состояние для отслеживания, раскрыт ли список полностью

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
        let url = `${API_URL}api/book/all`;
        if (category) {
            url = `${API_URL}api/book/category/${encodeURIComponent(category)}`;
        }
        try {
            const response = await fetch(url);
            const data = await response.json();
            setBooks(data.content);
        } catch (error) {
            console.error('Error fetching books:', error);
        }
    };

    const handleCategoryChoice = (category) => {
        setSelectedCategory(prevCategory => prevCategory === category ? '' : category);
    };

    const visibleCategories = isExpanded ? categories : categories.slice(0, MAX_VISIBLE_CATEGORIES);

    return (
        <div>
            <MyHeader/>
            <div className="booksCategoryChoiceContainer">
                {visibleCategories.map((category, index) => (
                    <button
                        key={index}
                        className={`categoryChoiceBtn ${category === selectedCategory ? 'selectedCategory' : ''}`}
                        onClick={() => handleCategoryChoice(category)}
                    >
                        {category}
                    </button>
                ))}
            </div>
            {categories.length > MAX_VISIBLE_CATEGORIES && (
                <button
                    onClick={() => setIsExpanded(!isExpanded)}
                    className={`expandCollapseButton ${isExpanded ? 'expanded' : ''}`}
                >
                    {isExpanded ? 'Свернуть' : 'Раскрыть все'}
                </button>
            )}
            <BookList books={books}/>
            <MyFooter/>
        </div>
    );
};

export default Books;