import React from "react";
import "./AccessTypeComponent.css";

const AccessTypeComponent = ({ platforms, formats, accessTypes, availability, setAvailability }) => {

    const handleAccessTypeChange = (platformName, formatName, accessTypeName, isChecked) => {
        setAvailability(prev => ({
            ...prev,
            [platformName]: {
                ...prev[platformName],
                [formatName]: {
                    ...prev[platformName]?.[formatName],
                    [accessTypeName]: isChecked
                }
            }
        }));
    };

    if (!platforms || platforms.length === 0 || !formats || formats.length === 0 || !accessTypes || accessTypes.length === 0) {
        return <div>No data available</div>;
    }

    return (
        <div className="access-type-component">
            {platforms.map((platform) => (
                <div key={platform.name} className="platform">
                    <div className="platform-title">{platform.name}</div>
                    <div className="formats-container">
                    {formats.map((format) => (
                        <div key={format.name} className="format-item">
                            <div className="format-title">{format.name}</div>
                            {accessTypes.map((accessType) => (
                                <div key={accessType.name} className="access-type-item">
                                    <input
                                        type="checkbox"
                                        id={`access-${platform.name}-${format.name}-${accessType.name}`}
                                        onChange={(e) => handleAccessTypeChange(platform.name, format.name, accessType.name, e.target.checked)}
                                        checked={availability[platform.name]?.[format.name]?.[accessType.name] || false}
                                    />
                                    <label htmlFor={`access-${platform.name}-${format.name}-${accessType.name}`}>{accessType.name}</label>
                                </div>
                            ))}
                        </div>
                    ))}
                </div>
                </div>
            ))}
        </div>
    );
};

export default AccessTypeComponent;
