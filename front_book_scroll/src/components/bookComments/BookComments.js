import React, {useEffect, useState} from 'react';
import Comment from './Comment';
import "./CommentStyle.css"
import {fetchBookComments, postBookComment} from "../../actions/commentActions";

const BookComments = ({ bookId }) => {
    const [comments, setComments] = useState([]);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);
    const [newComment, setNewComment] = useState('');
    const [replyTo, setReplyTo] = useState({});


    useEffect(() => {
        setLoading(true);
        fetchBookComments(bookId)
            .then(data => {
                setComments(data);
                setError(null);
            })
            .catch(err => {
                setError(err.message);
                setComments([]);
            })
            .finally(() => {
                setLoading(false);
            });
    }, [bookId]);

    const handlePostComment = async (parentId = null) => {
        if (!newComment.trim()) return;
        try {
            const data = await postBookComment(bookId, newComment, parentId);
            console.log(data);
            setNewComment('');
            fetchBookComments(bookId)
                .then(data => {
                    setComments(data);
                })
                .catch(err => {
                    console.error(err);
                });
        } catch (error) {
            console.error(error);
        }
    };

    const renderComments = (commentList, parentId = null) => {
        return commentList
            .filter(comment => comment.parentCommentId === parentId)
            .map(comment => (
                <div key={comment.id}>
                    <Comment comment={comment} onReply={() => handleReplyClick(comment.id)} isChild={!!parentId} />
                    {replyTo[comment.id] && (
                        <div className="replyForm">
                            <textarea
                                value={newComment}
                                onChange={e => setNewComment(e.target.value)}
                                placeholder="Напишите ответ..."
                            />
                            <button className="buttonStyle comment-button comment-button-reply" onClick={() => { handlePostComment(comment.id); handleReplyClick(comment.id); }} >Отправить</button>
                        </div>
                    )}
                    <div className="childCommentsContainer">
                        {renderComments(commentList, comment.id)}
                    </div>
                </div>
            ));
    };

    const handleReplyClick = (commentId) => {
        if (replyTo[commentId]) {
            setReplyTo(prev => ({ ...prev, [commentId]: false }));
        } else {
            setReplyTo(prev => ({ ...prev, [commentId]: true })); // Иначе - открыть
        }
    };


    if (loading) return <div>Loading comments...</div>;
    if (error) return <div>Error loading comments: {error}</div>;

    return (
        <div>
            <h3 style={{margin: "20px auto"}}>Комментарии</h3>
            <div className="new-comment-form">
                <textarea
                    value={newComment}
                    onChange={(e) => setNewComment(e.target.value)}
                    placeholder="Добавьте комментарий..."
                />
                <button className="buttonStyle comment-button" onClick={() => handlePostComment(null)}>Комментировать</button>
            </div>
            <section className="reviewsContainer">
                {renderComments(comments)}
            </section>
        </div>
    );
};

export default BookComments;