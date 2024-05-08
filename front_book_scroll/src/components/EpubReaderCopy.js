import React, { useState, useEffect, useCallback } from 'react';
import { ReactReader } from 'react-reader';
import { useParams } from 'react-router-dom';
import "./EpubReader.css";
import MyHeader from "./header/MyHeader";
import {API_URL} from "../config";
import {createBookmark, deleteBookmark, getBookmarks} from "../actions/bookActions";
import {postStories} from "../actions/scrollActions";
import axios from "axios";
import AudioPlayerComponent from "./audioplayer/AudioPlayerComponent";

const EpubReader = () => {
    const { bookId } = useParams();
    const [selections, setSelections] = useState([]);
    const [rendition, setRendition] = useState(null);
    const [currentPage, setCurrentPage] = useState(1);
    const [totalPages, setTotalPages] = useState(0);
    const [isMakingBookmarks, setIsMakingBookmarks] = useState(true);
    const [fileId, setFileId] = useState(null);
    const [scroll, setScroll] = useState(false)
    

    const updateHighlights = async () => {
        console.log("Я сработал")
        if (rendition) {
            selections.forEach(selection => {
                rendition.annotations.remove(selection.cfiRange, 'highlight');
            });

            selections.forEach(selection => {
                rendition.annotations.add('highlight', selection.cfiRange, {}, (e) => {}, 'hl', {
                    'fill': 'yellow', 'fill-opacity': '0.3', 'mix-blend-mode': 'multiply'
                });
            });
        }
    };

    const handleTextSelected = useCallback(async (cfiRange, contents) => {
        if (!isMakingBookmarks) return;
        const selectedText = rendition.getRange(cfiRange).toString();
        setSelections(prevSelections => [
            ...prevSelections,
            { text: selectedText, cfiRange }
        ]);
        try {
            const savedBookmark = await createBookmark(bookId, cfiRange, selectedText);
            console.log('Закладка сохранена:', savedBookmark);
            await loadBookmarks();
        } catch (error) {
            console.error('Не удалось сохранить закладку:', error);
        }
        contents.window.getSelection().removeAllRanges();
    }, [rendition, isMakingBookmarks, bookId]);

    useEffect(() => {
        const fetchFileId = async () => {
            try {
                console.log(bookId)
                const response = await axios.get(`${API_URL}api/book/bookFileId/${bookId}`, {
                    headers: { Authorization: `Bearer ${localStorage.getItem('token')}` }
                });
                setFileId(response.data);
            } catch (error) {
                console.error('Ошибка при получении fileId:', error);
            }
        };

        fetchFileId();
    }, );

    const bookUrl = fileId ? `${API_URL}api/book/files/${fileId}` : '';


    useEffect(() => {
        if (rendition) {
            rendition.on('selected', handleTextSelected);
            updateHighlights();
            return () => rendition.off('selected', handleTextSelected);
        }
    }, [rendition, handleTextSelected, selections, updateHighlights]);

    useEffect(() => {
        loadBookmarks();
    }, [bookId]);

    const loadBookmarks = async () => {
        try {
            console.log(bookId)
            const bookmarks = await getBookmarks(bookId);
            setSelections(bookmarks);
        } catch (error) {
            console.error('Не удалось загрузить закладки:', error);
        }
    };

    const removeSelection = useCallback(async (bookmarkId) => {
        if (rendition) {
            const selection = selections.find(s => s.id === bookmarkId);
            if (selection) {
                rendition.annotations.remove(selection.cfiRange, 'highlight', selection.id);
            }

            try {
                await deleteBookmark(bookmarkId);
                await loadBookmarks();
            } catch (error) {
                console.error('Не удалось удалить закладку:', error);
            }
        }
    }, [rendition, selections, loadBookmarks]);

    const handleRendition = useCallback((rend) => {
        setRendition(rend);
        rend.on('relocated', (location) => {
            setCurrentPage(location.start.displayed.page);
            setTotalPages(location.start.displayed.total);
        });
        if (scroll) {
            rend.themes.override('flow', 'scrolled-doc'); // Apply scroll layout
        }
    }, [scroll]);

    const toggleScroll = () => {
        setScroll(!scroll);
    };
    const [isSelectionsPanelOpen, setIsSelectionsPanelOpen] = useState(false);

    const toggleSelectionsPanel = () => {
        setIsSelectionsPanelOpen(!isSelectionsPanelOpen);
    };

    const handleCreateScroll = async (text, cfiRange, bookId) => {
        try {
            console.log(bookId)
            const result = await postStories(bookId, cfiRange, text);
            console.log('Scroll создан:', result);
        } catch (error) {
            console.error('Ошибка при создании scroll:', error);
        }
    };

    return (
        <div>
            <MyHeader/>

            <div className="epubReaderContainer">
                <div className="controlsArea">
                    <div className="pageInfo">
                        Страница {currentPage} / {totalPages} в главе.
                    </div>
                    <button onClick={toggleScroll} className="toggleButton">
                        {scroll ? 'Выключить прокрутку' : 'Переключиться на прокрутку'}
                    </button>
                    <button onClick={() => setIsMakingBookmarks(!isMakingBookmarks)} className="toggleButton">
                        {isMakingBookmarks ? 'Отключить создание закладок' : 'Включить создание закладок'}
                    </button>
                    <button onClick={toggleSelectionsPanel} className="toggleButton">
                        {isSelectionsPanelOpen ? 'Скрыть закладки' : 'Показать закладки'}
                    </button>
                </div>
                <div className="readerArea">
                    <ReactReader
                        key={scroll ? 'scroll-enabled' : 'scroll-disabled'}
                        url={bookUrl}
                        epubInitOptions={{openAs: 'epub'}}
                        epubOptions={scroll ? {
                            flow: 'scrolled',
                            manager: 'continuous',
                        } : {}}
                        getRendition={handleRendition}
                    />
                </div>
                <div className="audioPlayerComponent">
                    <AudioPlayerComponent bookId={bookId}/>
                </div>
                <div className={`selectionsArea ${isSelectionsPanelOpen ? 'open' : ''}`}>
                    <h2 className="selectionsTitle">
                        Закладки:
                    </h2>
                    {selections.map((selection, index) => (
                        <div key={index} className="selection">
                            <span className="selectionText">{selection.text}</span>
                            <button onClick={() => rendition.display(selection.cfiRange)}
                                    className="selectionButton">Перейти
                            </button>
                            <button onClick={() => removeSelection(selection.id, index)}
                                    className="selectionButton">Удалить
                            </button>
                            <button onClick={() => handleCreateScroll(selection.text, selection.cfiRange, bookId)}
                                    className="selectionButton">Создать Scroll
                            </button>
                        </div>
                    ))}
                </div>

            </div>
        </div>
    );
};

export default EpubReader;