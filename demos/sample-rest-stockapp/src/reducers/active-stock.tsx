import { CLEAR_ACTIVE_STOCK, SET_ACTIVE_STOCK_BINS, SET_ACTIVE_GROUP_STOCKS, CLEAR_ACTIVE_GROUP_STOCKS } from '../actions/active-stock';

export function activeStockSymbol(state: string = '', action: any) {
    switch (action.type) {
        case SET_ACTIVE_STOCK_BINS: {

            return action.bins.symbol;
        }
        case CLEAR_ACTIVE_STOCK: {
            return '';
        }
        default:
            return state;
    }
}

export function activeStockBins(state: Map<string, any>, action: any) {
    if (state === undefined ) {
        return null;
    }

    switch (action.type) {
        case SET_ACTIVE_STOCK_BINS: {
            return action.bins;
        }
        case CLEAR_ACTIVE_STOCK: {
            return null;
        }
        default: {
            return state;
        }
    }
}

export function activeGroupStocks(state: Array<Map<string, any>> = [], action: any) {
    switch (action.type) {
        case SET_ACTIVE_GROUP_STOCKS: {
            return action.stocks;
        }
        case CLEAR_ACTIVE_GROUP_STOCKS: {
            return [];
        }
        default: {
            return state;
        }
    }
}
