import React, { useState } from 'react';
import '../pages_css/login.css';
import maleMobileLogo from '../img/maleMobileLogo.png';
import Footer from '../components/Footer/Footer.jsx';
import Header from '../components/Header/Header.jsx';
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
            navigate('/home');
        } catch (error) {
            console.error('Login failed:', error.response.data);
            alert('Logowanie nieudane');
        }
    };
    return (
        <div className="login-container">
            <Header activePage="logsign" />
            <main className='login-main'>
                <div className="login">
                    <form className="form" onSubmit={handleLogin}>
                        <h1 id="login-title">Zaloguj się</h1>
                        <div className="logoPhoto">
                            <img src={maleMobileLogo} alt="logo" />
                        </div>
                        <input name="email" type="text" placeholder="Adres e-mail" id="email"  value={email}  onChange={e => setEmail(e.target.value)}/>
                        <input name="password" type="password" placeholder="Hasło" id="password"  value={password} onChange={e => setPassword(e.target.value)}/>
                        <button type="submit" id="login-button"> ZALOGUJ SIĘ </button>
                        <div id='goToSignup' className='goToSignup'>
                            <div id='donthaveanaccount' className='donthaveanaccount'>
                                Nie posiadasz konta?
                            </div>
                            <a href="signup" className='signup'>
                                Zarejestruj się
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