import { Card, CardContent, Typography } from '@material-ui/core';
import * as React from 'react';
import { connect } from 'react-redux';
import { Action } from 'redux';
import { ThunkDispatch } from 'redux-thunk';
import { setStockCountFilter } from '../../actions/active-stock';
import { addAPICall } from '../../actions/apiCall';
import { clearError, setErrorMessage } from '../../actions/errors';
import { setActiveStockEntries } from '../../actions/stockentries';
import { StoreState } from '../../core/types/StoreState';
import { doPerformBatchGet } from '../../generated/core/actions/batchReadOperationsActionCreators';
import { RestClientError } from '../../generated/core/api';
import SimpleResponse from '../../generated/core/types/SimpleResponse';
import { ActiveStockChart } from '../presentational/ActiveStockChart';

export interface ActiveStockPanelProps {
    activeStockBins?: Map<string, any>;
    active_filter: string;
    dailyStockEntries: Array<Map<string, any>>;
    loadDailyEntries: (dates: number[], count: number, symbol: string, filter: string) => void;
    setActiveButtonFilter: (filter: string) => void;
}

export class ActiveStockComponent extends React.PureComponent<ActiveStockPanelProps> {
    public render() {
        let activeStockBins = this.props.activeStockBins;
        let activeStockEntries = this.props.dailyStockEntries;
        let stockHeader: JSX.Element | null = null;
        let stockChartElement: JSX.Element | null = null;
        if (activeStockBins && activeStockBins !== undefined) {
            const maxRange = activeStockBins['dates'] ? activeStockBins['dates'].length : 5;
            const symbol = activeStockBins['symbol'];
            stockHeader = (
                <Typography
                    style={{ textAlign: 'center' }}
                    variant="display1"
                    component="h2"
                    gutterBottom
                >
                    {symbol}
                </Typography>
            );
            if (activeStockEntries && activeStockEntries.length > 0) {
                stockChartElement = (
                    <CardContent>
                        <ActiveStockChart
                            active_filter={this.props.active_filter}
                            dailyStockEntries={activeStockEntries}
                            max={maxRange}
                            loadDailyEntries={(count: number, filter: string) =>
                                this.loadNEntries(count, filter)
                            }
                        />
                    </CardContent>
                );
            }
        } else {
            return null;
        }

        return (
            <Card>
                {stockHeader}
                {stockChartElement}
            </Card>
        );
    }
    public loadNEntries(count: number, filter: string) {
        if (this.props.activeStockBins) {
            this.props.loadDailyEntries(
                this.props.activeStockBins['dates'],
                count,
                this.props.activeStockBins['symbol'],
                filter
            );
        }
    }
}

function stateToProps(state: StoreState) {
    return {
        activeStockBins: state.activeStockBins,
        dailyStockEntries: state.dailyStockEntries,
        active_filter: state.activeStockFilter
    };
}

function dispatchToProps(dispatch: ThunkDispatch<StoreState, void, Action>) {
    return {
        setActiveButtonFilter: (filter: string) => {
            dispatch(setStockCountFilter(filter));
        },
        loadDailyEntries: (dates: number[], count: number, symbol: string, filter: string) => {
            dispatch(setStockCountFilter(filter));
            const reads = dates.slice(-count).map((date: number) => ({
                readAllBins: true,
                key: {
                    namespace: 'test',
                    setName: 'stocks',
                    userKey: symbol + '-' + String(date)
                }
            }));
            dispatch(
                doPerformBatchGet({
                    batchKeys: reads,
                    onSuccess: (res?: SimpleResponse) => {
                        dispatch(clearError());
                        dispatch(
                            addAPICall('POST', '/v1/batch', true, JSON.stringify(res, null, 4))
                        );
                        if (res) {
                            dispatch(setActiveStockEntries(res));
                        }
                    },
                    onError: (res?: RestClientError) => {
                        dispatch(setErrorMessage('Failed to fetch daily stock Information'));

                        dispatch(
                            addAPICall(
                                'POST',
                                '/v1/batch',
                                false,
                                res && res.message ? res.message : ''
                            )
                        );
                    }
                })
            );
        }
    };
}

const ActiveStockPanel = connect(
    stateToProps,
    dispatchToProps
)(ActiveStockComponent);

export { ActiveStockPanel };
