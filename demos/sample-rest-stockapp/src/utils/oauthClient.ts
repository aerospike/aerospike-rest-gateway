import ClientOAuth2 from 'client-oauth2';

const OAuthClient = new ClientOAuth2({
  clientId: process.env.REACT_APP_CLIENT_ID || 'MR15hWHZcy2GTl27LBi7X7mboYQa',
  authorizationUri: process.env.REACT_APP_OAUTH_AUTHORIZATION_URI || 'https://localhost:8243/authorize',
  redirectUri: process.env.REACT_APP_STOCK_BASEPATH  + '/callback',
  scopes: ['openid']
});

export default OAuthClient;