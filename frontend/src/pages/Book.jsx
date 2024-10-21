import React, { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import '../pages_css/book.css';
import Footer from '../components/Footer/Footer.jsx';
import Header from '../components/Header/Header.jsx';
import PlatformItem from '../components/PlatformItem/PlatformItem.jsx';
import X from '../img/goingBack.svg';
import axios from '../../axiosConfig.js';
const Book = () => {
    const { title } = useParams();
    const [book, setBook] = useState(null);
    const [isAdded, setIsAdded] = useState(false);
    const [addError, setAddError] = useState("");
    useEffect(() => {
        const fetchBookData = async () => {
            try {
                const response = await axios.get(`/api/books/title/${title}`);
                setBook(response.data);
            } catch (error) {
                console.error('Error fetching book data:', error);
            }
        };

        fetchBookData();
    }, [title]);
    const handleAddToReadList = async (e) => {
        e.preventDefault();
        try {
            await axios.post(`/api/toread/addbook/${id}`, {}, {
                headers: {
                    Authorization: `Bearer ${localStorage.getItem('token')}`
                }
            });
            setIsAdded(true);
            setAddError("");
        } catch (error) {
            if (error.response && error.response.status === 409) {
                setAddError("Book is already in the to-read list.");
            } else {
                setAddError("Error adding book to read list.");
            }
            console.error('Error adding book to read list:', error);
        }
    };
    if (!book) {
        return <div>Loading...</div>;
    }
    return (
        <div className='book-container'>
            <Header activePage="book" />
            <main className='main-book'>
                <div className="left">
                    <div className="x">
                        <a href="/"><img src={X} alt="Go Back Icon" /></a>
                    </div>
                </div>
                <div className="middle">
                    <div className="image">
                        <img className="news-image-book" src={`data:image/jpeg;base64,${book.img}`} alt="Book Cover"/>
                    </div>
                    <form className="form-book">
                        {!isAdded && <button id="add-button" onClick={handleAddToReadList}>+ To Read</button>}
                        {isAdded && <div className="good-result">Book added to read list!</div>}
                        {addError && <div className="bad-result">{addError}</div>}
                    </form>
                </div>
                <div className="right">
                    <div className="news-description-book">
                        <h3>{book.title}</h3>
                        <p>by {book.authors && book.authors.map(author => (
                            <span key={author.id}>{author.firstName} {author.lastName}</span>
                        )).reduce((prev, curr) => [prev, ', ', curr])} </p>
                        <p>date: {new Date(book.date).toLocaleDateString()}</p>
                        <p>ISBN: {book.isbn}</p>
                        <p>language: {book.language}</p>
                    </div>
                    {/* Subscription Section */}
                    <div className="subscription">
                        <div className="news-title-container-book">
                            <div className="news-title-book">
                                <h3>Subscription</h3>
                            </div>
                        </div>
                        <div className="items">
                            {book.availabilities && book.availabilities
                                .filter(item => item.accessTypeName === "subscription")
                                .map(item => (
                                    <PlatformItem
                                        key={item.platformName + item.formatName}
                                        platformImg={item.platformLogo}
                                        formatName={item.formatName}
                                        formatImg={item.formatLogo}
                                    />
                                ))
                            }
                        </div>
                    </div>

                    {/* Purchase Section */}
                    <div className="buy">
                        <div className="news-title-container-book">
                            <div className="news-title-book">
                                <h3>Buy</h3>
                            </div>
                        </div>
                        <div className="items">
                            {book.availabilities && book.availabilities
                                .filter(item => item.accessTypeName === "purchase")
                                .map(item => (
                                    <PlatformItem
                                        key={item.platformName + item.formatName}
                                        platformImg={item.platformLogo}
                                        formatName={item.formatName}
                                        formatImg={item.formatLogo}
                                    />
                                ))
                            }
                        </div>
                    </div>
                    <div className="news-description-book">
                        <p>{book.description}</p>
                    </div>
                </div>
            </main>
            <Footer/>
        </div>
    );
}

export default Book;