import React from 'react';

const generateColor = (char) => {
    const charCode = char.charCodeAt(0);
    const hue = charCode * 137 % 360;
    return `hsl(${hue}, 50%, 60%)`;
};

const Avatar = ({ name }) => {
    const firstLetter = name.charAt(0).toUpperCase();
    const backgroundColor = generateColor(firstLetter);
    const avatarStyle = {
        width: '20px',
        height: '20px',
        borderRadius: '20%',
        backgroundColor,
        display: 'flex',
        justifyContent: 'center',
        alignItems: 'center',
        color: '#FFFFFF',
        fontSize: '8px',
        fontWeight: 'bold',
        userSelect: 'none',
    };

    return (
        <div style={avatarStyle}>
            {firstLetter}
        </div>
    );
};

export default Avatar;