export default interface SimpleResponse {
    readonly headers: { [key: string]: string };
    readonly ok: boolean;
    readonly redirected: boolean;
    readonly status: number;
    readonly statusText: string;
    readonly type: ResponseType;
    readonly url: string;
    readonly bodyText: string;
}
