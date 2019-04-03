import {
    createStyles,
    Grid,
    Paper,
    Theme,
    Typography,
    WithStyles,
    withStyles
} from '@material-ui/core';
import * as React from 'react';
import APICallPanel from '../components/containers/APICallPanel';
import Header from '../components/presentational/Header';
import ErrorState from '../core/types/ErrorState';
import { TypedMap } from '../core/types/TypedMap';
import { LastApiCall } from '../generated/core/state/ApiState';

export interface PageProps {
    backgroundImage?: string;
    pageTitle?: string;
    excludeAPIPanel?: boolean;
    lastResponse?: TypedMap<LastApiCall>;
    errorStatus: ErrorState;
}

const styles = (theme: Theme) =>
    createStyles({
        root: {
            flexGrow: 1
        },
        layout: {
            width: 'auto',
            marginLeft: theme.spacing.unit * 2,
            marginRight: theme.spacing.unit * 2,
            [theme.breakpoints.up(600 + theme.spacing.unit * 2 * 2)]: {
                // width: 1200,
                marginLeft: 'auto',
                marginRight: 'auto'
            }
        },
        paper: {
            marginTop: theme.spacing.unit * 3,
            marginBottom: theme.spacing.unit * 3,
            padding: theme.spacing.unit * 2,
            [theme.breakpoints.up(800 + theme.spacing.unit * 3 * 2)]: {
                marginTop: theme.spacing.unit * 6,
                marginBottom: theme.spacing.unit * 6,
                padding: theme.spacing.unit * 3
            }
        }
    });

interface PageState {}
class PageComponent extends React.PureComponent<WithStyles<typeof styles> & PageProps, PageState> {
    constructor(props: WithStyles<typeof styles> & PageProps) {
        super(props);
        this.state = {};
    }

    public render() {
        let errorBanner: JSX.Element | null = null;
        if (this.props.errorStatus.errorOccurred) {
            errorBanner = (
                <Grid item xs={12}>
                    <Paper>
                        <Typography
                            gutterBottom
                            color="error"
                            align="center"
                            style={{ height: '50px', paddingTop: '10px' }}
                        >
                            {this.props.errorStatus.errorMessage}
                        </Typography>
                    </Paper>
                </Grid>
            );
        }
        return (
            <div className={this.props.classes.root}>
                <Header title={this.props.pageTitle || 'REST Demo'} />
                <div className={this.props.classes.layout}>
                    <main className={this.props.classes.layout}>
                        <Grid container spacing={24} direction="row" justify="space-evenly">
                            {errorBanner}
                            <Grid item xs={10} md={7}>
                                <Paper className={this.props.classes.paper}>
                                    {this.props.children}
                                </Paper>
                            </Grid>
                            {!this.props.excludeAPIPanel && (
                                <Grid item xs={10} md={4}>
                                    <Paper
                                        className={this.props.classes.paper}
                                        style={{ maxHeight: 600, overflow: 'scroll' }}
                                    >
                                        <APICallPanel />
                                    </Paper>
                                </Grid>
                            )}
                        </Grid>
                    </main>
                </div>
            </div>
        );
    }
}

const Page = withStyles(styles)(PageComponent);
export default Page;
