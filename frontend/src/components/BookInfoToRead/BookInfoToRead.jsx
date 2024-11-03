import React from 'react';
import './BookInfoToRead.css';
import { Link } from 'react-router-dom';
const BookInfoToRead = ({ id, title, authors =[], imageSrc, removeBook }) => {
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
            <p>by {authors && authors.map(author => (
                <span key={author.id}>{author.firstName} {author.lastName}</span>
            )).reduce((prev, curr) => [prev, ', ', curr])}</p>
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