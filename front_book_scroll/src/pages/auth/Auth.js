import React, {useEffect, useState} from 'react';
import axios from 'axios'; // Импортируем axios для выполнения HTTP-запросов
import "./Auth.css"
import "../../components/FiraSans.css"
import MyHeader from "../../components/header/MyHeader";
import MyFooter from "../../components/footer/MyFooter";
import {login, registration} from "../../actions/auth";
import {useDispatch, useSelector} from "react-redux";
import {useNavigate} from "react-router-dom";

const Auth = () => {
    const navigate = useNavigate();
    const [currentPage, setCurrentPage] = useState('login');
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [email, setEmail] = useState('');
    const dispatch = useDispatch();
    const isAuth = useSelector(state => state.user.isAuth)

    const togglePage = () => {
        setCurrentPage(currentPage === 'login' ? 'registration' : 'login');
    };

    useEffect(() => {
        if (isAuth) {
            navigate("/");
        }
    }, [isAuth, navigate]);
    return (
        <div>
            <header>
                <MyHeader/>
            </header>
            <main className="main">
                <section className="loginSection">
                    {currentPage === 'login' ? (
                        <div>
                            <h1>Вход</h1>
                            <p className="loginTittle"> Введите никнейм</p>
                            <input placeholder="Никнейм" value={username} onChange={(e) => setUsername(e.target.value)}/>
                            <p className="loginTittle">Введите пароль</p>
                            <input type="password" placeholder="Пароль" value={password} onChange={(e) => setPassword(e.target.value)}/>
                            <button className="buttonStyle" onClick={() => dispatch(login(username, password))}>Войти</button>
                            <p className="registationCheckTittle">Еще не зарегистрированы?</p>
                            <button className="buttonStyle" onClick={togglePage}>Зарегистрироваться</button>
                        </div>
                    ) : (
                        <div>
                            <h1>Регистрация</h1>
                            <p className="loginTittle"> Введите никнейм</p>
                            <input placeholder="Никнейм" value={username} onChange={(e) => setUsername(e.target.value)}/>
                            <p className="loginTittle"> Введите почту</p>
                            <input type="email" placeholder="почта@example.com" value={email} onChange={(e) => setEmail(e.target.value)}/>
                            <p className="loginTittle">Введите пароль</p>
                            <input type="password" placeholder="Пароль" value={password} onChange={(e) => setPassword(e.target.value)}/>
                            <button className="buttonStyle" onClick={() => dispatch(registration(email, username, password))}>Зарегистрироваться</button>
                            <p className="registationCheckTittle">Уже зарегистрированы?</p>
                            <button className="buttonStyle" onClick={togglePage}>Войти</button>
                        </div>
                    )}
                </section>
            </main>
            <footer>
                <MyFooter/>
            </footer>
        </div>
    );
};

export default Auth;
