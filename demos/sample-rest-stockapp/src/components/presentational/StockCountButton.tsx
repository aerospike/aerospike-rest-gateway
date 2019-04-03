import * as React from 'react';
import { Button } from '@material-ui/core';

interface StockCountButtonProps {
    filter: string;
    active_filter: string;
    count: number;
    onClick: (amt: number, filter: string) => void;
}

export default class StockCountButton extends React.PureComponent<StockCountButtonProps> {
    public render() {
        const active = this.props.filter !== this.props.active_filter;
        return (
            <Button
                onClick={() => this.props.onClick(this.props.count, this.props.filter)}
                disabled={!active}
            >{this.props.children}
            </Button>
        );
    }
}
