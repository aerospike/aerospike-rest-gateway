export const SET_ERROR_MESSAGE = 'SET_ERROR_MESSAGE';
export const CLEAR_ERROR = 'CLEAR_ERROR';

export function setErrorMessage(message: string) {
    return {
        type: SET_ERROR_MESSAGE,
        errorMessage: message
    };
}

export function clearError() {
    return {
        type: CLEAR_ERROR,
    };
}
