import { SET_ACTIVE_CALL_NUMBER, SET_LAST_API_CALL } from '../actions/apiCall';
import { APICallState } from '../core/types/ApiCallState';

export function apiCalls(state: APICallState | undefined, action: any) {
    if (state === undefined) {
        return {
            activeCallNumber: -1,
            apiCalls: []
        };
    }
    switch (action.type) {
        case SET_LAST_API_CALL: {
            return {
                ...state,
                apiCalls: [action.info, ...state.apiCalls].slice(0, 15)
            };
        }
        case SET_ACTIVE_CALL_NUMBER: {
            return {
                ...state,
                activeCallNumber: action.callNumber
            };
        }
        default: {
            return state;
        }
    }
}
