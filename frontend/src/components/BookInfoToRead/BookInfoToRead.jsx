import React, { useState, useEffect } from 'react';
import './BookInfoToRead.css';
import {Link} from 'react-router-dom';
import Rating from '@mui/material/Rating';
import Button from '@mui/material/Button';
import DeleteIcon from '@mui/icons-material/Delete';
import axios from '../../../axiosConfig.js';

const BookInfoToRead = ({id, title, authors = [], imageSrc, removeBook, rating, fetchUserRatings}) => {
    const handleRemove = (e) => {
        e.preventDefault();
        removeBook(id);
    };
    const handleRatingChange = async (event, newRating) => {
        try {
            await axios.post(`/api/book-rating/addbook/${id}`, null, {
                params: {
                    rating: newRating,
                },
            });
            console.log(`Book ${id} rated with ${newRating} stars.`);
            fetchUserRatings();
        } catch (error) {
            console.error('Error saving rating:', error);
        }
    };
    return (
        <div className="news-container-toread">
            <Link to={`/book/${title}`} className="book-link">
                <img className="news-image-toread" src={imageSrc} alt="News Image 1"/>
            </Link>
            <div className="news-description-toread">
                <h3>{title}</h3>
                <p> {authors && authors.map(author => (
                    <span key={author.id}>{author.firstName} {author.lastName}</span>
                )).reduce((prev, curr) => [prev, ', ', curr])}</p>
            </div>
            <div className="rating-section">
                <Rating
                    name={`rating-${id}`}
                    value={rating}
                    onChange={handleRatingChange}
                    readOnly={rating !== null}
                />
            </div>
            <Button
                variant="outlined"
                startIcon={<DeleteIcon />}
                onClick={handleRemove}
                className="remove-rating-button"
            >
            </Button>
        </div>
    );
}

export default BookInfoToRead;