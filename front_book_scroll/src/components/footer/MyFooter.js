import React from 'react';
import "./MyFooter.css"
import googlePlayBadge from '../../images/footer/google-play-badge.png';
import githubLogo from '../../images/footer/github-mark.png';

const MyFooter = () => {
    return (
        <div>
            <footer>
                <ul className="footerMenu">
                    <li className="firstLi">
                        <p>Ещё больше нас тут</p>
                        <a href="https://github.com/Kartishan/book-scroll"><img className="githubLogo" src={githubLogo} alt="githubLogo"/></a>
                    </li>
                    <li>
                    <p>Связь с нами</p>
                    <p>bookscrollteam@gmail.com</p>
                    </li>
                    <li className="lastLi">
                        <p>Мы в телефоне</p>
                        <a href="/"><img className="googlePlayLogo" src={googlePlayBadge} alt="googlePlayLogo"/></a>
                    </li>
                </ul>
            </footer>
        </div>
    );
};

export default MyFooter;
