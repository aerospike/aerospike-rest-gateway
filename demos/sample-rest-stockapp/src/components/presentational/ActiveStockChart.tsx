import * as React from 'react';
import { LineChart, Line, XAxis, YAxis, Tooltip, Legend, CartesianGrid } from 'recharts';
import { StockRangePanel } from './StockRangePanel';

export interface ActiveStockChartProps {
    dailyStockEntries: Array<Map<string, any>>;
    loadDailyEntries: (count: number, filter: string) => void;
    max: number;
    active_filter: string;
}

export class ActiveStockChart extends React.PureComponent<ActiveStockChartProps> {
    public render() {
        let data = this.props.dailyStockEntries;

        return (
            <div>
            <LineChart width={500} height={300} data={data}
            style={{margin: 'auto'}}>
                <XAxis dataKey="date" tickCount={4}/>
                <YAxis padding={{top: 100, bottom: 0}} domain={['dataMin', 'dataMax']}/>
                <Tooltip itemStyle={{color: 'black', height: 45}} position={{x: 100, y: 0}} />
                <Legend />
                <CartesianGrid strokeDasharray="3 3"/>
                <Line 
                    isAnimationActive={false} 
                    dataKey="close"
                    dot={false}
                    name="Closing Price"
                    stroke="#9b1217"
                />
            </LineChart>
            <StockRangePanel 
                max_count={this.props.max}
                active_filter={this.props.active_filter}
                loadDailyEntries={this.props.loadDailyEntries}
            />
        </div>
        );

    }
}
