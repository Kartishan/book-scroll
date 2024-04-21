import React, {useState, useEffect} from 'react';
import axios from 'axios';
import MyHeader from '../../components/header/MyHeader';
import './StoryScroll.css'
import Story from "../../components/story/Story";
import unLike from "../../images/scroll/like.png";
import fullLike from "../../images/scroll/likefull.png";
import comments from "../../images/scroll/comment.png";
import share from  "../../images/scroll/export.png";
import goToBook from "../../images/scroll/goToBook.png";
import {useNavigate} from "react-router-dom";
import {API_URL} from "../../config";


const StoryScroll = () => {
    const [stories, setStories] = useState([]);
    const [currentStoryIndex, setCurrentStoryIndex] = useState(0);
    const [isLoading, setIsLoading] = useState(true);
    const [animation, setAnimation] = useState('');
    const navigate = useNavigate();
    const [offset, setOffset] = useState(0);

    useEffect(() => {
        fetchStories(offset);
    }, [offset]);

    const fetchStories = async (newOffset) => {
        setIsLoading(true);
        try {
            const response = await axios.get(`${API_URL}api/scroll/recommendations/86a17475-f4b3-4a35-a613-41efa0638ec2?limit=10&offset=${newOffset}`);
            setStories(prevStories => [...prevStories, ...response.data]);
            setIsLoading(false);
        } catch (error) {
            console.error('Ошибка при загрузке данных:', error);
            setIsLoading(false);
        }
    };

    const changeStory = (newIndex, direction) => {
        setAnimation(direction);
        setTimeout(() => {
            if (direction === 'next' && newIndex === stories.length) {
                setOffset(prevOffset => prevOffset + stories.length);
                newIndex = 0; // Сбрасываем индекс, чтобы истории отображались с начала новой загруженной порции
            }
            setCurrentStoryIndex(newIndex);
            setAnimation('');
        }, 500); // Это значение должно совпадать с продолжительностью вашей CSS анимации
    };

    const fetchNextStory = () => {
        const newIndex = (currentStoryIndex + 1) % stories.length;
        changeStory(newIndex, 'next');
    };

    const fetchPreviousStory = () => {
        const newIndex = (currentStoryIndex - 1 + stories.length) % stories.length;
        changeStory(newIndex, 'prev');
    };

    const handleGoToBook = (bookId) => {
        navigate(`/book/${bookId}`);
    };
    return (<div>
        <MyHeader/>
        <div className={`storyScrollContainer ${isLoading ? "hidden" : ""}`}>
            <div className={`storyContentContainer ${animation}`}>
                {stories.length > 0 && <Story data={stories[currentStoryIndex]}/>}
            </div>

            <div className="storyButtonsContainer">
                {currentStoryIndex !== 0 &&
                    (<button onClick={fetchPreviousStory} className="story-button">▲</button>)}
                <button className="story-button"><img src={unLike} alt="лайк" /></button>
                <button className="story-button" onClick={() => handleGoToBook(stories[currentStoryIndex].bookId)}>
                    <img src={goToBook} alt="Перейти к книге"/>
                </button>
                <button className="story-button"><img src={comments} alt="Комментарии"/></button>
                <button className="story-button"><img src={share} alt="Поделиться"/></button>
                {currentStoryIndex !== stories.length - 1 &&
                    (<button onClick={fetchNextStory} className="story-button">▼</button>)}
            </div>
        </div>
    </div>)
};

export default StoryScroll;