import React from 'react';
import './Story.css';

const Story = ({data}) => {
    if (!data) {
        return <div>Loading...</div>;
    }

    return (
        <div className="storyDataContainer">
            <div className="storyTextContainer">
                <div className="storyText">
                    <p>{data.name}</p>
                </div>
                <p className="authorInfo">{data.username}</p>
            </div>
        </div>
    );
};

export default Story;