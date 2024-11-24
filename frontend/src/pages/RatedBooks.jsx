import React, {useState, useEffect} from 'react';
import '../pages_css/ToRead.css';
import Footer from '../components/Footer/Footer.jsx';
import Header from '../components/Header/Header.jsx';
import BookInfoRatedBooks from '../components/BookInfoRatedBooks/BookInfoRatedBooks.jsx';
import axios from '../../axiosConfig.js';

function RatedBooks() {
    const [ratedBooks, setRatedBooks] = useState([]);
    useEffect(() => {
        fetchRatedBooks();
    }, []);

    const fetchRatedBooks = async () => {
        try {
            const response = await axios.get('/api/book-rating/user-ratings');
            setRatedBooks(response.data);
        } catch (error) {
            console.error('Error fetching rated books:', error);
        }
    };

    const handleRatingRemoved = (bookId) => {
        setRatedBooks(ratedBooks.filter(book => book.book.id !== bookId));
    };

    return (
        <div className='toread-container'>
            <Header activePage="ratedBooks"/>
            <main className='main-toread'>
                <div className="toRead">Ocenione Książki</div>
                <div className="news-toread">
                    {ratedBooks.map((ratedBook) => (
                        <BookInfoRatedBooks
                            key={ratedBook.book.id}
                            id={ratedBook.book.id}
                            title={ratedBook.book.title}
                            authors={ratedBook.book.authors}
                            imageSrc={`data:image/jpeg;base64, ${ratedBook.book.img}`}
                            rating={ratedBook.rating}
                            onRatingRemoved={handleRatingRemoved}
                        />
                    ))}
                </div>
            </main>
            <Footer showProfileAndHello={false}/>
        </div>
    );
}

export default RatedBooks;