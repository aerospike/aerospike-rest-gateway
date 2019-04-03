export enum CallType {
    apiCall = 'apiCall',
    success = 'success',
    error = 'error'
}

export default interface ExtenderFunctions<TPayload, TErrorPayload> {
    apiCall?: {
        before?: (_?: any) => any | void;
        after?: (args: any) => any | void;
    };
    success?: {
        before?: (args: any) => any | void;
        after?: (args: any) => any | void;
    };
    error?: {
        before?: (args: any) => any | void;
        after?: (args: any) => any | void;
    };
}

export interface WrappedFunctions {
    apiCall: Function;
    success: Function;
    error: Function;
}

export class ApiActionCreatorExtender<TPayload, TErrorPayload> {
    constructor(
        private fns: WrappedFunctions,
        public successCb?: (successArg?: TPayload) => any | void,
        public errorCb?: (errorArg?: TErrorPayload) => any | void,
        private extenders?: ExtenderFunctions<TPayload, TErrorPayload>
    ) {}

    private async doWrappedCall(
        callType: CallType,
        args?: any[],
        wrapperArg?: TPayload | TErrorPayload
    ) {
        const xtnd = this.extenders;
        const fn = this.fns[callType];

        // Short circuit if we don't have to wrap
        if (!(xtnd && xtnd[callType])) {
            return await fn.apply(null, args);
        }

        const wrapperFns = xtnd![callType];
        let ret: any;
        if (wrapperFns!.before) {
            await wrapperFns!.before!.call(null, wrapperArg as TPayload);
        }
        ret = fn.apply(null, args);
        if (wrapperFns!.after) {
            if (callType === CallType.apiCall) {
                wrapperArg = ret;
            }
            await wrapperFns!.after!.call(null, wrapperArg as TPayload);
        }
        return ret;
    }

    public async apiCall(args?: any[]) {
        return await this.doWrappedCall(CallType.apiCall, args);
    }

    public async success(args?: any[], wrapperFcnArg?: TPayload) {
        const ret = await this.doWrappedCall(CallType.success, args, wrapperFcnArg);
        if (this.successCb) {
            await this.successCb.call(null, wrapperFcnArg);
        }
        return ret;
    }

    public async error(args?: any[], wrapperFcnArg?: TErrorPayload) {
        const ret = await this.doWrappedCall(CallType.error, args, wrapperFcnArg);
        if (this.errorCb) {
            await this.errorCb.call(null, wrapperFcnArg);
        }
        return ret;
    }
}
