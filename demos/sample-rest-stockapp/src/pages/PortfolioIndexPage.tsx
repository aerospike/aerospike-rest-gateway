import { Button, Typography } from '@material-ui/core';
import { push } from 'connected-react-router';
import { LocationDescriptor } from 'history';
import * as React from 'react';
import { connect } from 'react-redux';
import { Link } from 'react-router-dom';
import { Action } from 'redux';
import { ThunkDispatch } from 'redux-thunk';
import { addAPICall } from '../actions/apiCall';
import { clearError, setErrorMessage } from '../actions/errors';
import { setRecentPortfolios } from '../actions/portfolios';
import { ContentBox } from '../components/presentational/ContentBox';
import { PortfolioList } from '../components/presentational/PortfolioList';
import ErrorState from '../core/types/ErrorState';
import { StoreState } from '../core/types/StoreState';
import { TypedMap } from '../core/types/TypedMap';
import { doGetRecordNamespaceSetKey } from '../generated/core/actions/keyValueOperationsActionCreators';
import { Record, RestClientError } from '../generated/core/api';
import ExtenderFunctions from '../generated/core/helpers/ApiActionExtender';
import { LastApiCall } from '../generated/core/state/ApiState';
import { PerformBatchGetStateFields } from '../generated/core/state/batchReadOperationsStates';
import { GetRecordNamespaceSetKeyStateFields } from '../generated/core/state/keyValueOperationsStates';
import Page from './Page';
import isAuthError from '../utils/authError';

interface IndexProps {
    doViewPortfolioDetails: (recipeId: string) => void;
    idsResponse: TypedMap<GetRecordNamespaceSetKeyStateFields>;
    recipesResponse: TypedMap<PerformBatchGetStateFields>;
    lastResponse?: TypedMap<LastApiCall>;
    recentPortfolios: string[];
    errorStatus: ErrorState;
    doLoadInitialData: (count: number) => void;
    clearErrorMessage: () => void;
}

const to = (to: LocationDescriptor) => ({ to, component: Link as any });
export class IndexComponent extends React.PureComponent<IndexProps> {
    constructor(props: IndexProps) {
        super(props);
        props.clearErrorMessage();
        props.doLoadInitialData(10);
        // props.doCheckStocksExist();
    }

    public render() {
        return (
            <Page
                errorStatus={this.props.errorStatus}
                lastResponse={this.props.lastResponse}
                pageTitle="Aerospike Stock Central"
            >
                <ContentBox>
                    <Typography align="center" component="h2" variant="display1" gutterBottom>
                        Recently Created Portfolios
                    </Typography>
                    <PortfolioList
                        portfolios={this.props.recentPortfolios}
                        viewPortfolioDetails={this.props.doViewPortfolioDetails}
                    />
                    <Button variant="contained" color="secondary" mini={true} {...to('/portfolio')}>
                        Create New Portfolio
                    </Button>
                </ContentBox>
            </Page>
        );
    }
}

function stateToProps(state: StoreState) {
    return {
        lastResponse: state.api.get('last'),
        recentPortfolios: state.recentPortfolios,
        errorStatus: state.errorState
    };
}

function dispatchToProps(dispatch: ThunkDispatch<StoreState, void, Action>) {
    return {
        clearErrorMessage: () => {
            dispatch(clearError());
        },
        doLoadInitialData: (numberToRetrieve: number) => {
            dispatch(
                doGetRecordNamespaceSetKey({
                    key: process.env.REACT_APP_STOCKS_SYMBOLS_BIN!,
                    namespace: process.env.REACT_APP_STOCK_NS!,
                    set: process.env.REACT_APP_STOCKS_SET!,
                    onError: (err) => {
                        if (isAuthError(err)) {
                            dispatch(setErrorMessage('Not authenticated to use API'));
                            dispatch(push('/login'));
                            return;
                        }
                        dispatch(
                            setErrorMessage('No stock entries Found make sure to upload some!')
                        );
                        dispatch(push(`/upload`));
                    },
                    onSuccess: () => {
                        dispatch(
                            doGetRecordNamespaceSetKey({
                                key: process.env.REACT_APP_RECENT_PORTFOLIOS_KEY!,
                                namespace: process.env.REACT_APP_STOCK_NS!,
                                set: process.env.REACT_APP_STOCKS_SET!,
                                onSuccess: (record?: Record) => {
                                    dispatch(clearError());
                                    const ns = process.env.REACT_APP_STOCK_NS;
                                    const set = process.env.REACT_APP_STOCKS_SET;
                                    const key = process.env.REACT_APP_RECENT_PORTFOLIOS_KEY;
                                    const endpoint = `kvs/${ns}/${set}/${key}`;
                                    dispatch(
                                        addAPICall('GET', endpoint, true, JSON.stringify(record, null, 4))
                                    );
                                    if (record) {
                                        dispatch(setRecentPortfolios(record.bins.portfolios));
                                    }
                                },
                                onError: (err?: RestClientError) => {
                                    if (isAuthError(err)) {
                                        dispatch(setErrorMessage('Not authenticated to use API'));
                                        dispatch(push('/login'));
                                        return;
                                    }
                                    const ns = process.env.REACT_APP_STOCK_NS;
                                    const set = process.env.REACT_APP_STOCKS_SET;
                                    const key = process.env.REACT_APP_RECENT_PORTFOLIOS_KEY;
                                    const endpoint = `kvs/${ns}/${set}/${key}`;
                                    dispatch(
                                        addAPICall(
                                            'GET',
                                            endpoint,
                                            false,
                                            err && err.message ? err.message : ''
                                        )
                                    );
                                    dispatch(
                                        setErrorMessage('No portfolios Found Yet. You can Create the First!')
                                    );
                                    dispatch(setRecentPortfolios([]));
                                }
                            })
                        );
                    }
                })
            );
        },
        doViewPortfolioDetails: (portfolioId: string) => {
            const extender: ExtenderFunctions<Record, RestClientError> = {
                success: {
                    after: async (successArg: Record | RestClientError) => {
                        await dispatch(push(`/portfolios/${portfolioId}`));
                    }
                }
            };
            dispatch(
                doGetRecordNamespaceSetKey({
                    key: portfolioId,
                    namespace: process.env.REACT_APP_STOCK_NS!,
                    set: process.env.REACT_APP_PORTFOLIOS_SET!,
                    onSuccess: (record?: Record) => {
                        dispatch(clearError());
                        const ns = process.env.REACT_APP_STOCK_NS;
                        const set = process.env.REACT_APP_PORTFOLIOS_SET;
                        const key = portfolioId;
                        const endpoint = `kvs/${ns}/${set}/${key}`;
                        dispatch(
                            addAPICall('GET', endpoint, true, JSON.stringify(record, null, 4))
                        );
                    },
                    onError: (err?: RestClientError) => {
                        if (isAuthError(err)) {
                            dispatch(setErrorMessage('Not authenticated to use API'));
                            dispatch(push('/login'));
                            return;
                        }
                        const ns = process.env.REACT_APP_STOCK_NS;
                        const set = process.env.REACT_APP_PORTFOLIOS_SET;
                        const key = portfolioId;
                        const endpoint = `kvs/${ns}/${set}/${key}`;
                        dispatch(
                            addAPICall(
                                'GET',
                                endpoint,
                                false,
                                err && err.message ? err.message : ''
                            )
                        );
                    },
                    extender
                })
            );
        }
    };
}

const IndexPage = connect(
    stateToProps,
    dispatchToProps
)(IndexComponent);

export default IndexPage;
