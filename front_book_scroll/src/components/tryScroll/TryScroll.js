import React from 'react';
import "./TryScroll.css"
import "../../components/FiraSans.css"
import {useNavigate} from "react-router-dom";
const TryScroll = () => {
    const navigate = useNavigate();
    function toStoryClick(){
        navigate("/scroll")
    }
    return (
        <div>
            <section className="tryScrollMain">
                <h1>
                    BookScroll - возможность открыть книги по новому
                </h1>
                <button className="buttonStyle" onClick={toStoryClick}>
                    Попробуйте Scroll
                </button>
            </section>
        </div>
    );
};

export default TryScroll;