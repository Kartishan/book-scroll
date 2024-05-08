import React, { useState, useEffect, useCallback } from 'react';
import { useParams } from 'react-router-dom';
import axios from "axios";
import { ReactReader } from 'react-reader';
import MyHeader from "./header/MyHeader";
import AudioPlayerComponent from "./audioplayer/AudioPlayerComponent";
import { API_URL } from "../config";
import { createBookmark, deleteBookmark, getBookmarks } from "../actions/bookActions";
import { postStories } from "../actions/scrollActions";

const ContextMenu = ({ x, y, onClose }) => {
    return (
        <div
            style={{
                position: 'absolute',
                top: y,
                left: x,
                zIndex: '1000',
                background: '#FFFFFF',
                border: '1px solid #CCC',
                boxShadow: '0px 4px 12px rgba(0, 0, 0, 0.15)',
                borderRadius: '4px',
                padding: '8px',
            }}
        >
            <button onClick={onClose}>Закрыть меню</button>
        </div>
    );
};

const EpubReader = () => {
    const { bookId } = useParams();
    const [selections, setSelections] = useState([]);
    const [rendition, setRendition] = useState(null);
    const [currentPage, setCurrentPage] = useState(1);
    const [totalPages, setTotalPages] = useState(0);
    const [isMakingBookmarks, setIsMakingBookmarks] = useState(true);
    const [fileId, setFileId] = useState(null);
    const [scroll, setScroll] = useState(false);
    const [contextMenu, setContextMenu] = useState({ show: false, x: 0, y: 0 });
    const [isSelectionsPanelOpen, setIsSelectionsPanelOpen] = useState(false);

    useEffect(() => {
        const fetchFileId = async () => {
            try {
                const response = await axios.get(`${API_URL}api/book/bookFileId/${bookId}`, {
                    headers: { Authorization: `Bearer ${localStorage.getItem('token')}` }
                });
                setFileId(response.data);
            } catch (error) {
                console.error('Ошибка при получении fileId:', error);
            }
        };

        fetchFileId();
    }, [bookId]);

    useEffect(() => {
        loadBookmarks();
    }, [bookId]);

    const loadBookmarks = async () => {
        try {
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
            rend.themes.override('flow', 'scrolled-doc');
        }
    }, [scroll]);

    const toggleScroll = () => {
        setScroll(!scroll);
    };

    const toggleSelectionsPanel = () => {
        setIsSelectionsPanelOpen(!isSelectionsPanelOpen);
    };

    const handleCreateScroll = async (text, cfiRange, bookId) => {
        try {
            const result = await postStories(bookId, cfiRange, text);
        } catch (error) {
            console.error('Ошибка при создании scroll:', error);
        }
    };

    const handleContextMenu = (event) => {
        event.preventDefault();
        setContextMenu({
            show: true,
            x: event.clientX,
            y: event.clientY
        });
    };

    const closeContextMenu = () => {
        setContextMenu({ show: false, x: 0, y: 0 });
    };

    const bookUrl = fileId ? `${API_URL}api/book/files/${fileId}` : '';

    return (
        <div>
            <MyHeader />
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
                <div className="readerArea" onContextMenu={handleContextMenu}>
                    <ReactReader
                        key={scroll ? 'scroll-enabled' : 'scroll-disabled'}
                        url={bookUrl}
                        epubInitOptions={{ openAs: 'epub' }}
                        epubOptions={scroll ? {
                            flow: 'scrolled',
                            manager: 'continuous',
                        } : {}}
                        getRendition={handleRendition}
                    />
                </div>
                <div className="audioPlayerComponent">
                    <AudioPlayerComponent bookId={bookId} />
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
                            <button onClick={() => removeSelection(selection.id)}
                                    className="selectionButton">Удалить
                            </button>
                            <button onClick={() => handleCreateScroll(selection.text, selection.cfiRange, bookId)}
                                    className="selectionButton">Создать Scroll
                            </button>
                        </div>
                    ))}
                </div>
                {contextMenu.show && (
                    <ContextMenu
                        x={contextMenu.x}
                        y={contextMenu.y}
                        onClose={closeContextMenu}
                    />
                )}
            </div>
        </div>
    );
};

export default EpubReader;