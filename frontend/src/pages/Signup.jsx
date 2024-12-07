import React, { useState } from 'react';
import '/app/src/pages_css/signup.css';
import { useNavigate } from 'react-router-dom';
import Footer from '../components/Footer/Footer.jsx';
import maleMobileLogo from '../img/maleMobileLogo.png';
import Header from '../components/Header/Header.jsx';
import axios from '../../axiosConfig.js';
import { validatePhoneNumber, validatePassword } from '../validation/validators.jsx';
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
            navigate('/');
        } catch (error) {
            console.error('Registration failed:', error.response.data);
            alert('Rejestracja nieudana');
        }
    };

    const handlePhoneChange = (e) => {
        const value = e.target.value;
        setPhone(value);
        setErrors(prev => ({ ...prev, phone: !validatePhoneNumber(value) }));
        console.log(errors.phone);
    };

    const handlePasswordChange = (e) => {
        const value = e.target.value;
        setPassword(value);
        setErrors(prev => ({ ...prev, password: !validatePassword(value) }));
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
                        {errors.phone && (
                        <p className="error-message">Numer telefonu powinien mieć dokładnie 9 cyfr.</p>
                        )}
                        <input name="password" type="password" placeholder="Hasło" id="password" value={password} onChange={handlePasswordChange} className={errors.password ? 'error' : ''}/>
                        {errors.password && (
                            <p className="error-message">
                                Hasło powinno zawierać co najmniej jedną cyfrę i jedną wielką literę.
                            </p>
                        )}
                        <button type="submit" id="signup-button"> ZAREJESTRUJ SIĘ</button>
                    </form>
                </div>
            </main>
            <Footer/>
        </div>
    );
}

export default SignUp;