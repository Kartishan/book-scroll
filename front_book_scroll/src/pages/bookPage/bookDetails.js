import React, {useEffect, useState} from "react";
import "./bookDetaileCss.css";
import {useNavigate, useParams} from "react-router-dom";
import MyHeader from "../../components/header/MyHeader";
import MyFooter from "../../components/footer/MyFooter";
import Description from "../../components/description/Description";
import {API_URL} from "../../config";
import BookComments from "../../components/bookComments/BookComments";
import Rating from "./raiting";
import {fetchBookDetails, getBookSimilar} from "../../actions/bookActions";
import BookSlider from "../../components/slider/BookSlider";


const BookDetails = () => {
    const [book, setBook] = useState(null);
    const { bookId } = useParams();
    const navigate = useNavigate();
    const [recommendations, setRecommendations] = useState([]);

    const handleReadOnline = () => {
        console.log(bookId)
        navigate(`/read/${bookId}`);
    };

    useEffect(() => {
        fetchBookDetails(bookId)
            .then(data => {
                setBook(data);
            })
            .catch(error => {
                console.error('Ошибка при загрузке книги:', error);
            });
        getBookSimilar(bookId)
            .then(data => {
                setRecommendations(data);
            })
            .catch(error => {
                console.error('Ошибка при получении рекомендаций:', error);
            });
    }, [bookId]);

    return (
        <div>
            <MyHeader></MyHeader>
            <main className="main">
                {book && (
                    <section className="bookContainer">
                        <div className="leftBookElement">
                            <img className="bookImageBig" src={`${API_URL}api/image/${bookId}`} alt={book.title}/>
                            <div>
                                <div className="bookRaitingContainer">
                                    <p id="bookRaiting">Рейтинг: </p>
                                    <Rating
                                        bookId={bookId}
                                        currentRate={book.rating}
                                    />
                                </div>
                            </div>
                        </div>

                        <div className="midlBookElement">
                            <h2>Название</h2>
                            <p >{book.name}</p>
                            <h2 className="bookAuthorHeader">Автор</h2>
                            <p >{book.author}</p>
                            <h2 className="bookAuthorHeader">Категория</h2>
                            <p>{book.categories}</p>
                            <Description description={book.description}/>
                        </div>
                        <div className="rightBookElement">
                            <button className="buttonStyle bookButton" onClick={handleReadOnline}>Читать онлайн</button>
                            <p id="pageCount">Страниц: {book.pageCount}</p>
                        </div>
                    </section>
                )}
                {book && recommendations.length > 0 && (
                    <BookSlider books={recommendations} categoryName="Похожие книги" />
                )}
                <BookComments bookId={bookId}></BookComments>
            </main>
            <MyFooter></MyFooter>
        </div>
    );
};

export default BookDetails;
