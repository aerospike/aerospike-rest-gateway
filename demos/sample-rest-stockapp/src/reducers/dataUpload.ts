import { SET_ROWS_PROCESSED, SET_ROW_COUNT, SET_ROW_PROCESS_STATUS, SET_ROW_PROCESSING_COMPLETED } from '../actions/dataUploaded';

import DataUploadState from '../core/types/DataUploadState'; 

const initialState = {
    totalRows: 0,
    rowsProcessed: 0,
    inProgress: false,
    completed: false
};

export function dataUpload(state: DataUploadState = initialState, action: any) {
    switch (action.type) {
        case SET_ROWS_PROCESSED: {
            return {
                ...state,
                rowsProcessed: action.count
            };
        }
        case SET_ROW_COUNT: {
            return {
                ...state,
                totalRows: action.count
            };
        }
        case SET_ROW_PROCESS_STATUS: {
            return {
                ...state,
                inProgress: action.inProgress
            };
        }
        case SET_ROW_PROCESSING_COMPLETED: {
            return {
                ...state,
                completed: action.completed,
                inProgress: !action.completed
            };
        }
        default: {
            return state;
        }
    }
}
