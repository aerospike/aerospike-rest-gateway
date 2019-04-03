import * as React from 'react';
import APICallInfo from '../../core/types/ApiCallState';
import ExpansionPanel from '@material-ui/core/ExpansionPanel';
import ExpansionPanelSummary from '@material-ui/core/ExpansionPanelSummary';
import ExpansionPanelDetails from '@material-ui/core/ExpansionPanelDetails';
import ExpandMoreIcon from '@material-ui/icons/ExpandMore';
import Check from '@material-ui/icons/Check';
import Error from '@material-ui/icons/Error';
import Card from '@material-ui/core/Card';
import CardContent from '@material-ui/core/CardContent';

import { Typography } from '@material-ui/core';
interface APICallEntryProps {
    onClick: (callNumber: number) => void;
    callInfo: APICallInfo;
    activeCallNumber: number;
}

export default class APICallEntry extends React.PureComponent<APICallEntryProps> {

    public render() {

        const {success, payload} = this.props.callInfo.response;
        const {method, endpoint, request} = this.props.callInfo;

        const successIcon: JSX.Element = ( success ? 
            <span aria-label="Successful API Call"><Check style={{color: 'green'}}/> </span> : 
            <span aria-label="Unsuccesful API Call"><Error color="error"/></span>
        );
        
        const payloadSection: JSX.Element = requestResponseContent(request, payload);
        return (
        <ExpansionPanel elevation={5}>
            <ExpansionPanelSummary expandIcon={<ExpandMoreIcon />}>
                {successIcon} {` ${method}: ${endpoint} `}
            </ExpansionPanelSummary>
            <ExpansionPanelDetails>
                {payloadSection}
            </ExpansionPanelDetails>
        </ExpansionPanel>
        );
    }
}

function requestResponseContent(request: string, response: string): JSX.Element {
    const requestSection: JSX.Element | null = (request && request !== '') ?
        (
            <span>
            <Typography 
            variant="subheading" component="h3" gutterBottom
                style={{textAlign: 'center', textDecoration: 'underline'}}
            >
            Request Payload</Typography>
            <Typography style={{fontFamily: 'monospace', whiteSpace: 'pre'}} component="p">
            {request}
            </Typography>
            </span>
        )
        : null;

    const responseSection: JSX.Element | null = (response && response !== '') ?
        (
            <span>
            <Typography 
                variant="subheading" component="h3" gutterBottom
                style={{textAlign: 'center', textDecoration: 'underline'}}
                >Response Data</Typography>
            <Typography style={{fontFamily: 'monospace', whiteSpace: 'pre'}} component="p">
            {response}
            </Typography>
            </span>
        )
        : null;

    return (
        <Card style={{width: '100%'}}>
            <CardContent>
                {requestSection}
                {responseSection}
            </CardContent>
        </Card>
    );
}
