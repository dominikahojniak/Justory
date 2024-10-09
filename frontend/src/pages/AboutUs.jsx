import React from 'react';
import Header from '../components/Header/Header.jsx';
import Footer from '../components/Footer/Footer.jsx';
import '../pages_css/aboutUs.css';
const AboutUs = () => {
    return (
        <div className="aboutus-container">
            <Header activePage="aboutus" />
            <main className="aboutus-content">
                <p>Welcome to juStory!</p>

                <p>We are here to make it easier for you to access valuable information about the literary
                    worlds.<br/><br/> In one place you will find where to buy books, in what form and the latest
                    premieres.<br/> You don't have to look everywhere anymore - we are your destination where literature
                    enthusiasts meet, share their discoveries and get inspiration for new adventures with books.</p>

                <p>We transform reading into a fascinating journey!</p>
            </main>
            <Footer showProfileAndHello={false}/>
        </div>
    );
}

export default AboutUs;