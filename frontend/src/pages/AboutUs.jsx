import React from 'react';
import Header from '../components/Header/Header.jsx';
import Footer from '../components/Footer/Footer.jsx';
import '../pages_css/aboutUs.css';
const AboutUs = () => {
    return (
        <div className="aboutus-container">
            <Header activePage="aboutus" />
            <main className="aboutus-content">
                <p>Witamy w juStory!</p>

                <p>Jesteśmy tutaj, aby ułatwić Ci dostęp do wartościowych informacji o literackich światach.<br/><br/>
                    W jednym miejscu znajdziesz informacje o tym, gdzie kupić książki, w jakiej formie oraz o
                    najnowszych premierach.<br/>
                    Nie musisz już szukać wszędzie - jesteśmy miejscem, gdzie miłośnicy literatury spotykają się, dzielą
                    swoimi odkryciami
                    i czerpią inspirację do nowych przygód z książkami.</p>

                <p>Przekształcamy czytanie w fascynującą podróż!</p>
            </main>
            <Footer showProfileAndHello={false}/>
        </div>
    );
}

export default AboutUs;