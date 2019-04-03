
import React from "react";
import OauthClient from "../utils/oauthClient";
import uuid4 from "uuid/v4";
import Page from "./Page";
import { StoreState } from "../core/types/StoreState";
import ErrorState from "../core/types/ErrorState";
import { ThunkDispatch } from "redux-thunk";
import { Action } from "redux";
import { connect } from "react-redux";

interface AuthPageProps {
    errorStatus: ErrorState;
}

class SignInComponent extends React.Component<AuthPageProps> {
  onLoginButtonClick(event:any) {
    const localStorage = window.localStorage;
    const state = uuid4();
    localStorage.setItem('oauthState', state);
    event.preventDefault();
    // SET STATE HERE
    const callbackURI = process.env.REACT_APP_STOCK_BASEPATH ? process.env.REACT_APP_STOCK_BASEPATH + '/callback' : `${window.location.protocol}//${window.location.hostname}${window.location.port ? `:${window.location.port}` : ''}/callback`;
    const authURI = OauthClient.token.getUri( {redirectUri: callbackURI, state: state});
    console.log(authURI)
    window.location.replace(authURI);
  }

  render() {
    return (
      <Page errorStatus={this.props.errorStatus}
      ><div>
        <h3>Welcome to the aerospike stock app!</h3>
        <p>Please log in to continue</p>
        <button onClick={this.onLoginButtonClick}>Authorize the Rest API</button>
        </div>
      </Page>
    );
  }
}

function stateToProps(state: StoreState) {
    return {
        errorStatus: state.errorState
    };
}

function dispatchToProps(dispatch: ThunkDispatch<StoreState, void, Action>) {
    return {
    };
}


const SignInPage = connect(
    stateToProps,
    dispatchToProps
)(SignInComponent);


export default SignInPage;