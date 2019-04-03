/**
 * Preparations for themeability. Currently allows for named common colors and etc.
 */

const colors = {
    red: '#c31618',
    darkRed: '#cc4449',
    purple: '#c31649',
    black: '#000000',
    white: '#ffffff',
};

const theme = {
    colors: colors,
    link: {
        text: colors.red,
        hover: colors.darkRed,
        active: colors.purple,
    },
    images: {
        rocket: '/image/aerospike-rocket.jpg',
        jc: '/image/jc.jpg',
        white: '/image/white.png',
        cook: '/image/cook.svg',
    },
};

export default theme;
