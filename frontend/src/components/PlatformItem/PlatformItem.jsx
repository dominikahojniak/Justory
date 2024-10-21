import React from 'react';
import './PlatformItem.css';
const PlatformItem = ({ platformImg, formatName ,formatImg}) => {
    return (
        <div className="platform-item">
            <img src={`data:image/jpeg;base64,${platformImg}`} alt="platformImg" className="platform-logo"/>
            <div className="options">
                <img src={`data:image/jpeg;base64,${formatImg}`} alt={formatName}/>
            </div>
        </div>
    );
};

export default PlatformItem;
