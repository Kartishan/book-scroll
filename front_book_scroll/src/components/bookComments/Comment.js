import React from 'react';
import "./CommentStyle.css";

const Comment = ({ comment, onReply, isChild }) => {
    const commentClass = isChild ? "reviewElement childReviewElement" : "reviewElement";

    return (
        <div className={commentClass}>
            <h4>{comment.username}</h4>
            <p>{comment.title}</p>
            <button onClick={onReply} className="buttonStyle replyButton">Ответить</button>
        </div>
    );
};

export default Comment;