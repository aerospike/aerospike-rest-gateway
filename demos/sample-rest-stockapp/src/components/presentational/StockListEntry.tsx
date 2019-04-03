import * as React from 'react';
import { Button, TableRow, TableCell } from '@material-ui/core';

export interface StockItemProps {
    doSetActiveStock: (stockSymbol: string) => void;
    symbol: string;
    daysOfData: number;
    active: boolean;
    dates: Array<string>;
}

export class StockListEntry extends React.PureComponent<StockItemProps> {
    public render() {
        let activeCell: JSX.Element;

        activeCell = (
        <Button disabled={this.props.active} onClick={() => this.props.doSetActiveStock(this.props.symbol)}>
            Set {this.props.symbol} as active
        </Button>);

        return (
            <TableRow>
                <TableCell>{this.props.symbol}</TableCell>
                <TableCell>{this.props.daysOfData}</TableCell>
                <TableCell>
                    {activeCell}
                </TableCell>
            </TableRow>
        );
    }
}

export default StockListEntry;
