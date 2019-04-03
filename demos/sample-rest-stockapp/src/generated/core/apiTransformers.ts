import SimpleResponse from './types/SimpleResponse';

async function xformResponseObject(res: Response): Promise<SimpleResponse> {
    const headers = {};

    res.headers.forEach((v, k) => {
        headers[k] = v;
    });

    return {
        url: res.url,
        type: res.type,
        redirected: res.redirected,
        ok: res.ok,
        status: res.status,
        statusText: res.statusText,
        bodyText: await res.text(),
        headers,
    };
}

export async function xformApiResponse<T>(response: T): Promise<any> {
    if (response instanceof Response) {
        return await xformResponseObject(response);
    }
    return response;
}
