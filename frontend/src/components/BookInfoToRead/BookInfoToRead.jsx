import React from 'react';
import './BookInfoToRead.css';
import { Link } from 'react-router-dom';
const BookInfoToRead = ({ id, title, author, imageSrc, removeBook }) => {
    const handleRemove = (e) => {
        e.preventDefault();
        removeBook(id);
    };

  return (
    <div className="news-container-toread">
        <Link to={`/book/${title}`} className="book-link">
      <img className="news-image-toread" src={imageSrc} alt="News Image 1" />
        </Link>
      <div className="news-description-toread">
        <h3>{title}</h3>
        <p>by {author}</p>
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