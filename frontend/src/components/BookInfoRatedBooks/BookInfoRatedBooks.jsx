import React from 'react';
import './BookInfoRatedBooks.css';
import {Link} from 'react-router-dom';
import Rating from '@mui/material/Rating';
import Button from '@mui/material/Button';
import DeleteIcon from '@mui/icons-material/Delete';
import axios from '../../../axiosConfig.js';



const BookInfoToRead = ({id, title, authors = [], imageSrc, rating, onRatingRemoved}) => {

    const handleRemoveRating = async () => {
        try {
            await axios.delete(`/api/book-rating/removebook/${id}`);
            onRatingRemoved(id);
            console.log(`Successfully removed rating for book ${title}`);
        } catch (error) {
            console.error('Error removing rating:', error);
        }
    };
    return (
        <div className="rated-book-container">
            <Link to={`/book/${title}`} className="book-link">
                <img className="rated-book-image" src={imageSrc} alt="Book cover"/>
            </Link>
            <div className="rated-book-description">
                <h3>{title}</h3>
                <p> {authors && authors.map(author => (
                    <span key={author.id}>{author.firstName} {author.lastName}</span>
                )).reduce((prev, curr) => [prev, ', ', curr])}</p>
            </div>
            <div className="rating-section">
                <Rating
                    name={`rating-${id}`}
                    value={rating || 0}
                    readOnly
                />
            </div>
            <Button
                variant="outlined"
                startIcon={<DeleteIcon />}
                onClick={handleRemoveRating}
                className="remove-rating-button"
            >
            </Button>
        </div>
    );
};

export default BookInfoToRead;