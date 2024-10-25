import React from 'react';
import './BookInfoPremieres.css';
const BookInfoPremieres = ({ date, title, imageSrc }) => {
    const [year, month, day] = date;
    const formattedMonth = month < 10 ? '0' + month : month;
    const formattedDay = day < 10 ? '0' + day : day;

    const formattedDate = `${year}-${formattedMonth}-${formattedDay}`;
    return (
        <div className="news-container-premieres">
            <div className="date"> <h3>{formattedDate}</h3> </div>
            <div className="right-premieres">
                <div className="news-description-premieres">
                    <p>{title}</p>
                </div>
                <img className="news-image-premieres" src={imageSrc} alt="News Image 1" />
            </div>
        </div>
    );
}

export default BookInfoPremieres;