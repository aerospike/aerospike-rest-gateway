export const SET_LAST_API_CALL = 'SET_LAST_API_CALL';
export const SET_ACTIVE_CALL_NUMBER = 'SET_ACTIVE_CALL_NUMBER';

let callNumber = 0;

export function addAPICall(method: string, endpoint: string, success: boolean, payload: string, request: string = ''): any {
    callNumber++;
    return {
        type: SET_LAST_API_CALL,
        info: {
            method,
            endpoint,
            callNumber,
            request,
            response: {
                success,
                payload
            }
        }
    };
}

export function setActiveCallNumber(callNumber: number): any {
    return {
        type: SET_ACTIVE_CALL_NUMBER,
        callNumber
    };
}
