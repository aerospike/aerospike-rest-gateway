/*
 * Load a bunch of data
 */

import { RouterAction } from 'connected-react-router';
import { Action } from 'redux';
import { ThunkDispatch } from 'redux-thunk';
import { StoreState } from '../core/types/StoreState';
import { CreateRecordNamespaceKeyAction } from '../generated/core/actions/keyValueOperationsActions';
import * as gapi from '../generated/core/api';
import {
    doSetRowProcessingStatus,
    doSetRowsProcessed,
    doSetTotalRowCount,
    setRowProcessingComplete
} from './dataUploaded';
import { getConfiguration } from '../generated/core/helpers/apiConfiguration';

const conf = getConfiguration();
const api = new gapi.KeyValueOperationsApi(conf, process.env.REACT_APP_API_BASE || '');


interface DoLoadDataArgs {
    rows: string[];
}
export const doLoadData = ({ rows }: DoLoadDataArgs) => async (
    dispatch: ThunkDispatch<StoreState, void, CreateRecordNamespaceKeyAction | RouterAction>
) => {
    await setRowCount(dispatch, rows.length);
    await setInProgress(dispatch, true);
    await setProcessingComplete(dispatch, false);
    const inflight = 100;
    let calls = [];
    for (let i = 1; i < 1 + inflight; i++) {
        calls.push(writeData(rows, i, inflight, dispatch));
    }
    for (let i = 0; i < inflight; i++) {
        await calls[i];
    }
    await setInProgress(dispatch, false);
    await setProcessingComplete(dispatch, true);
};

async function setRowCount(dispatch: ThunkDispatch<StoreState, void, Action>, count: number) {
    dispatch(doSetTotalRowCount(count));
}

async function setInProgress(dispatch: ThunkDispatch<StoreState, void, Action>, complete: boolean) {
    dispatch(doSetRowProcessingStatus(complete));
}

async function setProcessingComplete(
    dispatch: ThunkDispatch<StoreState, void, Action>,
    complete: boolean
) {
    dispatch(setRowProcessingComplete(complete));
}

async function writeData(
    rows: string[],
    index: number,
    offset: number,
    dispatch: ThunkDispatch<StoreState, void, Action>
) {
    for (let i = index; i < rows.length; i += offset) {
        const row = rows[i];

        /* Only update every 100 rows */
        if (i % 100 === 0) {
            dispatch(doSetRowsProcessed(i));
        }

        const cols = row.split(',');
        const symbol: string = cols[6];
        const date: string = cols[0];
        // If any of the required fields are empty, just skip this row
        if (cols[1] === '' || cols[4] === '' || symbol === '' || date === '') {
            continue;
        }

        const open: number = parseFloat(cols[1]);
        const close: number = parseFloat(cols[4]);
        const uKey = symbol + '-' + date;
        const bins = {
            symbol: symbol,
            date: date,
            open: open,
            close: close
        };
        try {
            await api.createRecordNamespaceSetKey(
                bins,
                uKey,
                process.env.REACT_APP_STOCK_NS!,
                process.env.REACT_APP_STOCKS_SET!
            );
        } catch {}
    }
}
