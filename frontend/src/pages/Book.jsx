import React, { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import '../pages_css/book.css';
import Footer from '../components/Footer/Footer.jsx';
import Header from '../components/Header/Header.jsx';
import PlatformItem from '../components/PlatformItem/PlatformItem.jsx';
import UserLocationIcon from '../img/locationIcon.png';
import BookstoreIcon from '../img/bookstoreIcon.png';
import X from '../img/goingBack.svg';
import axios from '../../axiosConfig.js';
import { MapContainer, TileLayer, Marker, Popup } from 'react-leaflet';
import 'leaflet/dist/leaflet.css';
import L from 'leaflet';

delete L.Icon.Default.prototype._getIconUrl;
L.Icon.Default.mergeOptions({
    iconRetinaUrl: BookstoreIcon,
    shadowUrl: 'https://cdnjs.cloudflare.com/ajax/libs/leaflet/1.7.1/images/marker-shadow.png',
});
const userIcon = new L.Icon({
    iconUrl: UserLocationIcon,
    shadowUrl: 'https://cdnjs.cloudflare.com/ajax/libs/leaflet/1.7.1/images/marker-shadow.png',
});

const Book = () => {
    const { title } = useParams();
    const [book, setBook] = useState(null);
    const [isAdded, setIsAdded] = useState(false);
    const [addError, setAddError] = useState("");
    const [userLocation, setUserLocation] = useState(null);
    const [booksLocation, setBooksLocation] = useState([]);
    const [isLoggedIn, setIsLoggedIn] = useState(null);
    const [isAdmin, setIsAdmin] = useState(false);
    const [addResponseDeleted, setAddResponseDeleted] = useState("");

    useEffect(() => {
        const fetchUserData = async () => {
            try {
                const response = await axios.get('/api/users/profile', {
                    headers: {
                        Authorization: `Bearer ${localStorage.getItem('token')}`
                    }
                });
                setIsLoggedIn(true);
                setIsAdmin(response.data.role === 'ADMIN');
            } catch (error) {
                setIsLoggedIn(false);
                setIsAdmin(false);
            }
        };

        fetchUserData();
    }, []);

    useEffect(() => {
        const fetchBookData = async () => {
            try {
                const response = await axios.get(`/api/books/title/${title}`);
                setBook(response.data);
                console.log('Fetched book data:', response.data);
            } catch (error) {
                console.error('Error fetching book data:', error);
            }
        };

        fetchBookData();
    }, [title]);
    useEffect(() => {
        if (navigator.geolocation) {
            navigator.geolocation.getCurrentPosition(
                (position) => {
                    setUserLocation({
                        lat: position.coords.latitude,
                        lng: position.coords.longitude,
                    });
                },
                (error) => {
                    console.error("Error fetching current location:", error);
                }
            );
        }
    }, []);

    useEffect(() => {
        if (userLocation) {
            fetchNearbyBooksLocation();
        }
    }, [userLocation]);

    const fetchNearbyBooksLocation = async () => {
        try {
            const response = await axios.get('/api/booksLocations', {
                params: {
                    lat: userLocation.lat,
                    lng: userLocation.lng,
                    radius: 2500
                }
            });
            setBooksLocation(response.data.elements.map((el) => ({
                name: el.tags.name,
                type: el.tags.amenity === "library" ? "Biblioteka" : "Księgarnia",
                lat: el.lat,
                lng: el.lon
            })));
        } catch (error) {
            console.error("Error fetching bookstores data:", error);
        }
    };

    const handleAddToReadList = async (e) => {
        e.preventDefault();
        if (!book || !book.id) {
            console.error('Book ID is undefined');
            return;
        }
        try {
            await axios.post(`/api/toread/addbook/${book.id}`, {}, {
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
    const handleDeleteBook = async (e) => {
        e.preventDefault();
        if (!book || !book.id) {
            console.error('Book ID is undefined');
            return;
        }
        try {
            await axios.delete(`/api/books/delete/${book.id}`, {
                headers: {
                    Authorization: `Bearer ${localStorage.getItem('token')}`
                }
            });
            setAddResponseDeleted("Deleted permanently from app");
        } catch (error) {
            console.error('Error deleting book:', error);
        }
    };

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
                        {isLoggedIn && (
                            <>
                                {!isAdded && <button id="addToRead-button" onClick={handleAddToReadList}>+ To Read</button>}
                                {isAdded && <div className="good-result">Book added to read list!</div>}
                                {addError && <div className="bad-result">{addError}</div>}
                                {isAdmin && (
                                    <button id="delete-button" onClick={handleDeleteBook}>Delete Book</button>

                                )}
                                {addResponseDeleted && <div className="bad-result">{addResponseDeleted}</div>}
                            </>
                        )}
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
                        <p>publisher: {book.publisher && book.publisher.name}</p>
                        <p>categories: {book.categories && book.categories.map(category => (
                            <span key={category.id}>{category.name}</span>
                        )).reduce((prev, curr) => [prev, ', ', curr])}</p>
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
                    {userLocation && (
                        <MapContainer center={userLocation} zoom={13} style={{ height: '400px', width: '90%' }}>
                            <TileLayer
                                url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
                                attribution='&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
                            />
                            <Marker position={userLocation} icon={userIcon}>
                                <Popup>You're here!</Popup>
                            </Marker>
                            {booksLocation.map((store, index) => (
                                <Marker key={index} position={[store.lat, store.lng]}>
                                    <Popup>
                                        <strong>{store.name}</strong>
                                        <em>{store.type}</em>
                                    </Popup>
                                </Marker>
                            ))}
                        </MapContainer>
                    )}
                </div>
            </main>
            <Footer/>
        </div>
    );
}

export default Book;