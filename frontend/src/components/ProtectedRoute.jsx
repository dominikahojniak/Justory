import React from 'react';
import { Navigate } from 'react-router-dom';

const ProtectedRoute = ({ isAllowed, redirectPath = '/home', children }) => {
    if (!isAllowed) {
        return <Navigate to={redirectPath} replace />;
    }

    return children;
};

export default ProtectedRoute;