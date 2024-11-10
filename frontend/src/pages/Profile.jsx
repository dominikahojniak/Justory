import React, { useState, useEffect } from 'react';
import '../pages_css/profile.css';
import Header from '../components/Header/Header.jsx';
import Footer from '../components/Footer/Footer.jsx';
//import profileImage from './img/duzeprofil.svg';
import axios from '../../axiosConfig.js';
import { useNavigate } from 'react-router-dom';
const Profile = () => {
    const [userProfile, setUserProfile] = useState(null);
    const navigate = useNavigate();
    useEffect(() => {
        const fetchUserProfile = async () => {
            try {
                const response = await axios.get('/api/users/profile', {
                    headers: {
                        Authorization: `Bearer ${localStorage.getItem('token')}`
                    }
                });
                const { email, name, userFeaturesID } = response.data;
                setUserProfile({ email, name, phone: userFeaturesID.phone });
            } catch (error) {
                console.error('Error fetching user profile:', error);
            }
        };

        fetchUserProfile();
    }, []);
    const handleLogout = () => {
        localStorage.removeItem('token');
        navigate('/login');
    };
    const handleSeeRatedBooks = () => {
        navigate('/ratedBooks');
    };
    return (
        <div className='profile-container'>
            <Header activePage="profile" />
            <main className='main-profile'>
                <div className="profile">
                    Profile
                </div>
                {userProfile && (
                    <div className="news-profile">
                        <div className="name-profile">{userProfile.name}</div>
                        <div className="email-profile">{userProfile.email}</div>
                        <div className="email-profile">{userProfile.phone}</div>
                        <button onClick={handleSeeRatedBooks} className="rated-books-button">
                            See Rated Books
                        </button>
                        <button onClick={handleLogout} className="logout-profile">Log out</button>
                    </div>
                )}
            </main>
            <Footer showProfileAndHello={false}/>
        </div>
    );
}

export default Profile;