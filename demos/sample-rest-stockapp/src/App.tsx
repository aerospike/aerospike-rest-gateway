import CssBaseline from '@material-ui/core/CssBaseline';
import { createMuiTheme, MuiThemeProvider } from '@material-ui/core/styles';
import { PaletteOptions } from '@material-ui/core/styles/createPalette';
import { ConnectedRouter } from 'connected-react-router';
import { History } from 'history';
import * as React from 'react';
import { Route, Switch } from 'react-router';
import PortfolioViewPage from './pages/PortfolioViewPage';
import CreatePortfolioPage from './pages/CreatePortfolioPage';
import IndexPage from './pages/PortfolioIndexPage';
import DataUploadPage from './pages/DataUploadPage';
import SignInPage from './pages/SigninPage';
import CallbackPage from './pages/AuthPage';

export interface AppProps {
    history: History;
}

const palette: PaletteOptions = {
    primary: { main: '#B71C1C' },
    secondary: { main: '#212121' },
    background: {
        default: 'darkgray',
    },
};

const theme = createMuiTheme({ palette });
const App = ({ history }: AppProps) => (
    <MuiThemeProvider theme={theme}>
        <CssBaseline />
        <ConnectedRouter history={history}>
            <Switch>
                <Route exact path={'/'} component={IndexPage} />
                <Route exact path={'/portfolios/:portfolioId'} component={PortfolioViewPage} />
                <Route exact path={'/portfolio'} component={CreatePortfolioPage} />
                <Route exact path={'/upload'} component={DataUploadPage} />
                <Route exact path={'/login'} component={SignInPage} />
                <Route exact path={'/callback'} component={CallbackPage} />
            </Switch>
        </ConnectedRouter>
    </MuiThemeProvider>
);

export default App;
