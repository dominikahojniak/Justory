import React from 'react';
import './PlatformItem.css';
function PlatformItem({ platformImg, formatImg }) {
    return (
        <div className="platform-item">
            <img  className="platform-image" src={platformImg} alt="Platform Logo" />
            <div className="options">
                <img src={formatImg} alt="Format Icon" />
            </div>
        </div>
    );
}

export default PlatformItem;