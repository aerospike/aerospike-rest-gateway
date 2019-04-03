import { SET_ACTIVE_STOCK_ENTRIES, SET_STOCK_LIST } from '../actions/stockentries';
import { SET_STOCK_COUNT_FILTER } from '../actions/active-stock';

export function dailyStockEntries(state: Map<string, any>[] = [], action: any) {
    switch (action.type) {
        case SET_ACTIVE_STOCK_ENTRIES:
            return action.stockEntries;
        default:
            return state;
    }
}

export function activeStockFilter(state: String = '5', action: any) {
    switch (action.type) {
        case (SET_STOCK_COUNT_FILTER): {
            return action.filter;
        }
        default: {
            return state;
        }
    }
}

export function stockList(state: string[] = [], action: any) {

    switch (action.type) {
        case (SET_STOCK_LIST): {
            return action.stockList;
        }
        default: {
            return state;
        }
    }
}
