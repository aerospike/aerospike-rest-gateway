import * as I from 'immutable';
import * as React from 'react';
import { connect } from 'react-redux';
import { Action } from 'redux';
import { ThunkDispatch } from 'redux-thunk';
import {
    clearActiveGroupStocks,
    clearActiveStock,
    setActiveGroupStocks,
    setActiveStockBins,
    setActiveStockSymbol,
    setStockCountFilter
} from '../actions/active-stock';
import { addAPICall } from '../actions/apiCall';
import { clearError, setErrorMessage } from '../actions/errors';
import { setActiveStockEntries } from '../actions/stockentries';
import { ActiveStockPanel } from '../components/containers/ActiveStockPanel';
import { StockList } from '../components/presentational/StockList';
import ErrorState from '../core/types/ErrorState';
import { StoreState } from '../core/types/StoreState';
import { TypedMap } from '../core/types/TypedMap';
import { doPerformBatchGet } from '../generated/core/actions/batchReadOperationsActionCreators';
import { doGetRecordNamespaceSetKey } from '../generated/core/actions/keyValueOperationsActionCreators';
import { Record, RestClientError } from '../generated/core/api';
import ExtenderFunctions from '../generated/core/helpers/ApiActionExtender';
import { LastApiCall } from '../generated/core/state/ApiState';
import SimpleResponse from '../generated/core/types/SimpleResponse';
import Page from './Page';
import isAuthError from '../utils/authError';
import { push } from 'connected-react-router';

interface ViewPortfolioProps {
    doClearActiveStock: () => void;
    doClearPortfolioStocks: () => void;
    doGetStockSymbols: (name: string) => void;
    doSetActiveStock: (symbol: string) => void;
    clearError: () => void;
    activeStockSymbol: string;
    activeGroupStocks: Array<I.Map<string, any>>;
    match: any;
    lastResponse?: TypedMap<LastApiCall>;
    errorStatus: ErrorState;
}

// const to = (to: LocationDescriptor) => ({ to, component: Link as any });
export class StockComponent extends React.PureComponent<ViewPortfolioProps> {
    constructor(props: ViewPortfolioProps) {
        super(props);
        props.clearError();
        props.doClearPortfolioStocks();
        props.doClearActiveStock();
    }

    public componentDidMount() {
        const props = this.props;
        if (props.match && props.match.params && props.match.params.portfolioId) {
            props.doGetStockSymbols(props.match.params.portfolioId);
        }
    }

    public render() {
        const { doGetStockSymbols, ...listProps } = this.props;
        let groupName: string | null = null;
        if (this.props.match && this.props.match.params && this.props.match.params.portfolioId) {
            groupName = this.props.match.params.portfolioId;
        }
        return (
            <Page
                errorStatus={this.props.errorStatus}
                lastResponse={this.props.lastResponse}
                pageTitle="Aerospike Stock Central"
            >
                <StockList {...{ ...listProps, groupName }} />
                <ActiveStockPanel />
            </Page>
        );
    }
}

function stateToProps(state: StoreState) {
    return {
        lastResponse: state.api.get('last'),
        activeStockSymbol: state.activeStockSymbol,
        activeGroupStocks: state.activeGroupStocks,
        activeStockBins: state.activeStockBins,
        errorStatus: state.errorState
    };
}

