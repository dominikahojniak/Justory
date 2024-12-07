import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import './header.css';
import malelogo from '../../img/malelogo.png';
import greenHome from '../../img/green-home.svg';
import greenPremieres from '../../img/green-premieres.svg';
import greenSearch from '../../img/green-search.svg';
import greenToRead from '../../img/green-toRead.svg';
import greenProfile from '../../img/green-profile.svg';
import greenAddBook from '../../img/green-addbook.svg';
import home from '../../img/home.svg';
import premieres from '../../img/premieres.svg';
import toRead from '../../img/toRead.svg';
import search from '../../img/search.svg';
import profile from '../../img/profile.svg';
import addbook from '../../img/addbook.svg';
import axios from '../../../axiosConfig.js';

const Header = ({ activePage }) => {
    const [isLoggedIn, setIsLoggedIn] = useState(null);
    const [isAdmin, setIsAdmin] = useState(false);

    useEffect(() => {
        const fetchUserData = async () => {
            try {
                const response = await axios.get('/api/users/profile');
                setIsLoggedIn(true);
                setIsAdmin(response.data.role === 'ADMIN');
            } catch (error) {
                setIsLoggedIn(false);
                setIsAdmin(false);
            }
        };

        fetchUserData();
    }, []);
    if (isLoggedIn === null) {
        return null;
    }
    const isLogSignPage = activePage === 'logsign';
    const headerClass = isLogSignPage ? 'header-nav logsign-page' : 'header-nav';
    const isHomePage = activePage === 'home';
    const homeImage = isHomePage ? greenHome : home;
    const isPremieresPage = activePage === 'premieres';
    const premieresImage = isPremieresPage ? greenPremieres : premieres;
    const isSearchPage = activePage === 'search';
    const searchImage = isSearchPage ? greenSearch : search;
    const isToReadPage = activePage === 'toRead';
    const ToReadImage = isToReadPage ? greenToRead : toRead;
    const isProfilePage = activePage === 'profile';
    const ProfileImage = isProfilePage ? greenProfile : profile;
    const isAddBookPage = activePage === 'addbook';
    const AddBookImage = isAddBookPage ? greenAddBook : addbook;

    return (
        <nav className={headerClass}>
            <ul>
                <li>
                    <div className="images-container">
                        <img src={malelogo} alt="logo"/>
                    </div>
                </li>
                <li><Link to="/home" className={activePage === 'home' ? 'active' : ''}><img src={homeImage} alt="Home"/>Strona Główna</Link></li>
                <li><Link to="/premieres" className={activePage === 'premieres' ? 'active' : ''}><img src={premieresImage} alt="Premieres"/>Premiery</Link></li>
                <li><Link to="/toRead" className={activePage === 'toRead' ? 'active' : ''}><img src={ToReadImage} alt="To Read"/>Do przeczytania</Link></li>
                <li><Link to="/search" className={activePage === 'search' ? 'active' : ''}><img src={searchImage} alt="Search"/>Wyszukaj</Link></li>
                {isLoggedIn && (
                    <>
                        {isAdmin && (
                            <li><Link to="/addBook" className={activePage === 'addbook' ? 'active' : ''}><img src={AddBookImage} alt="Add Book"/>Dodaj</Link></li>
                        )}
                        <li><Link to="/profile" className={activePage === 'profile' ? 'active' : ''}><img src={ProfileImage} alt="Profile"/>Profil</Link></li>
                    </>
                )}
                {!isLoggedIn && (
                    <div className="loginsignup">
                        <Link to="/login">Logowanie</Link>
                        <Link to="/signup" id="signup-link">Rejestracja</Link>
                    </div>
                )}
            </ul>
        </nav>
    );
};

export default Header;