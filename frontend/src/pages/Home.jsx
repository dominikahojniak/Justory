import React, { useState, useEffect } from 'react';
import '../pages_css/home.css';
//import cover from './img/okladka.jpg';
import Footer from '../components/Footer/Footer.jsx';
import Header from '../components/Header/Header.jsx';
import HomeBook from '../components/home_book/HomeBook.jsx';
import Banner from '../img/baner.png'
import axios from '../../axiosConfig.js';
const Home = () => {
    const [username, setUsername] = useState("");
    const [books, setBooks] = useState([]);
    const [recommendedBooks, setRecommendedBooks] = useState([]);
    const [isLoggedIn, setIsLoggedIn] = useState(null);

    useEffect(() => {

        const fetchUserData = async () => {
            try {
                const response = await axios.get('/api/users/profile');
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
                const response = await axios.get('/api/recommendations');
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
                    <img src={Banner} alt="Baner promocyjny" className="banner-image"/>
                    <p>Dołącz do justStory, klucza do premier książkowych, i otwórz drzwi<br/>do świata czytelniczych
                        możliwości.</p>
                </div>
                <div className="catalog">
                    Zobacz nasz katalog
                </div>
                <div className="news">
                    {books.map((book) => (
                        <HomeBook key={book.id} id={book.id} imageSrc={`data:image/jpeg;base64, ${book.img}`} title={book.title} />
                    ))}
                </div>
                {isLoggedIn && (
                    <>
                        <div className="catalog">
                            Te tytuły mogą Cię zainteresować
                        </div>
                        {recommendedBooks.length > 0 ? (
                            <div className="news">
                                {recommendedBooks.map((book) => (
                                    <HomeBook key={book.id} id={book.id} imageSrc={`data:image/jpeg;base64,${book.img}`} title={book.title} />
                                ))}
                            </div>
                        ) : (
                            <p className="no-recommendations">Aktualnie nie masz rekomendacji. Oceń kilka książek lub dodaj książki do swojej listy "Do przeczytania".</p>
                        )}
                    </>
                )}
            </main>
            <Footer showProfileAndHello={true} username={username}/>
        </div>
    );
}

export default Home;