export const SET_ACTIVE_STOCK_BINS = 'SET_RECENT_PORTFOLIOS';

export function setRecentPortfolios(portfolios: string[]): any {
    return {
        type: SET_ACTIVE_STOCK_BINS,
        portfolios: portfolios
    };
}
