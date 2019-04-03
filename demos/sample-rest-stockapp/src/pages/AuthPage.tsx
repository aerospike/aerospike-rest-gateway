import * as React from 'react';
import { connect } from 'react-redux';import { push } from 'connected-react-router';
import OauthClient from '../utils/oauthClient';
import {setJWTToken} from '../generated/core/helpers/apiConfiguration';
import { StoreState } from '../core/types/StoreState';
import { Action } from 'redux';
import { setErrorMessage } from '../actions/errors';

import { ThunkDispatch } from 'redux-thunk';
import Page from './Page';
import ErrorState from '../core/types/ErrorState';

interface AuthPageProps {
    dispatch: ThunkDispatch<StoreState, void, Action>;
    clearErrorMessage: () => void;
    setErrorMessage:(msg: string) => void;
    setAuthToken: (token:string) => void;
    errorStatus: ErrorState;

}

class CallbackComponent extends React.Component<AuthPageProps> {
    componentDidMount() {
        const storage = window.localStorage;
        const expectedState = storage.getItem('oauthState');
      // just redirect to '/' in both cases
        OauthClient.token.getToken(window.location).then((token) => {
            if (token.data.state != null && expectedState === token.data.state) {
                this.props.setAuthToken(token.accessToken);
            } else {
                this.props.setErrorMessage("uthorization failed: Invalid authentication state.");
            }
        }).catch(err => {
            console.error(err);
            this.props.setErrorMessage("Authorization failed.");
        })
    }
    render() {
        return (
            <Page errorStatus={this.props.errorStatus}
            ><div>Validating permissions...
              </div>
            </Page>);
    }
}

function stateToProps(state: StoreState) {
    return {
        lastResponse: state.api.get('last'),
        errorStatus: state.errorState
    };
}

function dispatchToProps(dispatch: ThunkDispatch<StoreState, void, Action>) {
    return {
        setAuthToken: (token: string) => {
            token = token.startsWith("Bearer ") ? token : "Bearer " + token;
            setJWTToken(token);
            dispatch(push('/'));
        },
        setErrorMessage: (msg: string) => {
            dispatch(setErrorMessage(msg));
            dispatch(push('/login'));
        },
        dispatch
    };
}


const CallbackPage = connect(
    stateToProps,
    dispatchToProps
)(CallbackComponent);

export default CallbackPage;
