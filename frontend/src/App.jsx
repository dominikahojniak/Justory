import React from 'react';
import ReactDOM from 'react-dom';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import Login from "./pages/Login.jsx";
import SignUp from './pages/Signup.jsx';
import Home from './pages/Home.jsx';
import Profile from './pages/Profile.jsx';
import Help from './pages/Help.jsx';
import AboutUs from './pages/AboutUs.jsx';
import Contact from './pages/Contact.jsx';
import axios from '../axiosConfig.js';
const App = () => {
    // const [isLoggedIn,setIsLoggedIn] = useState(false);
    // const [userRole, setUserRole] = useState('');

    // useEffect(() => {
    //     const fetchUserProfile = async () => {
    //         try {
    //             const response = await axios.get('/api/users/profile', {
    //                 headers: {
    //                     Authorization: `Bearer ${localStorage.getItem('token')}`
    //                 }
    //             });
    //             console.log(response.data.role);
    //             setIsLoggedIn(true);
    //             setUserRole(response.data.role);
    //         } catch (error) {
    //             console.error('Error fetching user profile:', error);
    //         }
    //     };
    //
    //     fetchUserProfile();
    // }, []);
    return (
        <Router>
            <Routes>
                <Route exact path="/" element={<Login/>} />
                <Route exact path="/home" element={<Home/>} />
                <Route exact path="/login" element={<Login/>}/>
                <Route exact path="/signup" element={<SignUp/>} />
                <Route exact path="/profile" element={<Profile/>} />
                <Route exact path="/help" element={<Help />} />
                <Route exact path="/aboutus" element={<AboutUs />} />
                <Route exact path="/contact" element={<Contact />} />
            </Routes>
        </Router>
    );
}
export default App;