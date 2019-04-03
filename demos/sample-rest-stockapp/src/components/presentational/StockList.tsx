import * as React from 'react';
import {Map} from 'immutable';
import { StockListEntry } from './StockListEntry';
import {Card, CardContent, Typography, Table, TableBody, TableHead, TableRow, TableCell} from '@material-ui/core';

export interface StockListProps {
    doSetActiveStock: (stockSymbol: string) => void;
    activeGroupStocks: Array<Map<string, any>>;
    activeStockSymbol: string;
    groupName?: string|null;
}

export class StockList extends React.PureComponent<StockListProps> {
    public render() {
        const stocks = this.props.activeGroupStocks;
        const groupTitle = this.props.groupName ? this.props.groupName : 'This Portfolio';
        let stocksValue: JSX.Element | null = null;
        if (stocks) {
            const stockRows = stocks
            .map((stockRecord: Map<string, any>, index: number) => {
                let dates = stockRecord.get('dates', []);
                let daysOfData: number = dates.size;
                let symbol: string = stockRecord.get('symbol');
                return <StockListEntry key={index} {...{daysOfData, symbol, dates,
                    doSetActiveStock: () => this.props.doSetActiveStock(symbol)}}
                    active={this.props.activeStockSymbol === symbol}
                />;
            });
            stocksValue = (
                <Card>
                    <Typography variant="display1" component="h2"
                    style={{textAlign: 'center'}} gutterBottom>Stocks in {groupTitle}</Typography>
                    <CardContent>
                    <Table>
                        <TableHead>
                            <TableRow>
                                <TableCell component="th" scope="col">Symbol</TableCell>
                                <TableCell component="th" scope="col" numeric>Days of data</TableCell>
                                <TableCell component="th" scope="col">Active</TableCell>
                            </TableRow>
                        </TableHead>
                        <TableBody>
                            {stockRows}
                        </TableBody>
                    </Table>
                    </CardContent>
                </Card>
            );
        }
        return stocksValue;
    }
}
