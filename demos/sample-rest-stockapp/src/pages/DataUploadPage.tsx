import { Typography } from '@material-ui/core';
import * as React from 'react';
import { connect } from 'react-redux';
import { Action } from 'redux';
import { ThunkDispatch } from 'redux-thunk';
import { doLoadData } from '../actions/dataloader';
import { setRowProcessingComplete } from '../actions/dataUploaded';
import { clearError } from '../actions/errors';
import { ContentBox } from '../components/presentational/ContentBox';
import DataUploadForm from '../components/presentational/DataUploadForm';
import DataUploadState from '../core/types/DataUploadState';
import ErrorState from '../core/types/ErrorState';
import { StoreState } from '../core/types/StoreState';
import { TypedMap } from '../core/types/TypedMap';
import { doCreateRecordNamespaceSetKey } from '../generated/core/actions/keyValueOperationsActionCreators';
import { LastApiCall } from '../generated/core/state/ApiState';
import Page from './Page';

interface DataUploadProps {
    lastResponse?: TypedMap<LastApiCall>;
    errorStatus: ErrorState;
    doCreateDailyStockEntry: (rows: string[]) => void;
    doCreateStockEntry: (symbol: string, entryDates: string[]) => void;
    doCreateStockSymbolList: (symbols: string[]) => void;
    dataUpload: DataUploadState;
    clearErrorMessage: () => void;
}

export class DataUploadComponent extends React.PureComponent<DataUploadProps> {
    constructor(props: DataUploadProps) {
        super(props);
        props.clearErrorMessage();
    }
    public render() {
        return (
            <Page
                lastResponse={this.props.lastResponse}
                errorStatus={this.props.errorStatus}
                excludeAPIPanel={true}
            >
                <ContentBox>
                    <Typography align="center" component="h2" variant="display1" gutterBottom>
                        Upload your own stock data!
                    </Typography>
                    <Typography>
                        The sample dataset for this app can be found at:
                        <a
                            href="https://www.kaggle.com/camnugent/sandp500#all_stocks_5yr.csv"
                            target="_blank"
                        >
                            S&amp;P 500 data
                        </a>
                        .
                    </Typography>
                    <DataUploadForm
                        dataUpload={this.props.dataUpload}
                        createSymbolList={this.props.doCreateStockSymbolList}
                        createStockEntry={this.props.doCreateStockEntry}
                        createDailyStockEntry={this.props.doCreateDailyStockEntry}
                    />
                </ContentBox>
            </Page>
        );
    }
}

function stateToProps(state: StoreState) {
    return {
        lastResponse: state.api.get('last'),
        errorStatus: state.errorState,
        dataUpload: state.dataUpload
    };
}

function dispatchToProps(dispatch: ThunkDispatch<StoreState, void, Action>) {
    return {
        clearErrorMessage: () => {
            dispatch(clearError());
        },
        doCreateDailyStockEntry: (rows: string[]) => {
            dispatch(setRowProcessingComplete(false));
            dispatch(doLoadData({ rows }));
        },
        doCreateStockEntry: (symbol: string, entryDates: string[]) => {
            dispatch(
                doCreateRecordNamespaceSetKey({
                    key: symbol,
                    namespace: process.env.REACT_APP_STOCK_NS!,
                    set: process.env.REACT_APP_STOCKS_SET!,
                    bins: {
                        dates: entryDates,
                        symbol: symbol
                    }
                })
            );
        },
        doCreateStockSymbolList: (symbols: string[]) => {
            dispatch(
                doCreateRecordNamespaceSetKey({
                    key: process.env.REACT_APP_STOCKS_LIST_KEY!,
                    namespace: process.env.REACT_APP_STOCK_NS!,
                    set: process.env.REACT_APP_STOCKS_SET!,
                    bins: {
                        [process.env.REACT_APP_STOCKS_SYMBOLS_BIN!]: symbols
                    }
                })
            );
        }
    };
}

const DataUploadPage = connect(
    stateToProps,
    dispatchToProps
)(DataUploadComponent);

export default DataUploadPage;
