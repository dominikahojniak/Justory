import React, { useState, useEffect } from 'react';
import '../pages_css/ToRead.css';
import Footer from '../components/Footer/Footer.jsx';
import Header from '../components/Header/Header.jsx';
import BookInfoToRead from '../components/BookInfoToRead/BookInfoToRead.jsx';
import axios from '../../axiosConfig.js';
function ToRead() {
    const [toReadBooks, setToReadBooks] = useState([]);
    const [userRatings, setUserRatings] = useState([]);
    useEffect(() => {
        fetchToReadBooks();
        fetchUserRatings();
    }, []);

    const fetchToReadBooks = async () => {
        try {
            const response = await axios.get('/api/toread/all', {
                headers: {
                    Authorization: `Bearer ${localStorage.getItem('token')}`
                }
            });
            setToReadBooks(response.data);
        } catch (error) {
            console.error('Error fetching ToRead books:', error);
        }
    };
    const fetchUserRatings = async () => {
        try {
            const response = await axios.get('/api/book-rating/user-ratings', {
                headers: {
                    Authorization: `Bearer ${localStorage.getItem('token')}`
                }
            });
            setUserRatings(response.data);
        } catch (error) {
            console.error('Error fetching user ratings:', error);
        }
    };
    const removeBook = async (bookId) => {
        try {
            await axios.delete(`/api/toread/removebook/${bookId}`, {
                headers: {
                    Authorization: `Bearer ${localStorage.getItem('token')}`
                }
            });
            setToReadBooks(toReadBooks.filter(book => book.id !== bookId));
        } catch (error) {
            console.error('Error removing book from ToRead list:', error);
        }
    };
    return (
    <div className='toread-container'>
    <Header activePage="toRead" />
        <main className='main-toread'>
            <div className="toRead">Do Przeczytania</div>
            <div className="news-toread">
                {toReadBooks.map((book) => {
                    const ratingObj = userRatings.find(rating => rating.book.id === book.id);
                    const rating = ratingObj ? ratingObj.rating : null;
                    return (
                        <BookInfoToRead
                            key={book.id}
                            id={book.id}
                            title={book.title}
                            authors={book.authors}
                            imageSrc={`data:image/jpeg;base64, ${book.img}`}
                            removeBook={removeBook}
                            rating={rating}
                            fetchUserRatings={fetchUserRatings}
                        />
                    );
                })}
            </div>
        </main>
        <Footer showProfileAndHello={false}/>
    </div>
    );
}

export default ToRead;