import React from 'react';
import Header from '../components/Header/Header.jsx';
import Footer from '../components/Footer/Footer.jsx';
import '../pages_css/contact.css';
const Contact = () => {
    return (
        <div className="contact-container">
            <Header activePage="contact" />
            <main className="contact-content">
                <p>Jeśli masz jakiekolwiek pytania, możesz się z nami skontaktować pod tym adresem e-mail:</p>
                <p>dominikahojniak@gmail.com</p>
                <p>Infolinia +48 692 774 166</p>
                <p>Otwarte w dni robocze od 10:00 do 16:00.</p>
            </main>
            <Footer showProfileAndHello={false} />
        </div>
    );
}

export default Contact;