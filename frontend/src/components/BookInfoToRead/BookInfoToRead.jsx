import React, {useState} from 'react';
import './BookInfoToRead.css';
import {Link} from 'react-router-dom';
import Rating from '@mui/material/Rating';
import axios from '../../../axiosConfig.js';

const BookInfoToRead = ({id, title, authors = [], imageSrc, removeBook}) => {
    const [rating, setRating] = useState(null);
    const handleRemove = (e) => {
        e.preventDefault();
        removeBook(id);
    };
    const handleRatingChange = async (event, newRating) => {
        setRating(newRating);

        try {
            const token = localStorage.getItem('token');
            await axios.post(`/api/book-rating/${id}`, null, {
                params: {
                    rating: newRating,
                },
                headers: {
                    Authorization: `Bearer ${token}`,
                },
            });
            console.log(`Book ${id} rated with ${newRating} stars.`);
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
                <p>by {authors && authors.map(author => (
                    <span key={author.id}>{author.firstName} {author.lastName}</span>
                )).reduce((prev, curr) => [prev, ', ', curr])}</p>
            </div>
            <div className="rating-section">
                <Rating
                    name={`rating-${id}`}
                    value={rating}
                    onChange={handleRatingChange}
                />
            </div>
            <div className="remove-button">
                <form className="form-toread" onSubmit={handleRemove}>
                    <button type="submit" id="remove-button" name="remove_button">- Remove</button>
                </form>
            </div>
        </div>
    );
}

export default BookInfoToRead;