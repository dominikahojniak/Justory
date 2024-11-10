import React, {useState, useEffect} from 'react';
import ReactDOM from 'react-dom';
import {BrowserRouter as Router, Routes, Route, Navigate} from 'react-router-dom';
import Login from "./pages/Login.jsx";
import SignUp from './pages/Signup.jsx';
import Home from './pages/Home.jsx';
import Profile from './pages/Profile.jsx';
import Help from './pages/Help.jsx';
import AboutUs from './pages/AboutUs.jsx';
import Contact from './pages/Contact.jsx';
import Book from './pages/Book.jsx';
import AddBook from './pages/AddBook.jsx';
import ToRead from './pages/ToRead.jsx';
import Premieres from './pages/Premieres.jsx';
import Search from './pages/Search.jsx';
import CategoryBooks from './pages/CategoryBooks.jsx';
import axios from '../axiosConfig.js';
import ProtectedRoute from "./components/ProtectedRoute.jsx";
import RatedBooks from './pages/RatedBooks.jsx';

const App = () => {
    const [isLoggedIn, setIsLoggedIn] = useState(null);
    const [userRole, setUserRole] = useState('');

    useEffect(() => {
        const fetchUserProfile = async () => {
            try {
                const response = await axios.get('/api/users/profile', {
                    headers: {
                        Authorization: `Bearer ${localStorage.getItem('token')}`
                    }
                });
                console.log(response.data.role);
                setIsLoggedIn(true);
                setUserRole(response.data.role);
            } catch (error) {
                console.error('Error fetching user profile:', error);
                setIsLoggedIn(false);
            }
        };

        fetchUserProfile();
    }, []);
    if (isLoggedIn === null) {
        return null;
    }
    return (
        <Router>
            <Routes>
                <Route exact path="/" element={<Home/>}/>
                <Route exact path="/home" element={<Home/>}/>
                <Route exact path="/login" element={<Login/>}/>
                <Route exact path="/signup" element={<SignUp/>}/>
                <Route exact path="/profile" element={<Profile/>}/>
                <Route exact path="/help" element={<Help/>}/>
                <Route exact path="/aboutus" element={<AboutUs/>}/>
                <Route exact path="/contact" element={<Contact/>}/>
                <Route path="/book/:title" element={<Book/>}/>
                <Route
                    path="/addbook"
                    element={
                        <ProtectedRoute isAllowed={isLoggedIn && userRole === 'ADMIN'}>
                            <AddBook/>
                        </ProtectedRoute>
                    }
                />
                {isLoggedIn ? (
                    <Route exact path="/toRead" element={<ToRead/>}/>
                ) : (
                    <Route exact path="/toRead" element={<Navigate to="/login"/>}/>
                )}
                <Route exact path="/premieres" element={<Premieres/>}/>
                <Route exact path="/search" element={<Search/>}/>
                <Route exact path="/category/:categoryName" element={<CategoryBooks/>}/>
                <Route exact path="/ratedBooks" element={<RatedBooks/>}/>
            </Routes>
        </Router>
    );
}
export default App;