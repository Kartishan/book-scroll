import React, {useCallback, useEffect, useState} from 'react';
import {ReactReader} from 'react-reader';
import {useParams} from 'react-router-dom';
import "./EpubReader.css";
import MyHeader from "./header/MyHeader";
import {API_URL} from "../config";
import {createBookmark, deleteBookmark, getBookmarks} from "../actions/bookActions";
import {postStories} from "../actions/scrollActions";
import axios from "axios";
import {MoreVertical} from 'lucide-react';
import Modal from "./modal/Modal";
import HeroesPage from "../pages/HeroesPage";
import SummariesPage from "../pages/SummariesPage";


const EpubReader = () => {
    const {bookId} = useParams();
    const [selections, setSelections] = useState([]);
    const [rendition, setRendition] = useState(null);
    const [currentPage, setCurrentPage] = useState(1);
    const [totalPages, setTotalPages] = useState(0);
    const [isMakingBookmarks, setIsMakingBookmarks] = useState(true);
    const [fileId, setFileId] = useState(null);
    const [scroll, setScroll] = useState(false)
    const [isEllipsisMenuOpen, setIsEllipsisMenuOpen] = useState(false);
    const [activeBookmark, setActiveBookmark] = useState(null);
    const [isModalOpen, setIsModalOpen] = useState(false);
    const [selectedTextForBookmark, setSelectedTextForBookmark] = useState('');
    const [selectedCfiRangeForBookmark, setSelectedCfiRangeForBookmark] = useState('');
    const [comment, setComment] = useState('');
    const [exportUrl, setExportUrl] = useState('');
    const [isExportModalOpen, setIsExportModalOpen] = useState(false);
    const [importLink, setImportLink] = useState('');
    const [isImportModalOpen, setIsImportModalOpen] = useState(false);
    const [isHeroesModalOpen, setIsHeroesModalOpen] = useState(false);
    const [isSummariesModalOpen, setIsSummariesModalOpen] = useState(false);

    const updateHighlights = async () => {
        if (rendition) {
            selections.forEach(selection => {
                rendition.annotations.remove(selection.cfiRange, 'highlight');
            });

            console.log("4el")
            console.log(rendition.annotations)


            selections.forEach(selection => {
                rendition.annotations.add('highlight', selection.cfiRange, {}, (e) => {
                }, 'hl', {
                    'fill': 'yellow', 'fill-opacity': '0.3', 'mix-blend-mode': 'multiply'
                });
            });
        }
    };

    const handleTextSelected = useCallback(async (cfiRange, contents) => {
        if (!isMakingBookmarks) return;
        const selectedText = rendition.getRange(cfiRange).toString();
        setSelectedTextForBookmark(selectedText);
        setSelectedCfiRangeForBookmark(cfiRange);
        setIsModalOpen(true); // Открываем модальное окно
        contents.window.getSelection().removeAllRanges();
    }, [rendition, isMakingBookmarks]);
    const confirmBookmarkCreation = async () => {
        try {
            const savedBookmark = await createBookmark(bookId, selectedCfiRangeForBookmark, selectedTextForBookmark, comment);
            console.log('заметка сохранена:', savedBookmark);
            await loadBookmarks();
            setIsModalOpen(false);
            setComment('');
        } catch (error) {
            console.error('Не удалось сохранить заметку:', error);
        }
    };

    useEffect(() => {
        const fetchFileId = async () => {
            try {
                console.log(bookId)
                const response = await axios.get(`${API_URL}api/book/bookFileId/${bookId}`, {
                    headers: {Authorization: `Bearer ${localStorage.getItem('token')}`}
                });
                setFileId(response.data);
            } catch (error) {
                console.error('Ошибка при получении fileId:', error);
            }
        };

        fetchFileId();
    },);

    const bookUrl = fileId ? `${API_URL}api/book/files/${fileId}` : '';


    useEffect(() => {
        if (rendition) {
            rendition.on('selected', handleTextSelected);
            updateHighlights();
            return () => rendition.off('selected', handleTextSelected);
        }
    }, [rendition, handleTextSelected, selections]);

    useEffect(() => {
        loadBookmarks();
    }, [bookId]);

    const loadBookmarks = async () => {
        try {
            console.log(bookId)
            const bookmarks = await getBookmarks(bookId);
            setSelections(bookmarks);
            updateHighlights();
        } catch (error) {
            console.error('Не удалось загрузить Заметки:', error);
        }
    };

    const removeSelection = useCallback(async (bookmarkId) => {
        try {
            await deleteBookmark(bookId, bookmarkId);

            const newSelections = selections.filter(s => s.id !== bookmarkId);
            setSelections(newSelections);

            loadBookmarks();
            updateHighlights()
        } catch (error) {
            console.error('Не удалось удалить заметку:', error);
        }
    }, [bookId, selections]);

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

    const handleImportClick = () => {
        setIsImportModalOpen(true);
    };

    const importBookmarks = async () => {
        const token = importLink.split('/share/')[1]; // Извлекаем токен из ссылки
        if (!token) {
            console.error('Неверный формат ссылки.');
            return;
        }

        try {
            const response = await axios.get(`http://localhost:8080/api/new/bookmarks/import/${token}`, {
                headers: {Authorization: `Bearer ${localStorage.getItem('token')}`}
            });
            // Обработка ответа сервера
            console.log('Заметки импортированы:', response.data);
            setIsImportModalOpen(false); // Закрываем модальное окно
            setImportLink(''); // Очищаем ссылку
            loadBookmarks(); // Обновляем список заметок
        } catch (error) {
            console.error('Ошибка при импорте заметок:', error);
        }
    };


    const handleExportClick = async () => {
        try {
            const response = await axios.post(`http://localhost:8080/api/new/bookmarks/share/${bookId}`, {}, {
                headers: {Authorization: `Bearer ${localStorage.getItem('token')}`}
            });
            const token = response.data; // Предполагаем, что сервер возвращает токен напрямую в теле ответа
            const exportUrl = `http://localhost:3000/share/${token}`; // Формируем полный URL
            setExportUrl(exportUrl); // Сохраняем URL в состоянии
            setIsExportModalOpen(true); // Открываем модальное окно
        } catch (error) {
            console.error('Ошибка при получении ссылки для экспорта:', error);
        }
    };

    const copyToClipboard = () => {
        navigator.clipboard.writeText(exportUrl).then(() => {
            // Показать сообщение об успешном копировании или изменить состояние
            console.log('Ссылка скопирована в буфер обмена');
        }).catch((error) => {
            console.error('Ошибка при копировании в буфер обмена:', error);
        });
    };

    const handleGoToClick = (cfiRange) => {
        console.log('Перейти');
        rendition.display(cfiRange);
        setActiveBookmark(null);
    };

    const handleRemoveClick = (bookmarkId) => {
        console.log('Удалить');
        removeSelection(bookmarkId);
        setActiveBookmark(null);
    };

    const handleCreateScrollClick = (text, cfiRange, bookId) => {
        console.log('Создать Scroll');
        handleCreateScroll(text, cfiRange, bookId);
        setActiveBookmark(null);
    };


    const handleOpenHeroesModal = () => {
        setIsHeroesModalOpen(true);
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
                    <button onClick={() => setIsSummariesModalOpen(true)} className="toggleButton">
                        Пересказы глав
                    </button>
                    <button onClick={handleOpenHeroesModal} className="toggleButton">
                        Герои
                    </button>
                    <button onClick={() => setIsMakingBookmarks(!isMakingBookmarks)} className="toggleButton">
                        {isMakingBookmarks ? 'Отключить создание заметок' : 'Включить создание заметок'}
                    </button>
                    <button onClick={toggleSelectionsPanel} className="toggleButton">
                        {isSelectionsPanelOpen ? 'Скрыть Заметки' : 'Показать Заметки'}
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
                <div className={`selectionsArea ${isSelectionsPanelOpen ? 'open' : ''}`}>
                    <h2 className="selectionsTitle">
                        Заметки:
                        <MoreVertical className="ellipsisIcon" style={{right: '10px'}}
                                      onClick={() => setIsEllipsisMenuOpen(!isEllipsisMenuOpen)}/>
                    </h2>
                    {isEllipsisMenuOpen && (
                        <div className="ellipsisMenu">
                            <button onClick={handleImportClick}>Импорт</button>
                            <button onClick={handleExportClick}>Экспорт</button>
                        </div>
                    )}
                    {selections.map((selection, index) => (
                        <div key={index} className="selection">
                            <span className="selectionText">{selection.text}</span>
                            {/*<div className="commentDivider"></div>*/}
                            {selection.comment && <span className="selectionComment">{selection.comment}</span>}
                            <MoreVertical className="ellipsisIcon"
                                          onClick={() => setActiveBookmark(selection.id === activeBookmark ? null : selection.id)}/>
                            {activeBookmark === selection.id && (
                                <div className="ellipsisMenu">
                                    <button onClick={() => handleGoToClick(selection.cfiRange)}>Перейти</button>
                                    <button onClick={() => handleRemoveClick(selection.id)}>Удалить</button>
                                    <button
                                        onClick={() => handleCreateScrollClick(selection.text, selection.cfiRange, bookId)}>Создать
                                        Scroll
                                    </button>
                                </div>
                            )}
                        </div>
                    ))}
                </div>

            </div>
            <Modal active={isModalOpen} setActive={setIsModalOpen}>
                <div className="modalTwoButton-text">Комментарий к заметке</div>
                <textarea
                    className="modalTwoButton-textarea"
                    value={comment}
                    onChange={(e) => setComment(e.target.value)}
                    placeholder="Введите комментарий"
                />
                <div className="createHighlite">
                    <button onClick={confirmBookmarkCreation} className="buttonStyle modalTwoButton">Да</button>
                    <button className="buttonStyle modalTwoButton" onClick={() => setIsModalOpen(false)}>Нет</button>
                </div>
            </Modal>
            <Modal active={isExportModalOpen} setActive={setIsExportModalOpen}>
                <div className="modal-content">
                    <p style={{marginBottom: '10px'}}>Ссылка для экспорта заметок:</p>
                    <label
                        id="exportUrl"
                        onClick={copyToClipboard}
                        style={{
                            display: 'block',
                            marginBottom: '20px',
                            cursor: 'pointer',
                            background: '#f3f3f3',
                            padding: '10px',
                            borderRadius: '4px',
                            fontFamily: 'monospace',
                            wordWrap: 'break-word',
                        }}
                    >
                        {exportUrl}
                    </label>
                    <button
                        onClick={copyToClipboard}
                        style={{
                            padding: '10px 20px',
                            fontSize: '1em',
                            cursor: 'pointer',
                            border: 'none',
                            borderRadius: '15px',
                            background: '#1D1A1A',
                            color: 'white',
                            marginBottom: '10px'
                        }}
                    >
                        Копировать
                    </button>
                </div>
            </Modal>
            <Modal active={isImportModalOpen} setActive={setIsImportModalOpen}>
                <div className="modal-content">
                    <div>Введите ссылку для импорта заметок:</div>
                    <input
                        type="text"
                        value={importLink}
                        onChange={(e) => setImportLink(e.target.value)}
                        placeholder="Токен"
                        style={{
                            padding: '10px',
                            marginBottom: '10px',
                            border: '1px solid #ccc',
                            borderRadius: '4px'
                        }}
                    />
                    <button
                        onClick={importBookmarks}
                        style={{
                            padding: '10px 20px',
                            fontSize: '1em',
                            cursor: 'pointer',
                            border: 'none',
                            borderRadius: '15px',
                            background: '#1D1A1A',
                            color: 'white'
                        }}
                    >
                        Импортировать
                    </button>
                </div>
            </Modal>
            <Modal active={isHeroesModalOpen} setActive={setIsHeroesModalOpen}>
                <HeroesPage bookId={bookId} />
            </Modal>
            <Modal active={isSummariesModalOpen} setActive={setIsSummariesModalOpen}>
                <SummariesPage bookId={bookId} />
            </Modal>
        </div>
    );
};

export default EpubReader;