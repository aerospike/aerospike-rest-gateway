import * as I from 'immutable';
import { Record } from '../generated/core/api';
import SimpleResponse from '../generated/core/types/SimpleResponse';

export const SET_ACTIVE_STOCK_ENTRIES = 'SET_ACTIVE_STOCK_ENTRIES';
export const SET_STOCK_LIST = 'SET_STOCK_LIST';

export function setActiveStockEntries(activeStockResponse: SimpleResponse) {
    let activeStockEntries: Array<Map<string, any>> = [];
    const dataPoints = I.fromJS(activeStockResponse);

    if (dataPoints) {
        activeStockEntries = dataPoints
            .filter((stockDate: any) => stockDate.getIn(['record', 'bins']) != null)
            .toArray()
            .map((stockDate: any) => stockDate.getIn(['record', 'bins']).toJS());
    }

    return {
        type: SET_ACTIVE_STOCK_ENTRIES,
        stockEntries: activeStockEntries
    };
}

export function setStockList(stockListRecord: Record) {
    let symbols: string[] = [];
    if (stockListRecord.bins && stockListRecord.bins.symbols) {
        symbols = stockListRecord.bins.symbols;
    }
    return {
        type: SET_STOCK_LIST,
        stockList: symbols
    };
}
