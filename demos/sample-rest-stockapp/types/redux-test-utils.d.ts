declare module 'redux-test-utils' {
    import { AnyAction, Dispatch, Store } from 'redux';
    export function createMockStore(state?: any): mockStore<any>;

    export function createMockDispatch(): mockDispatch<any>;

    export interface mockStore<S> extends Store<S>, mockDispatch<AnyAction> {}

    export interface mockDispatch<S> {
        dispatch: Dispatch<AnyAction>;
        getActions: () => AnyAction[];
        getAction: (type: any) => AnyAction | undefined;
        isActionTypeDispatched: (type: any) => boolean;
        isActionDispatched: (action: AnyAction) => boolean;
    }
}
