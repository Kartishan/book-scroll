import React, { useEffect, useState } from 'react';
import {fetchBooksByCategory, fetchBooksByRating, fetchBooksByView} from "../../actions/bookActions";
import BookSlider from "../../components/slider/BookSlider";

const CategoriesSliders = () => {
    const [popularBooks, setPopularBooks] = useState([]);
    const [ratingBooks, setRatingBooks] = useState([]);
    const [romanceBooks, setRomanceBooks] = useState([]);
    const [detectiveBooks, setDetectiveBooks] = useState([]);

    useEffect(() => {
        const fetchCategoryBooks = async (categoryPath, setCategoryBooks) => {
            try {
                const booksData = await fetchBooksByCategory(categoryPath);
                setCategoryBooks(booksData);
            } catch (error) {
                console.error(`Ошибка при загрузке по адресу ${categoryPath}:`, error);
            }
        };
        fetchCategoryBooks('Роман', setRomanceBooks);
        fetchCategoryBooks('Детектив', setDetectiveBooks);
    }, []);

    useEffect(() => {
        const fetchPopularAndRatingBooks = async () => {
            try {
                const popularData = await fetchBooksByView();
                setPopularBooks(popularData);
                const ratingData = await fetchBooksByRating();
                setRatingBooks(ratingData);
            } catch (error) {
                console.error(`Ошибка при загрузке данных:`, error);
            }
        };

        fetchPopularAndRatingBooks();
    }, []);

    return (
        <>
            <BookSlider books={popularBooks} categoryName="Популярные" />
            <BookSlider books={ratingBooks} categoryName="Рейтинговые" />
            <BookSlider books={romanceBooks} categoryName="Роман" />
            <BookSlider books={detectiveBooks} categoryName="Детектив" />
        </>
    );
};

export default CategoriesSliders;