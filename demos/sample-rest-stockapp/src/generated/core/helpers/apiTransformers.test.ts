import SimpleResponse from '../types/SimpleResponse';
import { xformApiResponse } from './apiTransformers';

describe('apiTransformers', () => {
    let response: Response;
    let simpleResponse: SimpleResponse;
    let OgResponse = Response;
    beforeEach(async () => {
        response = new Response('my body', { status: 200, statusText: 'rude bwoy' });
        simpleResponse = await xformApiResponse(response);
    });
    it('Validates response', () => {
        expect(response).toBe(response);
        expect(response).toBeInstanceOf(Response);
    });
    it('Validates transformatin for SimpleResponse', () => {
        expect(simpleResponse).toMatchObject({
            bodyText: 'my body',
            statusText: 'rude bwoy',
        });
    });
    it('does not tranform things of which it does not know', async () => {
        let xformed = await xformApiResponse(1);
        expect(xformed).toBe(1);
        const fourObj = { four: 'four' };
        xformed = await xformApiResponse(fourObj);
        expect(xformed).toBe(fourObj);
        expect(xformed).toMatchObject({ four: 'four' });
    });
    it('does not tranform things of which it does not know name collision edition', async () => {
        //not an actual response
        class Response {
            constructor(public amIAResponse = 'No') {}
        }
        const resp = new Response();
        const xformed = await xformApiResponse(resp);
        expect(xformed).toBe(resp);
        expect(xformed).toBeInstanceOf(Response);
        expect(xformed).not.toBeInstanceOf(OgResponse);
        expect(xformed.amIAResponse).toBe('No');
    });
});
