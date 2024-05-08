import React from 'react';
import "./Home.css";
import TryScroll from "../../components/tryScroll/TryScroll";
import MyHeader from "../../components/header/MyHeader";
import MyFooter from "../../components/footer/MyFooter";
import CategoriesSliders from "../../components/сategoriesSliders/CategoriesSliders";
import AudioPlayerComponent from "../../components/audioplayer/AudioPlayerComponent";

const Home = () => {
    return (
        <div className="wrapper">
            <header>
                <MyHeader/>
            </header>
            <main>
                <TryScroll/>
                <CategoriesSliders />
            </main>
            <footer>
                <MyFooter/>
            </footer>
        </div>
    );
};

export default Home;