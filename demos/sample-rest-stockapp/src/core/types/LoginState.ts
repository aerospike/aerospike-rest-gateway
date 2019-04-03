import { TypedMap } from './TypedMap';

export interface AuthLoginStateFields {
    readonly errorValue?: TypedMap<Response>;
    readonly inProgress?: boolean;
    readonly successValue?: TypedMap<Response>;
}

export interface AuthStateFields {
    readonly login?: TypedMap<AuthLoginStateFields>;
}

// export const NoLoginStateDefaults: LoginStateBase = {
//     errorResponse: null,
//     inProgress: false,
//     token: null,
//     username: null,
//     authInfo: null,
// };

export type AuthState = TypedMap<AuthStateFields>;

//export const createLoginState = (init?: LoginStateFields): LoginState => createTypedMap(init);
