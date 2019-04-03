import { Button, Grid, Typography } from '@material-ui/core';
import { push } from 'connected-react-router';
import { Field, FieldArray, Form, Formik, FormikActions, FormikValues } from 'formik';
import { TextField } from 'formik-material-ui';
import * as React from 'react';
import { connect } from 'react-redux';
import { Action } from 'redux';
import { ThunkDispatch } from 'redux-thunk';
import { addAPICall } from '../actions/apiCall';
import { clearError, setErrorMessage } from '../actions/errors';
import { setStockList } from '../actions/stockentries';
import { ContentBox } from '../components/presentational/ContentBox';
import ErrorState from '../core/types/ErrorState';
import { StoreState } from '../core/types/StoreState';
import { TypedMap } from '../core/types/TypedMap';
import {
    doCreateRecordNamespaceSetKey,
    doGetRecordNamespaceSetKey
} from '../generated/core/actions/keyValueOperationsActionCreators';
import { doOperateNamespaceSetKey } from '../generated/core/actions/operateOperationsActionCreators';
import { Record, RestClientError } from '../generated/core/api';
import { LastApiCall } from '../generated/core/state/ApiState';
import Page from './Page';
import isAuthError from '../utils/authError';

interface CreatePortfolioProps {
    lastResponse?: TypedMap<LastApiCall>;
    doCreatePortfolio: (recipeId: string, bins: {}) => void;
    getStockList: () => void;
    errorStatus: ErrorState;
    stockList: string[];
    clearErrorMessage: () => void;
}
interface CreatePortfolioState {
    numberOfStocks: number;
}

export class CreatePortfolioComponent extends React.PureComponent<
    CreatePortfolioProps,
    CreatePortfolioState
> {
    constructor(props: CreatePortfolioProps) {
        super(props);
        this.props.clearErrorMessage();
        this.props.getStockList();
        this.state = {
            numberOfStocks: 3
        };
    }

    public render() {
        const emptyStocks = Array.apply(null, Array(this.state.numberOfStocks)).map(
            String.prototype.valueOf,
            ''
        );
        const options = this.props.stockList.map((name, index) => (
            <option key={`stock-${index}`} value={name}>
                {name}
            </option>
        ));
        return (
            <Page
                errorStatus={this.props.errorStatus}
                lastResponse={this.props.lastResponse}
                pageTitle="Aerospike Stock Central"
            >
                <ContentBox>
                    <Formik
                        initialValues={{
                            portfolioName: '',
                            stocks: emptyStocks
                        }}
                        mapPropsToValues={(props: any) => ({
                            portfolioName: '',
                            ingredients: emptyStocks
                        })}
                        onSubmit={(
                            values: FormikValues,
                            { setSubmitting }: FormikActions<FormikValues>
                        ) => {
                            this.props.doCreatePortfolio(values.portfolioName, {
                                id: values.portfolioName,
                                name: values.portfolioName,
                                stocks: values.stocks.filter((stocks: string) => stocks !== '')
                            });
                        }}
                        render={() => (
                            <Grid container direction="column">
                                <Typography variant="title">Create Portfolio</Typography>
                                <Form>
                                    <Field
                                        name="portfolioName"
                                        placeholder="name of portfolio ex: Awesome Portfolio"
                                        label="Portfolio Name"
                                        margin="normal"
                                        variant="outlined"
                                        required={true}
                                        fullWidth={true}
                                        validate={(val: any) =>
                                            val === '' ? 'Value cannot be empty' : undefined
                                        }
                                        component={TextField}
                                    />
                                    <Typography variant="subheading">Stocks:</Typography>

                                    <Grid item xs>
                                        <FieldArray
                                            name="stocks"
                                            render={({ push }) => {
                                                return Array.from(
                                                    new Array(this.state.numberOfStocks)
                                                ).map((_: any, ii: number) => (
                                                    <div key={`stock-${ii}`}>
                                                        <Field
                                                            component="select"
                                                            id={`stock-${ii}`}
                                                            name={`stocks[${ii}]`}
                                                            margin="normal"
                                                            variant="outlined"
                                                        >
                                                            <option value="_blank" />
                                                            {options}
                                                        </Field>
                                                    </div>
                                                ));
                                            }}
                                        />
                                    </Grid>
                                    <Button
                                        disableRipple={true}
                                        focusRipple={false}
                                        onClick={e => {
                                            this.setState({
                                                numberOfStocks: this.state.numberOfStocks + 1
                                            });
                                            e.preventDefault();
                                        }}
                                    >
                                        Add Another Stock
                                    </Button>
                                    <Button type="submit" color="primary" variant="contained">
                                        Create Portfolio
                                    </Button>
                                </Form>
                            </Grid>
                        )}
                    />
                </ContentBox>
            </Page>
        );
    }
}

function stateToProps(state: StoreState) {
    return {
        lastResponse: state.api.get('last'),
        stockList: state.stockList,
        errorStatus: state.errorState
    };
}

