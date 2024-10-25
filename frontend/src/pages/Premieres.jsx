import React, { useState, useEffect } from 'react';
import Footer from '../components/Footer/Footer.jsx';
import Header from '../components/Header/Header.jsx';
import BookInfoPremieres from '../components/BookInfoPremieres/BookInfoPremieres.jsx';
import '../pages_css/premieres.css';
import axios from '../../axiosConfig.js';
import { Link } from 'react-router-dom';
const Premieres = () => {
    const [books, setBooks] = useState([]);
    useEffect(() => {
        const fetchBooks = async () => {
            try {
                const response = await axios.get('/api/books/premieres');
                const sortedBooks = response.data.sort((a, b) => new Date(a.date) - new Date(b.date));
                setBooks(sortedBooks);
            } catch (error) {
                console.error('Error fetching books:', error);
            }
        };

        fetchBooks();
    }, []);
    return (
        <div className="premieres-container">
            <Header activePage="premieres" />
            <main className='main-premieres'>
                <div className="premieres">
                    Premieres
                </div>
                <div className="news-premieres">
                    {books.map((book, index) => (
                        <Link to={`/book/${book.title}`} key={index} className="premiereBook-link">
                        <BookInfoPremieres key={index} date={book.date} title={book.title} imageSrc={`data:image/jpeg;base64, ${book.img}`} />
                        </Link>
                    ))}
                </div>
            </main>
            <Footer showProfileAndHello={false}/>
        </div>
    );
}

export default Premieres;