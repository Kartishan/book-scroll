import React from 'react';
import "./MyHeader.css"
import "../../components/FiraSans.css"
import BookSearch from "../bookSearch/BookSearch";
import {useDispatch, useSelector} from "react-redux";
import {useNavigate} from "react-router-dom";

const MyHeader = () => {
    const navigate = useNavigate();
    const isAuth = useSelector(state => state.user.isAuth)
    const dispatch = useDispatch();
    const username = useSelector(state => state.user.currentUser?.username || '');
    const handleBookFound = (book) => {
        console.log("tuuuts книга");
        console.log(book);
        navigate(`/book/${book.id}`)
    };
    return (
        <div>
            <header className="header">
                <nav className="navbar">
                    <div className="container">
                        <div className="navbar-wrap">
                            <a href="../../App.js" className="navbar-logo">BookScroll</a>
                            <ul className="navbar-menu">
                                <li><a href="/books">Книги</a></li>
                                <li><a href="/audio">Аудио</a></li>
                                <li><a href="/scroll">СториСкролл</a></li>
                                {/*<li><input className="searchInput" id="searchInput" placeholder="Поиск" required/></li>*/}
                                {/*<button className="searchButton" type="button">*/}
                                {/*    /!* Сюда надо будет добавить лупу или тип того:)" *!/*/}
                                {/*    Поиск*/}
                                {/*</button>*/}
                                <li><BookSearch onBookFound={handleBookFound}/></li>
                            </ul>
                        </div>
                        <div className="navbar-wrap">
                            <ul className="navbar-menu">
                                {/*Добавить перед История isAuth <3*/}
                                {/*<li><a className="navbarLastItem" href="/history">История</a></li>*/}
                                {/*<li><a className="navbarLastItem" href="/auth">Войти</a></li>*/}
                                {isAuth && <li><button className="navbarLastItem navbarButton" onClick={() => navigate("/history")}>История</button> </li>}
                                {isAuth && <li><button className="navbarLastItem navbarButton" onClick={() => navigate("/profile")}>{username}</button></li>}

                                {!isAuth && <li><button className="navbarLastItem navbarButton" onClick={() => navigate("/auth")}>Войти</button></li>}
                            </ul>
                        </div>
                    </div>
                </nav>
            </header>
        </div>
    );
};

export default MyHeader;