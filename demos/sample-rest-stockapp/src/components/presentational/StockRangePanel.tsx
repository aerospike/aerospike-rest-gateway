import * as React from 'react';
import StockCountButton from './StockCountButton';

interface StockRangePanelProps {
    loadDailyEntries: (count: number, filter: string) => void;
    active_filter: string;
    max_count: number;
}

export class StockRangePanel extends React.PureComponent<
StockRangePanelProps> {


    public render() {
        return (
            <div style={{textAlign: 'center'}}>
            <StockCountButton 
                active_filter={this.props.active_filter}
                filter="5"
                onClick={this.props.loadDailyEntries}
                count={5}
            >5 days
            </StockCountButton>
            <StockCountButton 
                active_filter={this.props.active_filter}
                filter="100"
                onClick={this.props.loadDailyEntries}
                count={100}
            >100 days
            </StockCountButton>
            <StockCountButton 
                active_filter={this.props.active_filter}
                filter="500"
                onClick={this.props.loadDailyEntries}
                count={500}
            >500 days
            </StockCountButton>
            <StockCountButton 
                active_filter={this.props.active_filter}
                filter="ALL"
                onClick={this.props.loadDailyEntries}
                count={this.props.max_count}
            >All days
            </StockCountButton>



            </div>
        );
    }
}
