export interface APICallState {
    apiCalls: APICallInfo[];
    activeCallNumber: number;
}

export default interface APICallInfo {
    endpoint: string;
    method: string;
    callNumber: number;
    request: string;
    response: APIResponse;
}

interface APIResponse {
    success: boolean;
    payload: string;
}
