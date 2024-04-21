import React, { useState, useEffect } from "react";
import "./bookDetaileCss.css";
import {useNavigate, useParams} from "react-router-dom";
import axios from "axios";
import MyHeader from "../../components/header/MyHeader";
import MyFooter from "../../components/footer/MyFooter";
import Description from "../../components/description/Description";
import {API_URL} from "../../config";


const BookDetails = () => {
    const [book, setBook] = useState(null);
    const { bookId } = useParams();
    const navigate = useNavigate();

    const handleReadOnline = () => {
        navigate(`/read/${"6620100c7c65a6004744c23a"}`);
    };

    useEffect(() => {
        const getBook = async () => {
            try {
                const response = await axios.get(`${API_URL}api/book/${bookId}`);
                console.log(response.data);
                setBook(response.data);
            } catch (error) {
                console.error(error);
            }
        };

        getBook();
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
                                    <p id="bookRaiting">Рейтинг: {book.rating}</p>
                                    <img id="starImage" src="./images/Star.png" alt=""/>
                                </div>
                                <p id="pageCount">Страниц: {book.pageCount}</p>
                            </div>
                        </div>

                        <div className="midlBookElement">
                            <h2>Название</h2>
                            <p id="bookName">{book.name}</p>
                            <h2 className="bookAuthorHeader">Автор</h2>
                            <p id="bookAuthor">{book.author}</p>
                            <h2 className="bookDescriptionHeader">Категория</h2>
                            <p id="bookCategory">{book.category}</p>
                            <Description description={book.description}/>
                            {/*<p id="bookDescription" className="bookDescription">{book.description}</p>*/}
                            {/*<p id="fullBookDescriptionBtn" className="fullBookDescriptionBtn">Раскрыть описание</p>*/}
                        </div>
                        <div className="rightBookElement">
                            <h2 id="bookPrice">{book.price}</h2>
                            {/*<button className="buttonStyle bookButton">Купить книгу</button>*/}
                            {/*<button className="buttonStyle bookButton">Забронировать</button>*/}
                            <button className="buttonStyle bookButton" onClick={handleReadOnline}>Читать онлайн</button>
                            {/*<p id="bookSell">Кол-во книг для покупки: {book.sellCount}</p>*/}
                            {/*<p id="bookRent">Кол-во книг для брони: {book.rentCount}</p>*/}
                        </div>
                    </section>
                )}
                <section className="reviewsContainer">
                </section>
            </main>
            <MyFooter></MyFooter>
        </div>
    );
};

export default BookDetails;
