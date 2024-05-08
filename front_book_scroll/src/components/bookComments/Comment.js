import React from 'react';
import { createAvatar } from '@dicebear/core';
import { adventurer } from '@dicebear/collection';
import "./CommentStyle.css";

const Comment = ({ comment, onReply, isChild }) => {
    const commentClass = isChild ? "reviewElement childReviewElement" : "reviewElement";
    const avatarSvg = createAvatar(adventurer, {
        seed: comment.username,
    });

    return (
        <div className={commentClass}>
            <div className="commentHeader">
                <div className="commentAvatar" dangerouslySetInnerHTML={{ __html: avatarSvg }} />
                <h4>{comment.username}</h4>
            </div>
            <p>{comment.title}</p>
            <button onClick={onReply} className="buttonStyle replyButton">Ответить</button>
        </div>
    );
};

export default Comment;