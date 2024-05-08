import React, { useEffect, useRef, useState } from 'react';
import axios from 'axios';
import { Rewind, FastForward, CirclePlay, CirclePause , SkipBack, SkipForward, MoreVertical, BookOpen } from 'lucide-react';
import './AudioPlayerCss.css';

const AudioPlayerComponent = ({ bookId: initialBookId }) => {
    const [bookId, setBookId] = useState(initialBookId);
    const [chapters, setChapters] = useState([]);
    const [currentChapterIndex, setCurrentChapterIndex] = useState(0);
    const [isPlaying, setIsPlaying] = useState(false);
    const [progress, setProgress] = useState(0);
    const [playbackRate, setPlaybackRate] = useState(1);
    const [currentTime, setCurrentTime] = useState(0);
    const audioRef = useRef(null);
    const [progressBarStyle, setProgressBarStyle] = useState({});
    const [showPlaybackRateOptions, setShowPlaybackRateOptions] = useState(false);
    const [showChaptersMenu, setShowChaptersMenu] = useState(false);
    const [isReady, setIsReady] = useState(false);
    const [playbackPosition, setPlaybackPosition] = useState(0);

    const fetchChapters = async () => {
        let currentBookId = bookId;
        if (!currentBookId) {
            try {
                const { data: lastPlaybackData } = await axios.get('http://localhost:8080/api/playback/last', {
                    headers: {
                        Authorization: `Bearer ${localStorage.getItem('token')}`
                    }
                });
                console.log(lastPlaybackData);
                console.log(lastPlaybackData.lastPlaybackPosition);
                currentBookId = lastPlaybackData.book.id;
                setCurrentChapterIndex(lastPlaybackData.chapter.number-1)
                setBookId(currentBookId);
                setPlaybackPosition(lastPlaybackData.lastPlaybackPosition);
            } catch (error) {
                console.error('Ошибка при получении последней позиции воспроизведения', error);
            }
        }
        if (currentBookId) {
            try {
                const { data } = await axios.get(`http://localhost:8080/api/chapters/${currentBookId}`);
                console.log(data);
                setChapters(data);
                if (audioRef.current) {
                    console.log("da");
                    audioRef.current.currentTime = playbackPosition; // Устанавливаем время воспроизведения
                }
            } catch (error) {
                console.error('Ошибка при загрузке глав', error);
            }
        }
    };
    const selectChapter = (index) => {
        setCurrentChapterIndex(index);
        setShowChaptersMenu(false);
        if (audioRef.current) {
            audioRef.current.playbackRate = playbackRate; // Сбросить скорость воспроизведения
        }
    };

    const setPlaybackTime = () => {
        if (audioRef.current && !isReady) {
            audioRef.current.currentTime = playbackPosition;
            setIsReady(true);
        }
    };

    const updatePlaybackPosition = async () => {
        if (currentChapter && bookId) {
            try {
                await axios.post('http://localhost:8080/api/playback', {
                    bookId: bookId,
                    chapterNumber: currentChapter.number,
                    lastPlaybackPosition: audioRef.current.currentTime
                }, {
                    headers: {
                        Authorization: `Bearer ${localStorage.getItem('token')}`
                    }
                });
            } catch (error) {
                console.error('Ошибка при обновлении последней позиции воспроизведения', error);
            }
        }
    };

    const onAudioEnded = () => {
        updatePlaybackPosition();
        goToNextChapter();
    };

    useEffect(() => {
        fetchChapters();
    }, [bookId]);

    const togglePlayPause = () => {
        if (audioRef.current) {
            if (isPlaying) {
                audioRef.current.pause();
                updatePlaybackPosition();
            } else {
                audioRef.current.play();
            }
            setIsPlaying(!isPlaying);
        }
    };
    const handleTimeUpdate = () => {
        if (audioRef.current) {
            setCurrentTime(audioRef.current.currentTime);
            const value = (audioRef.current.currentTime / audioRef.current.duration) * 100;
            setProgress(value);
        }
    };

    const handlePlaybackRateChange = (rate) => {
        if (audioRef.current) {
            audioRef.current.playbackRate = rate;
            setPlaybackRate(rate);
            setShowPlaybackRateOptions(false);
        }
    };

    const rewind = (seconds) => {
        if (audioRef.current) {
            audioRef.current.currentTime += seconds;
        }
    };

    const goToNextChapter = () => {
        setCurrentChapterIndex((prevIndex) => Math.min(prevIndex + 1, chapters.length - 1));
    };

    const goToPreviousChapter = () => {
        setCurrentChapterIndex((prevIndex) => Math.max(prevIndex - 1, 0));
    };

    const handleProgressChange = (event) => {
        const value = event.target.value;
        audioRef.current.currentTime = (audioRef.current.duration / 100) * value;
        setProgress(value);
        updateProgressBarStyle(value);
    };

    const updateProgressBarStyle = (value) => {
        const newStyle = {
            background: `linear-gradient(to right, #1D1A1A ${value}%, #ddd ${value}%, #ddd 100%)`
        };
        setProgressBarStyle(newStyle);
    };

    useEffect(() => {
        const interval = setInterval(() => {
            if (audioRef.current && isPlaying) {
                const value = (audioRef.current.currentTime / audioRef.current.duration) * 100;
                setProgress(value);
                updateProgressBarStyle(value);
            }
        }, 1000);

        return () => clearInterval(interval);
    }, [isPlaying]);

    const currentChapter = chapters[currentChapterIndex];

    return (
        <div className="audioPlayer">
            <div className="customAudioControls">
                <SkipBack className="controlIcon" onClick={goToPreviousChapter}/>
                <Rewind className="controlIcon" onClick={() => rewind(-10)}/>
                {isPlaying ? (
                    <CirclePause  className="controlIcon" onClick={togglePlayPause}/>
                ) : (
                    <CirclePlay  className="controlIcon" onClick={togglePlayPause}/>
                )}
                <FastForward className="controlIcon" onClick={() => rewind(10)}/>
                <SkipForward className="controlIcon" onClick={goToNextChapter}/>
                <span
                    className="currentTime">{Math.floor(currentTime / 60)}:{('0' + Math.floor(currentTime % 60)).slice(-2)}</span>
                <input
                    type="range"
                    min="0"
                    max="100"
                    value={progress}
                    onChange={handleProgressChange}
                    style={progressBarStyle}
                    className="audioProgress audioProgress"
                />
                <div className="chapterSelector">
                    {currentChapter && (
                        <span className="currentChapter" onClick={() => setShowChaptersMenu(!showChaptersMenu)}>
                            <BookOpen className="controlIcon bookOpen"/>
                            Глава {currentChapter.number}
                    </span>
                    )}
                    {showChaptersMenu && (
                        <div className="chaptersMenu">
                            {chapters.map((chapter, index) => (
                                <div key={chapter.id} onClick={() => selectChapter(index)}>
                                    Глава {chapter.number}
                                </div>
                            ))}
                        </div>
                    )}
                </div>
                <MoreVertical className="controlIcon"
                              onClick={() => setShowPlaybackRateOptions(!showPlaybackRateOptions)}/>
                {showPlaybackRateOptions && (
                    <div className="playbackRateOptions">
                        <button onClick={() => handlePlaybackRateChange(0.5)}>0.5</button>
                        <button onClick={() => handlePlaybackRateChange(0.75)}>0.75</button>
                        <button onClick={() => handlePlaybackRateChange(1)}>1</button>
                        <button onClick={() => handlePlaybackRateChange(1.25)}>1.25</button>
                        <button onClick={() => handlePlaybackRateChange(1.5)}>1.5</button>
                        <button onClick={() => handlePlaybackRateChange(1.75)}>1.75</button>
                        <button onClick={() => handlePlaybackRateChange(2)}>2x</button>
                    </div>
                )}
            </div>
            <audio
                ref={audioRef}
                src={currentChapter ? `http://localhost:8080/api/file/${currentChapter.audioFileId}` : ''}
                onEnded={onAudioEnded}
                onCanPlay={setPlaybackTime} 
                onTimeUpdate={handleTimeUpdate}
            />
        </div>
    );
};

export default AudioPlayerComponent;