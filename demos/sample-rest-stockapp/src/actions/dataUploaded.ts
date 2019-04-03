export const SET_ROWS_PROCESSED = 'SET_ROWS_PROCESSED';
export const SET_ROW_COUNT = 'SET_ROW_COUNT';
export const SET_ROW_PROCESS_STATUS = 'SET_ROW_PROCESS_STATUS';
export const SET_ROW_PROCESSING_COMPLETED = 'SET_ROW_PROCESSING_COMPLETED';

export function doSetRowsProcessed(count: number) {
    return {
        type: SET_ROWS_PROCESSED,
        count
    };
}

export function doSetTotalRowCount(count: number) {
    return {
        type: SET_ROW_COUNT,
        count
    };
}

export function doSetRowProcessingStatus(inProgress: boolean) {
    return {
        type: SET_ROW_PROCESS_STATUS,
        inProgress
    };
}

export function setRowProcessingComplete(completed: boolean) {
    return {
        type: SET_ROW_PROCESSING_COMPLETED,
        completed
    };
}
