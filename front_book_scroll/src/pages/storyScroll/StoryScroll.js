import React, {useEffect, useState} from 'react';
import MyHeader from '../../components/header/MyHeader';
import './StoryScroll.css'
import Story from "../../components/story/Story";
import unLike from "../../images/scroll/like.png";
import likeFull from  "../../images/scroll/likefull.png"
import comments from "../../images/scroll/comment.png";
import share from "../../images/scroll/export.png";
import goToBook from "../../images/scroll/goToBook.png";
import {useNavigate} from "react-router-dom";
import {checkLikeStatus, fetchStories, putScrollLike} from "../../actions/scrollActions";


const StoryScroll = () => {
    const [stories, setStories] = useState([]);
    const [currentStoryIndex, setCurrentStoryIndex] = useState(0);
    const [isLoading, setIsLoading] = useState(true);
    const [animation, setAnimation] = useState('');
    const navigate = useNavigate();
    const [offset, setOffset] = useState(0);
    const [likeStatus, setLikeStatus] = useState({});

    useEffect(() => {
        setIsLoading(true);
        fetchStories(offset)
            .then(newStories => {
                setStories(prevStories => [...prevStories, ...newStories]);
                setIsLoading(false);
                return newStories;
            })
            .then(newStories => {
                newStories.forEach(story => {
                    checkLikeStatus(story.id)
                        .then(isLiked => {
                            setLikeStatus(prevLikeStatus => ({
                                ...prevLikeStatus,
                                [story.id]: isLiked
                            }));
                        })
                        .catch(error => {
                            console.error('Ошибка при проверке лайка:', error);
                        });
                });
            })
            .catch(error => {
                console.error('Ошибка при загрузке данных:', error);
                setIsLoading(false);
            });
    }, [offset]);

    const handleLikeClick = async () => {
        if (stories.length > 0) {
            const scrollId = stories[currentStoryIndex].id;
            try {
                const response = await putScrollLike(scrollId);
                setLikeStatus(prevLikeStatus => ({
                    ...prevLikeStatus,
                    [scrollId]: !prevLikeStatus[scrollId]
                }));
            } catch (error) {
                console.error('Ошибка при отправке лайка:', error);
            }
        }
    };

    const changeStory = (newIndex, direction) => {
        setAnimation(direction);
        setTimeout(() => {
            if (direction === 'next' && newIndex === stories.length) {
                setOffset(prevOffset => prevOffset + stories.length);
                newIndex = 0;
            }
            setCurrentStoryIndex(newIndex);
            setAnimation('');
        }, 500);
    };

    const fetchNextStory = () => {
        const newIndex = (currentStoryIndex + 1) % stories.length;
        changeStory(newIndex, 'next');
        if (newIndex === stories.length - 1) {
            setOffset(prevOffset => prevOffset + stories.length);
        }
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
                {currentStoryIndex !== 0 && (<button onClick={fetchPreviousStory} className="story-button">▲</button>)}
                {stories.length > 0 && (
                    <button className="story-button" onClick={handleLikeClick}>
                        <img src={likeStatus[stories[currentStoryIndex].id] ? likeFull : unLike} alt="лайк"/>
                    </button>
                )}
                <button className="story-button" onClick={() => handleGoToBook(stories[currentStoryIndex].bookId)}>
                    <img src={goToBook} alt="Перейти к книге"/>
                </button>
                <button className="story-button"><img src={comments} alt="Комментарии"/></button>
                <button className="story-button"><img src={share} alt="Поделиться"/></button>
                {currentStoryIndex !== stories.length - 1 && (
                    <button onClick={fetchNextStory} className="story-button">▼</button>)}
            </div>
        </div>
    </div>)
};

export default StoryScroll;