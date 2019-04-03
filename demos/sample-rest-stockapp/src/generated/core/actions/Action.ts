import { Action } from 'redux';

export interface ActionWithPayload<TType, TPayload> extends Action<TType> {
    readonly payload: TPayload;
}
