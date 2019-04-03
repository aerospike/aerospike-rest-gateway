import { RouterState } from 'connected-react-router';
import { ApiState } from '../../generated/core/state/ApiState';
import  {APICallState}  from './ApiCallState';
import * as I from 'immutable';
import ErrorState from './ErrorState';
import DataUploadState from './DataUploadState';

export interface StoreState {
    readonly api: ApiState;
    readonly router: RouterState;
    readonly activeStockSymbol: string;
    readonly activeGroupStocks: Array<I.Map<string, any>>;
    readonly activeStockBins: Map<string, any>;
    readonly dailyStockEntries: Map<string, any>[];
    readonly activeStockFilter: string;
    readonly stockList: string[];
    readonly recentPortfolios: string[];
    readonly apiCallState: APICallState;
    readonly errorState: ErrorState;
    readonly dataUpload: DataUploadState;
}
