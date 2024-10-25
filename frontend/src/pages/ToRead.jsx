import React, { useState, useEffect } from 'react';
import '../pages_css/ToRead.css';
import Footer from '../components/Footer/Footer.jsx';
import Header from '../components/Header/Header.jsx';
import BookInfoToRead from '../components/BookInfoToRead/BookInfoToRead.jsx';
import axios from '../../axiosConfig.js';
function ToRead() {
    const [toReadBooks, setToReadBooks] = useState([]);
    useEffect(() => {
        fetchToReadBooks();
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
        <div className="toRead">ToRead</div>
        <div className="news-toread">
            {toReadBooks.map((book) => (
                <BookInfoToRead
                    key={book.id}
                    id={book.id}
                    title={book.title}
                    author={book.author}
                    imageSrc={`data:image/jpeg;base64, ${book.img}`}
                    removeBook={removeBook}
                />
            ))}
        </div>
      </main>
      <Footer showProfileAndHello={false}/>
    </div>
  );
}

export default ToRead;