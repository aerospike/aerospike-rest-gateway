import { CLEAR_ERROR, SET_ERROR_MESSAGE } from '../actions/errors';
import ErrorState from '../core/types/ErrorState';
const defaultState: ErrorState = {
    errorOccurred: false,
    errorMessage: ''
};

export function errors(state = defaultState, action: any): ErrorState {
    switch (action.type) {
        case SET_ERROR_MESSAGE: {
            return {
                errorOccurred: true,
                errorMessage: action.errorMessage
            };
        }
        case CLEAR_ERROR: {
            return {
                errorOccurred: false,
                errorMessage: ''
            };
        }
        default: {
            return state;
        }
    }
}
