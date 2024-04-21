import React, { useState, useEffect, useCallback } from 'react';
import { ReactReader } from 'react-reader';
import { useParams } from 'react-router-dom';
import "./EpubReader.css";
import MyHeader from "./header/MyHeader";
import {API_URL} from "../config";

const EpubReader = () => {
    const { bookId } = useParams();
    const bookUrl = `${API_URL}api/book/files/${bookId}`;
    const [selections, setSelections] = useState([]);
    const [rendition, setRendition] = useState(null);
    const [currentPage, setCurrentPage] = useState(1);
    const [totalPages, setTotalPages] = useState(0);

    const handleTextSelected = useCallback((cfiRange, contents) => {
        const selectedText = rendition.getRange(cfiRange).toString();
        setSelections(prevSelections => [
            ...prevSelections,
            { text: selectedText, cfiRange: cfiRange }
        ]);
        rendition.annotations.add('highlight', cfiRange, {}, null, 'hl', {
            'fill': 'yellow', 'fill-opacity': '0.3', 'mix-blend-mode': 'multiply'
        });
        contents.window.getSelection().removeAllRanges();
    }, [rendition]);

    useEffect(() => {
        if (rendition) {
            rendition.on('selected', handleTextSelected);
            return () => rendition.off('selected', handleTextSelected);
        }
    }, [rendition, handleTextSelected]);

    const removeSelection = useCallback((cfiRange, index) => {
        if (rendition) {
            rendition.annotations.remove(cfiRange, 'highlight');
            setSelections(selections.filter((_, i) => i !== index));
        }
    }, [rendition, selections]);

    const handleRendition = useCallback((rend) => {
        setRendition(rend);
        rend.on('relocated', (location) => {
            setCurrentPage(location.start.displayed.page);
            setTotalPages(location.start.displayed.total);
        });
    }, []);

    const [isSelectionsPanelOpen, setIsSelectionsPanelOpen] = useState(false);

    const toggleSelectionsPanel = () => {
        setIsSelectionsPanelOpen(!isSelectionsPanelOpen);
    };

    return (
        <div>
            <MyHeader/>
            <div className="epubReaderContainer">
                <div className="controlsArea">
                    <div className="pageInfo">
                        Страница {currentPage} / {totalPages} в главе.
                    </div>
                    <button onClick={toggleSelectionsPanel} className="toggleButton">
                        {isSelectionsPanelOpen ? 'Скрыть закладки' : 'Показать закладки'}
                    </button>
                </div>
                <div className="readerArea">
                    <ReactReader
                        url={bookUrl}
                        epubInitOptions={{openAs: 'epub'}}
                        getRendition={handleRendition}
                    />
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
                            <button onClick={() => removeSelection(selection.cfiRange, index)}
                                    className="selectionButton">Удалить
                            </button>
                        </div>
                    ))}
                </div>
            </div>
        </div>
    );
};

export default EpubReader;