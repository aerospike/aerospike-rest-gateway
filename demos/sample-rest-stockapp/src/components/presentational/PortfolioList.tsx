import * as React from 'react';
import { Anchor } from './Anchor';
import { List, ListItem } from '@material-ui/core';

interface PortfolioListProps {
    portfolios: string[];
    viewPortfolioDetails: (name: string) => void;
}

export class PortfolioList extends React.PureComponent<PortfolioListProps> {
    public render() {
        if (!this.props.portfolios) {
            return null;
        }
        let portfolioEntries = this.props.portfolios.map(
            (name: string, index: number) => {
                return (
                <ListItem key={`portfolio${index}`}>
                <Anchor onClick={ e => this.props.viewPortfolioDetails(name)}>{name}</Anchor>
                </ListItem>);
            }
        );
        return (
            <List>
                {portfolioEntries}
            </List>
        );
    }
}
