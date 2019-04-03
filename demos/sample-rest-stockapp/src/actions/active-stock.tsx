import * as I from 'immutable';
import SimpleResponse from '../generated/core/types/SimpleResponse';

export const SET_ACTIVE_STOCK_SYMBOL = 'SET_ACTIVE_STOCK_SYMBOL';
export const SET_ACTIVE_STOCK_BINS = 'SET_ACTIVE_STOCK_BINS';
export const SET_ACTIVE_GROUP_STOCKS = 'SET_ACTIVE_GROUP_STOCKS';
export const SET_STOCK_COUNT_FILTER = 'SET_STOCK_COUNT_FILTER';
export const CLEAR_ACTIVE_STOCK = 'CLEAR_ACTIVE_STOCK';
export const CLEAR_ACTIVE_GROUP_STOCKS = 'CLEAR_ACTIVE_GROUP_STOCKS';

export function clearActiveStock(): any {
    return {
        type: CLEAR_ACTIVE_STOCK
    };
}
export function setActiveStockSymbol(symbol: string): any {
    return {
        type: SET_ACTIVE_STOCK_SYMBOL,
        stock: symbol
    };
}

export function setActiveStockBins(bins: Map<string, any>): any {
    return {
        type: SET_ACTIVE_STOCK_BINS,
        bins: bins
    };
}

export function setStockCountFilter(filter: string): any {
    return {
        type: SET_STOCK_COUNT_FILTER,
        filter
    };
}

export function clearActiveGroupStocks(): any {
    return {
        type: CLEAR_ACTIVE_GROUP_STOCKS
    };
}

export function setActiveGroupStocks(stocksResponse: SimpleResponse): any {
    let activeGroupStocks: Array<Map<string, any>> = [];
    const stocks = I.fromJS(stocksResponse);

    if (stocks) {
        activeGroupStocks = stocks
            .filter((stockRecord: any) => stockRecord.getIn(['record', 'bins']) != null)
            .toArray()
            .map((stockRecord: any) => stockRecord.getIn(['record', 'bins']));
    }
    return {
        type: SET_ACTIVE_GROUP_STOCKS,
        stocks: activeGroupStocks
    };
}