function dispatchToProps(dispatch: ThunkDispatch<StoreState, void, Action>) {
    return {
        clearError: () => {
            dispatch(clearError());
        },
        doGetStockSymbols: (portfolioName: string) => {
            const extender: ExtenderFunctions<Record, RestClientError> = {
                success: {
                    after: async (successArg?: Record) => {
                        dispatch(clearError());
                        if (
                            successArg &&
                            successArg.bins &&
                            successArg.bins.stocks &&
                            successArg.bins.stocks.length > 0
                        ) {
                            const reads = successArg.bins.stocks.map((symbol: string) => ({
                                readAllBins: true,
                                key: {
                                    namespace: 'test',
                                    setName: 'stocks',
                                    userKey: symbol
                                }
                            }));
                            await dispatch(
                                doPerformBatchGet({
                                    batchKeys: reads,
                                    onSuccess: (res?: SimpleResponse) => {
                                        const endpoint = '/v1/batch';
                                        if (res) {
                                            dispatch(
                                                addAPICall(
                                                    'POST',
                                                    endpoint,
                                                    true,
                                                    JSON.stringify(res, null, 4),
                                                    JSON.stringify(reads, null, 4)
                                                )
                                            );

                                            dispatch(setActiveGroupStocks(res));
                                            dispatch(clearError());
                                        }
                                    },
                                    onError: (res?: RestClientError) => {

                                        if (isAuthError(res)) {
                                            dispatch(setErrorMessage('Not authenticated to use API'));
                                            dispatch(push('/login'));
                                            return;
                                        }
                                        const endpoint = '/v1/batch';
                                        dispatch(
                                            addAPICall(
                                                'POST',
                                                endpoint,
                                                false,
                                                res && res.message ? res.message : '',
                                                JSON.stringify(reads, null, 4)
                                            )
                                        );
                                        dispatch(
                                            setErrorMessage(
                                                `Failed to fetch stocks for: ${portfolioName}`
                                            )
                                        );
                                    }
                                })
                            );
                        }
                    }
                }
            };
            dispatch(
                doGetRecordNamespaceSetKey({
                    key: portfolioName,
                    namespace: process.env.REACT_APP_STOCK_NS!,
                    set: process.env.REACT_APP_PORTFOLIOS_SET!,
                    onSuccess: (record?: Record) => {
                        const set = process.env.REACT_APP_PORTFOLIOS_SET;
                        const namespace = process.env.REACT_APP_STOCK_NS;
                        const endpoint = `/v1/kvs/${namespace}/${set}/${portfolioName}`;
                        dispatch(clearError());
                        dispatch(
                            addAPICall('GET', endpoint, true, JSON.stringify(record, null, 4))
                        );
                    },
                    onError: (res?: RestClientError) => {
                        if (isAuthError(res)) {
                            dispatch(setErrorMessage('Not authenticated to use API'));
                            dispatch(push('/login'));
                            return;
                        }
                        const set = process.env.REACT_APP_PORTFOLIOS_SET;
                        const namespace = process.env.REACT_APP_STOCK_NS;
                        const endpoint = `/v1/kvs/${namespace}/${set}/${portfolioName}`;
                        dispatch(
                            addAPICall(
                                'GET',
                                endpoint,
                                false,
                                res && res.message ? res.message : ''
                            )
                        );
                        dispatch(setErrorMessage(`No portfolio named ${portfolioName} found`));
                    },
                    extender
                })
            );
        },
        doSetActiveStock: (symbol: string) => {
            const extender: ExtenderFunctions<Record, Error> = {
                success: {
                    after: async (successArg: Record) => {
                        if (
                            successArg &&
                            successArg.bins &&
                            successArg.bins.dates &&
                            successArg.bins.dates.length > 0
                        ) {
                            const reads = successArg.bins.dates.slice(-5).map((date: number) => ({
                                readAllBins: true,
                                key: {
                                    namespace: 'test',
                                    setName: 'stocks',
                                    userKey: symbol + '-' + String(date)
                                }
                            }));
                            await dispatch(
                                doPerformBatchGet({
                                    batchKeys: reads,
                                    onSuccess: (res?: SimpleResponse) => {
                                        if (res) {
                                            dispatch(clearError());
                                            const endpoint = '/v1/batch';
                                            dispatch(
                                                addAPICall(
                                                    'POST',
                                                    endpoint,
                                                    true,
                                                    JSON.stringify(res, null, 4),
                                                    JSON.stringify(reads, null, 4)
                                                )
                                            );
                                            dispatch(setActiveStockEntries(res));
                                        }
                                    },
                                    onError: (res?: RestClientError) => {
                                        if (isAuthError(res)) {
                                            dispatch(setErrorMessage('Not authenticated to use API'));
                                            dispatch(push('/login'));
                                            return;
                                        }
                                        const endpoint = '/v1/batch';
                                        dispatch(
                                            addAPICall(
                                                'POST',
                                                endpoint,
                                                false,
                                                res && res.message ? res.message : '',
                                                JSON.stringify(reads, null, 4)
                                            )
                                        );
                                        dispatch(
                                            setErrorMessage(
                                                `Unable to fetch historical data for ${symbol}.`
                                            )
                                        );
                                    }
                                })
                            );
                        }
                    }
                }
            };
            const successFunc = (record?: Record) => {
                if (record) {
                    const key = symbol;
                    const ns = process.env.REACT_APP_STOCK_NS;
                    const set = process.env.REACT_APP_STOCKS_SET;
                    const endpoint = `/v1/kvs/${ns}/${set}/${key}`;
                    dispatch(clearError());
                    dispatch(addAPICall('GET', endpoint, true, JSON.stringify(record, null, 4)));
                    dispatch(setActiveStockBins(record.bins));
                }
            };
            const errorFunc = (res?: RestClientError) => {
                if (isAuthError(res)) {
                    dispatch(setErrorMessage('Not authenticated to use API'));
                    dispatch(push('/login'));
                    return;
                }
                const key = symbol;
                const ns = process.env.REACT_APP_STOCK_NS;
                const set = process.env.REACT_APP_STOCKS_SET;
                const endpoint = `/v1/kvs/${ns}/${set}/${key}`;
                dispatch(addAPICall('GET', endpoint, false, res && res.message ? res.message : ''));
                dispatch(setErrorMessage(`Unable to find information about ${symbol}`));
            };

            dispatch(setStockCountFilter('5'));
            dispatch(setActiveStockSymbol(symbol));
            dispatch(
                doGetRecordNamespaceSetKey({
                    key: symbol,
                    namespace: process.env.REACT_APP_STOCK_NS!,
                    set: process.env.REACT_APP_STOCKS_SET!,
                    onSuccess: successFunc,
                    onError: errorFunc,
                    extender
                })
            );
        },
        doClearActiveStock: () => {
            dispatch(clearActiveStock());
        },
        doClearPortfolioStocks: () => {
            dispatch(clearActiveGroupStocks());
        }
    };
}

const PortfolioViewPage = connect(
    stateToProps,
    dispatchToProps
)(StockComponent);

export default PortfolioViewPage;
