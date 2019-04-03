import { Button, LinearProgress, Typography } from '@material-ui/core';
import * as React from 'react';
import { Link } from 'react-router-dom';
import DataUploadState from '../../core/types/DataUploadState';

interface DataUploadFormProps {
    createSymbolList: (symbols: string[]) => void;
    createStockEntry: (symbol: string, dates: string[]) => void;
    createDailyStockEntry: (symbols: string[]) => void;
    dataUpload: DataUploadState;
}

// A sample subset of stocks
const includedStocks = [
    'AAPL',
    'MSFT',
    'AMZN',
    'NFLX',
    'JNJ',
    'JPM',
    'FB',
    'XOM',
    'GOOG',
    'GOOGL',
    'BAC',
    'UNH',
    'PFE',
    'V',
    'VZ',
    'PG',
    'WFC',
    'CVX',
    'INTC',
    'T',
    'CSCO',
    'HD',
    'MRK',
    'KO',
    'MA'
];
const includedStockSet = new Set(includedStocks);

export default class DataUploadForm extends React.Component<DataUploadFormProps> {
    constructor(props: DataUploadFormProps) {
        super(props);
        this.handleChange = this.handleChange.bind(this);
    }

    handleChange(selectorFiles: FileList | null) {
        if (!selectorFiles) {
            return;
        }

        let reader = new FileReader();
        reader.onload = e => {
            this.handleStockText(reader.result);
        };
        reader.readAsText(selectorFiles[0]);
    }

    handleStockText(stockText: string | ArrayBuffer | null) {
        let stringStockText = stockText as string;
        // This data is : date,open,high,low,close,volume,Name
        //                  0 , 1,   2,   3 , 4   , 5    , 6
        if (stringStockText) {
            let rows: string[] = stringStockText.split('\n');

            const stockSymbolsSet = new Set<string>();
            const symbolsToDates: Map<string, string[]> = new Map<string, string[]>();
            rows.slice(1).forEach((rowText: string, idx) => {
                let cols = rowText.split(',');
                let symbol: string = cols[6];
                let date: string = cols[0];
                if (symbolsToDates.has(symbol)) {
                    let dates = symbolsToDates.get(symbol) as string[];
                    dates.push(date);
                } else {
                    symbolsToDates.set(symbol, [date]);
                }
                stockSymbolsSet.add(cols[6]);
            });

            let stockSymbols: string[] = Array.from(stockSymbolsSet).filter(symbol =>
                includedStockSet.has(symbol)
            );

            this.props.createSymbolList(stockSymbols);
            symbolsToDates.forEach((dates, symbol) => {
                if (includedStockSet.has(symbol)) {
                    this.props.createStockEntry(symbol, dates);
                }
            });
            rows = rows.filter(row => {
                return includedStockSet.has(row.split(',')[6]);
            });

            this.props.createDailyStockEntry(rows);
        }
    }

    public render() {
        let processingStatus: JSX.Element | null = null;
        if (this.props.dataUpload.inProgress) {
            const progress =
                (100 * this.props.dataUpload.rowsProcessed) / this.props.dataUpload.totalRows;
            processingStatus = (
                <div>
                    <Typography style={{ textAlign: 'center' }} gutterBottom>
                        {this.props.dataUpload.rowsProcessed} / {this.props.dataUpload.totalRows}
                    </Typography>
                    <LinearProgress variant="determinate" value={progress} />
                </div>
            );
        } else {
            if (this.props.dataUpload.completed) {
                processingStatus = (
                    <div>
                        <Typography
                            style={{ textAlign: 'center' }}
                            variant="subheading"
                            component="h2"
                        >
                            {' '}
                            Upload Complete
                        </Typography>
                        <Link to="/portfolio">Create your first portfolio!</Link>
                    </div>
                );
            }
        }

        return (
            <div>
                {processingStatus}
                <input
                    id="data-upload"
                    type="file"
                    onChange={e => this.handleChange(e.target.files)}
                    style={{ display: 'none' }}
                    disabled={this.props.dataUpload.inProgress || this.props.dataUpload.completed}
                />
                <div style={{ textAlign: 'center' }}>
                    <label htmlFor="data-upload">
                        <Button
                            variant="raised"
                            component="span"
                            disabled={
                                this.props.dataUpload.inProgress || this.props.dataUpload.completed
                            }
                        >
                            Upload CSV Data
                        </Button>
                    </label>
                </div>
            </div>
        );
    }
}
