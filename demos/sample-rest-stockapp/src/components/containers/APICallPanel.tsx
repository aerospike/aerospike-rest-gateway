import * as React from 'react';
import { APICallState } from '../../core/types/ApiCallState';
import { StoreState } from '../../core/types/StoreState';
import { connect } from 'react-redux';
import { TransitionGroup, CSSTransition } from 'react-transition-group';
import { ThunkDispatch } from 'redux-thunk';
import { Action } from 'redux';
import APICallEntry from '../presentational/APICallEntry';
import {setActiveCallNumber} from '../../actions/apiCall';
import { Typography } from '@material-ui/core';

interface APICallPanelProps {
    apiCallState: APICallState;
    setActiveCall: (callNumber: number) => void;
}
class APIPCallPanelComponent extends React.PureComponent<APICallPanelProps> {
    public render() {
        return (
        <div>
        <Typography 
        style={{textAlign: 'center'}}
        variant="display1" component="h2"
        gutterBottom>Recent API Responses</Typography>

                <TransitionGroup>
                        {
                            this.props.apiCallState.apiCalls.map(
                                (callInfo) =>  {
                                    return (
                                    <CSSTransition
                                        key={`ac-${callInfo.callNumber}`}
                                        classNames="move"
                                        timeout={{enter: 500, exit: 300}}
                                    >
                                        <APICallEntry
                                            onClick={this.props.setActiveCall}
                                            activeCallNumber={this.props.apiCallState.activeCallNumber}
                                            callInfo={callInfo}
                                        />
                                    </CSSTransition>
                                    );
                                }
                            )
                        }
                </TransitionGroup>
        </div>);
    }
}


function stateToProps(state: StoreState) {
    return {
        apiCallState: state.apiCallState
    };
}

function dispatchToProps(dispatch: ThunkDispatch<StoreState, void, Action>) {
    return {
        setActiveCall: (callNumber: number) => {
            dispatch(setActiveCallNumber(callNumber));
        }
    };
}

const APIPCallPanel = connect(
    stateToProps,
    dispatchToProps
)(APIPCallPanelComponent);

export default APIPCallPanel;
