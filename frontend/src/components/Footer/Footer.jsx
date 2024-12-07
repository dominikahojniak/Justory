import React from 'react';
import { useLocation } from 'react-router-dom';
import './footer.css';
import linkedin from '../../img/linkedin.svg';
import facebook from '../../img/fb.svg';
import ig from '../../img/ig.svg';
const Footer = ({ showProfileAndHello , username}) => {
    const location = useLocation();
    return (
        <footer className={`footer-id ${showProfileAndHello ? 'home-footer-visible' : 'no'}`}>
            <ul className="footer-list">
                <li><a href="/help" className={location.pathname === '/help' ? 'active' : ''}>Pomoc</a></li>
                <li><a href="/aboutus" className={location.pathname === '/aboutus' ? 'active' : ''}>O nas</a></li>
                <li><a href="/contact" className={location.pathname === '/contact' ? 'active' : ''}>Kontakt</a></li>
                {showProfileAndHello && <li>Witaj {username}</li>}
            </ul>
            <div className="media-footer">
                <a href="https://www.linkedin.com/notifications/?filter=all" target="_blank"><img src={linkedin} alt="linkedin" /></a>
                <a href="https://www.facebook.com/?locale=pl_PL" target="_blank"><img src={facebook} alt="facebook" /></a>
                <a href="https://www.instagram.com" target="_blank"><img src={ig} alt="instagram" /></a>
                {showProfileAndHello && <a href="profile"></a>}
            </div>
        </footer>
    );
}

export default Footer;