import React from 'react';
import Header from '../components/Header/Header.jsx';
import Footer from '../components/Footer/Footer.jsx';
import '../pages_css/contact.css';
const Contact = () => {
    return (
        <div className="contact-container">
            <Header activePage="contact" />
            <main className="contact-content">
                <p>If you have any questions, you can always contact us on this email:</p>
                <p>juStory@gmail.com</p>
                <p>Hotline +48 22 322 65 44</p>
                <p>Open on weekdays from 10:00 to 16:00.</p>
            </main>
            <Footer showProfileAndHello={false} />
        </div>
    );
}

export default Contact;