function doAddToMostRecent(
    portfolioId: string,
    dispatch: ThunkDispatch<StoreState, void, Action>,
    onSuccess?: () => any | void
) {
    const ops = [
        {
            opValues: {
                bin: 'portfolios',
                value: portfolioId,
                listReturnType: 'NONE'
            },
            operation: 'LIST_REMOVE_BY_VALUE'
        },
        {
            opValues: {
                bin: 'portfolios',
                value: portfolioId,
                index: 0
            },
            operation: 'LIST_INSERT'
        },
        {
            opValues: {
                bin: 'portfolios',
                index: 0,
                count: 10
            },
            operation: 'LIST_TRIM'
        }
    ];

    dispatch(
        doOperateNamespaceSetKey({
            key: process.env.REACT_APP_RECENT_PORTFOLIOS_KEY!,
            namespace: process.env.REACT_APP_STOCK_NS!,
            set: process.env.REACT_APP_STOCKS_SET!,
            operations: ops,
            onError: (errorArg?: RestClientError) => {
                if (isAuthError(errorArg)) {
                    dispatch(setErrorMessage('Not authenticated to use API'));
                    dispatch(push('/login'));
                    return;
                }
                const key = process.env.REACT_APP_RECENT_PORTFOLIOS_KEY;
                const ns = process.env.REACT_APP_STOCK_NS;
                const set = process.env.REACT_APP_STOCKS_SET;
                const endpoint = `/v1/operate/${ns}/${set}/${key}`;
                dispatch(
                    addAPICall(
                        'POST',
                        endpoint,
                        false,
                        errorArg && errorArg.message ? errorArg.message : '',
                        JSON.stringify(ops, null, 4)
                    )
                );
            },
            onSuccess: () => {
                const key = process.env.REACT_APP_RECENT_PORTFOLIOS_KEY;
                const ns = process.env.REACT_APP_STOCK_NS;
                const set = process.env.REACT_APP_STOCKS_SET;
                const endpoint = `/v1/operate/${ns}/${set}/${key}`;
                dispatch(addAPICall('POST', endpoint, true, '', JSON.stringify(ops, null, 4)));
                if (onSuccess) {
                    onSuccess();
                }
            }
        })
    );
}

function dispatchToProps(dispatch: ThunkDispatch<StoreState, void, Action>) {
    return {
        clearErrorMessage: () => {
            dispatch(clearError());
        },
        doCreatePortfolio: (portfolioId: string, bins: {}) => {
            const key = portfolioId;
            const ns = process.env.REACT_APP_STOCK_NS;
            const set = process.env.REACT_APP_PORTFOLIOS_SET;
            const endpoint = `/v1/kvs/${ns}/${set}/${key}`;
            dispatch(
                doCreateRecordNamespaceSetKey({
                    key: portfolioId,
                    bins,
                    namespace: process.env.REACT_APP_STOCK_NS!,
                    set: process.env.REACT_APP_PORTFOLIOS_SET!,
                    onError: (errorArg?: RestClientError) => {
                        if (isAuthError(errorArg)) {
                            dispatch(setErrorMessage('Not authenticated to use API'));
                            dispatch(push('/login'));
                            return;
                        }
                        dispatch(
                            addAPICall(
                                'POST',
                                endpoint,
                                false,
                                errorArg && errorArg.message ? errorArg.message : '',
                                JSON.stringify(bins, null, 4)
                            )
                        );
                        dispatch(setErrorMessage('Failed to create Porfolio'));
                    },
                    onSuccess: () => {
                        dispatch(
                            addAPICall('POST', endpoint, true, '', JSON.stringify(bins, null, 4))
                        );
                        doAddToMostRecent(portfolioId, dispatch, () =>
                            dispatch(push(`/portfolios/${portfolioId}`))
                        );
                    }
                })
            );
        },
        getStockList: () => {
            const key = process.env.REACT_APP_STOCKS_LIST_KEY!;
            const namespace = process.env.REACT_APP_STOCK_NS!;
            const set = process.env.REACT_APP_STOCKS_SET!;
            dispatch(
                doGetRecordNamespaceSetKey({
                    bins: ['symbols'],
                    key: process.env.REACT_APP_STOCKS_LIST_KEY!,
                    namespace: process.env.REACT_APP_STOCK_NS!,
                    set: process.env.REACT_APP_STOCKS_SET!,
                    onSuccess: (resultRecord?: Record) => {
                        const endpoint = `/v1/kvs/${namespace}/${set}/${key}`;
                        dispatch(
                            addAPICall('GET', endpoint, true, JSON.stringify(resultRecord, null, 4))
                        );
                        if (resultRecord) {
                            dispatch(setStockList(resultRecord));
                        }
                    },
                    onError: (res?: RestClientError) => {
                        if (isAuthError(res)) {
                            dispatch(setErrorMessage('Not authenticated to use API'));
                            dispatch(push('/login'));
                            return;
                        }
                        const endpoint = `/v1/kvs/${namespace}/${set}/${key}`;
                        dispatch(
                            addAPICall(
                                'GET',
                                endpoint,
                                false,
                                res && res.message ? res.message : ''
                            )
                        );
                        dispatch(
                            setErrorMessage(
                                'No stocks found. Make sure you have preloaded the data.'
                            )
                        );
                    }
                })
            );
        }
    };
}

const CreatePortfolioPage = connect(
    stateToProps,
    dispatchToProps
)(CreatePortfolioComponent);

export default CreatePortfolioPage;
