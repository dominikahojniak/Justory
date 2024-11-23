import React from 'react';
import Header from '../components/Header/Header.jsx';
import Footer from '../components/Footer/Footer.jsx';
import '../pages_css/help.css';
const Help = () => {
    return (
        <div className="help-container">
            <Header activePage="help" />
            <main className="help-content">
                <p>Kontakt z Obsługą Klienta:</p>
                <p>pomoc@gmail.com</p>
                <p>Zgłaszanie problemów:</p>
                <p>problemy@gmail.com</p>
            </main>
            <Footer showProfileAndHello={false}/>
        </div>
    );
}

export default Help;