import React, { useState, useEffect } from 'react';
import '../pages_css/home.css';
//import cover from './img/okladka.jpg';
import Footer from '../components/Footer/Footer.jsx';
import Header from '../components/Header/Header.jsx';
import HomeBook from '../components/home_book/HomeBook.jsx';
import axios from '../../axiosConfig.js';
const Home = () => {
    const [username, setUsername] = useState("");
    const [books, setBooks] = useState([]);
    const [recommendedBooks, setRecommendedBooks] = useState([]);
    const [isLoggedIn, setIsLoggedIn] = useState(null);

    useEffect(() => {

        const fetchUserData = async () => {
            try {
                const response = await axios.get('/api/users/profile', {
                    headers: {
                        Authorization: `Bearer ${localStorage.getItem('token')}`
                    }
                });
                setUsername(response.data.name);
                setIsLoggedIn(true);
            } catch (error) {
                console.error('Error fetching user data:', error);
                setIsLoggedIn(false);
            }
        };

        fetchUserData();
        const fetchAllBooks = async () => {
            try {
                const response = await axios.get('/api/books');
                setBooks(response.data);
            } catch (error) {
                console.error('Error fetching books:', error);
            }
        };

        fetchAllBooks();

        const fetchRecommendedBooks = async () => {
            try {
                const response = await axios.get('/api/recommendations', {
                    headers: {
                        Authorization: `Bearer ${localStorage.getItem('token')}`
                    }
                });
                setRecommendedBooks(response.data);
            } catch (error) {
                console.error('Error fetching recommended books:', error);
            }
        };

        if (localStorage.getItem('token')) {
            fetchRecommendedBooks();
        }
    }, []);

    return (
        <div id="home-page" className="home-container">
            <Header activePage="home" />
            <main>
                <div className="description">
                    <h3>Discover the stories you've been searching for<br />and the places to find them. </h3>
                    <p>Join justStory, your key to book premieres, and unlock the door to a world of<br />reading possibilities. </p>
                </div>
                <div className="catalog">
                    Catalog
                </div>
                <div className="news">
                    {books.map((book) => (
                        <HomeBook key={book.id} id={book.id} imageSrc={`data:image/jpeg;base64, ${book.img}`} title={book.title} />
                    ))}
                </div>
                {isLoggedIn && (
                    <>
                        <div className="catalog">
                            Recommendations
                        </div>
                        {recommendedBooks.length > 0 ? (
                            <div className="news">
                                {recommendedBooks.map((book) => (
                                    <HomeBook key={book.id} id={book.id} imageSrc={`data:image/jpeg;base64,${book.img}`} title={book.title} />
                                ))}
                            </div>
                        ) : (
                            <p className="no-recommendations">You currently have no recommendations. Please rate some books or add books to your To Read list.</p>
                        )}
                    </>
                )}
            </main>
            <Footer showProfileAndHello={true} username={username}/>
        </div>
    );
}

export default Home;