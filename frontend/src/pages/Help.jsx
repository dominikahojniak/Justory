import React from 'react';
import Header from '../components/Header/Header.jsx';
import Footer from '../components/Footer/Footer.jsx';
import '../pages_css/help.css';
const Help = () => {
    return (
        <div className="help-container">
            <Header activePage="help" />
            <main className="help-content">
                <p>Customer Service Contact:</p>
                <p>help@gmail.com</p>
                <p>Reporting Problems:</p>
                <p>problems@gmail.com</p>
            </main>
            <Footer showProfileAndHello={false} />
        </div>
    );
}

export default Help;