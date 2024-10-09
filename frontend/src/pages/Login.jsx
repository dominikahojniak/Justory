import React, { useState } from 'react';
import '../pages_css/login.css';
import maleMobileLogo from '../img/maleMobileLogo.png';
import Footer from '../components/footer/Footer.jsx';
import Header from '../components/header/Header.jsx';
import axios from '../../axiosConfig.js';
import { useNavigate } from 'react-router-dom';
const Login = () => {
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const navigate = useNavigate();
    const handleLogin = async (event) => {
        event.preventDefault();
        if (!email  || !password) {
            alert('Please fill in all fields.');
            return;
        }
        try {
            const response = await axios.post('/api/auth/login', {
                email: email,
                password: password,
            });
            localStorage.setItem('token', response.data.token);
            navigate('/home');
        } catch (error) {
            console.error('Login failed:', error.response.data);
            alert('Login failed');
        }
    };
    return (
        <div className="login-container">
            <Header activePage="logsign" />
            <main className='login-main'>
                <div className="login">
                    <form className="form" onSubmit={handleLogin}>
                        <h1 id="login-title">Log In</h1>
                        <div className="logoPhoto">
                            <img src={maleMobileLogo} alt="logo" />
                        </div>
                        <input name="email" type="text" placeholder="email" id="email"  value={email}  onChange={e => setEmail(e.target.value)}/>
                        <input name="password" type="password" placeholder="password" id="password"  value={password} onChange={e => setPassword(e.target.value)}/>
                        <button type="submit" id="login-button"> LOG IN </button>
                        <div id='goToSignup' className='goToSignup'>
                            <div id='donthaveanaccount' className='donthaveanaccount'>
                                Don't have an account?
                            </div>
                            <a href="signup" className='signup'>
                                Sign Up
                            </a>
                        </div>
                    </form>
                </div>
            </main>
            <Footer showProfileAndHello={false}/>
        </div>
    );
}

export default Login;