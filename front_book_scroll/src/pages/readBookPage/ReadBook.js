import React from 'react';
import AudioPlayerComponent from "../../components/audioplayer/AudioPlayerComponent";
import EpubReader from "../../components/EpubReader";

const ReadBook = () => {
    return (
        <div>
            <AudioPlayerComponent/>
            <EpubReader></EpubReader>
        </div>
    );
};

export default ReadBook;