import React, { useState, useRef, useEffect } from 'react';
import "./description.css";

const Description = ({ description }) => {
    const [expanded, setExpanded] = useState(false);
    const [isOverflowing, setIsOverflowing] = useState(false);
    const descriptionRef = useRef(null);

    useEffect(() => {
        const descriptionElement = descriptionRef.current;
        if (descriptionElement.scrollHeight > descriptionElement.clientHeight) {
            setIsOverflowing(true);
        } else {
            setIsOverflowing(false);
        }
    }, [description]);

    const toggleDescription = () => {
        setExpanded(!expanded);
    };

    return (
        <div className="bookDescriptionContainer">
            <div ref={descriptionRef} className={`bookOriginalDescription ${expanded ? 'full' : ''}`}>{description}</div>
            {isOverflowing && <p className="fullBookDescriptionBtn" onClick={toggleDescription}>{expanded ? 'Свернуть описание' : 'Раскрыть описание'}</p>}
        </div>
    );
};

export default Description;
