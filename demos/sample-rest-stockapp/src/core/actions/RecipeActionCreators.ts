import { RouterAction } from 'connected-react-router';
import { ThunkDispatch } from 'redux-thunk';
import { GetRolesAction } from '../../generated/core/actions/adminOperationsActions';
import { doDeleteRecordNamespaceSetKey } from '../../generated/core/actions/keyValueOperationsActionCreators';
import { doOperateNamespaceSetKey } from '../../generated/core/actions/operateOperationsActionCreators';
import { StoreState } from '../types/StoreState';

export const doDeleteRecipe = (recipeId: string, onSuccess?: () => any | void) => async (
    dispatch: ThunkDispatch<StoreState, void, GetRolesAction | RouterAction>
) => {
    return dispatch(
        doDeleteRecordNamespaceSetKey({
            key: recipeId,
            namespace: process.env.REACT_APP_RECIPES_NS!,
            set: process.env.REACT_APP_RECIPES_SET!,
            onSuccess: () =>
                dispatch(
                    doOperateNamespaceSetKey({
                        key: process.env.REACT_APP_RECENT_RECIPES_KEY!,
                        namespace: process.env.REACT_APP_RECIPES_NS!,
                        set: process.env.REACT_APP_RECIPES_SET!,
                        operations: [
                            {
                                opValues: {
                                    bin: 'ids',
                                    value: recipeId,
                                    listReturnType: 'NONE',
                                },
                                operation: 'LIST_REMOVE_BY_VALUE',
                            },
                        ],
                        onSuccess,
                    })
                ),
        })
    );
};
