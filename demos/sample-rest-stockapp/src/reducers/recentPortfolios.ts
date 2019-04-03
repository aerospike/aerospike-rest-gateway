import {SET_ACTIVE_STOCK_BINS} from '../actions/portfolios';

export function recentPortfolios(state: string[] = [], action: any) {
    switch (action.type) {
        case SET_ACTIVE_STOCK_BINS: {
            return action.portfolios;
        }
        default: {
            return state;
        }
    }
}
