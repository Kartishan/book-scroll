import React from 'react';
import './Story.css';
import {createAvatar} from "@dicebear/core";
import {adventurer} from "@dicebear/collection";

const Story = ({data}) => {
    if (!data) {
        return <div>Loading...</div>;
    }
    const avatarSvg = createAvatar(adventurer, {
        seed: data.username,
    });

    return (
        <div className="storyDataContainer">
            <div className="storyTextContainer">
                <div className="storyText">
                    <p>{data.name}</p>
                </div>
                <div className="storyUserHeader">
                    <div className="storyUserAvatar" dangerouslySetInnerHTML={{__html: avatarSvg}}/>
                    <p className="authorInfo">{data.username}</p>
                </div>
            </div>

        </div>
    );
};

export default Story;