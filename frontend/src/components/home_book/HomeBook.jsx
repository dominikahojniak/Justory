import React from 'react';
import './homeBook.css';
import { Link } from 'react-router-dom';
const HomeBook = ({ id, imageSrc, title }) => {
    return (
        <div className="news-container-homebook">
            <Link to={`/book/${title}`}>
                <img className="news-image-homebook" src={imageSrc} alt="Book Cover" />
                <div className="news-description-homebook">
                    <h3>{title}</h3>
                </div>
            </Link>
        </div>
    );
}
export default HomeBook;