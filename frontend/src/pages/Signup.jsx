import React, { useState } from 'react';
import '../pages_css/signup.css';
import { useNavigate } from 'react-router-dom';
import Footer from '../components/Footer/Footer.jsx';
import maleMobileLogo from '../img/maleMobileLogo.png';
import Header from '../components/Header/Header.jsx';
import axios from '../../axiosConfig.js';
const SignUp = () => {
    const [email, setEmail] = useState('');
    const [name, setName] = useState('');
    const [phone, setPhone] = useState(null);
    const [password, setPassword] = useState('');
    const navigate = useNavigate();
    const [errors, setErrors] = useState({
        email: false,
        name: false,
        phone: false,
        password: false,
    });
    const handleRegister = async (event) => {
        event.preventDefault();
        if (!email || !name || !phone || !password) {
            alert('Wypełnij wszystkie pola.');
            return;
        }
        if (Object.values(errors).some(error => error)) {
            alert('Popraw błędy walidacji.');
            return;
        }
        try {
            const response = await axios.post('/api/auth/register', {
                email: email,
                name: name,
                phone: phone,
                password: password,
            });
            localStorage.setItem('token', response.data.token);
            navigate('/');
        } catch (error) {
            console.error('Registration failed:', error.response.data);
            alert('Rejestracja nieudana');
        }
    };
    const validatePhone = (value) => {
        const phoneRegex = /^\d{9}$/;
        return phoneRegex.test(value);
    };

    const handlePhoneChange = (e) => {
        const value = e.target.value;
        setPhone(value);
        setErrors(prevErrors => ({ ...prevErrors, phone: !validatePhone(value) }));
        console.log(errors.phone);
    };

    return (

        <div className="signup-container">
            <Header activePage="logsign" />
            <main className="signup-main">
                <div className="signup">
                    <form className="form-signup" onSubmit={handleRegister} >
                        <div className="form-login">
                            <p id="changeToLogin"><a href="login">Zaloguj się</a></p>
                        </div>
                        <h1 id="signup-title">Zarejestruj się</h1>
                        <div className="logoPhoto">
                            <img src={maleMobileLogo} alt="logo"/>
                        </div>
                        <input name="name" type="text" placeholder="Nazwa użytkownika" id="name" value={name} onChange={e => setName(e.target.value)}/>
                        <input name="email" type="text" placeholder="Adres e-mail" id="email" value={email} onChange={e => setEmail(e.target.value)}/>
                        <input name="phone" type="text" placeholder="Numer telefonu" id="phone" value={phone} onChange={handlePhoneChange} className={errors.phone ? 'error' : ''}/>
                        <input name="password" type="password" placeholder="Hasło" id="password" value={password} onChange={e => setPassword(e.target.value)}/>
                        <button type="submit" id="signup-button"> ZAREJESTRUJ SIĘ</button>
                    </form>
                </div>
            </main>
            <Footer/>
        </div>
    );
}

export default SignUp;