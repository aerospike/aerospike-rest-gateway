import { AppBar, IconButton, Toolbar, Typography } from '@material-ui/core';
// import Fastfood from '@material-ui/icons/Fastfood';
import TrendingUp from '@material-ui/icons/TrendingUp';
import * as React from 'react';
import { Link, LinkProps } from 'react-router-dom';

// import theme from '../../core/theme';
// import { IconBox } from './IconBox';

export interface HeaderProps {
    title?: string;
}

const LinkHome = (props: Partial<LinkProps>) => <Link to="/" {...props} />;

export default class Header extends React.PureComponent<HeaderProps> {
    public render() {
        return (
            <AppBar position="static" color="primary">
                <Toolbar>
                    <IconButton color="inherit" aria-label="Home" component={LinkHome}>
                        <TrendingUp />
                        {/* <IconBox
                            width={64}
                            iconProps={{
                                p: '10px',
                                src: theme.images.cook,
                                width: 64,
                            }}
                        />{' '} */}
                    </IconButton>
                    <Typography variant="title" color="inherit" noWrap>
                        {this.props.title}
                    </Typography>
                </Toolbar>
            </AppBar>
        );
    }
}
