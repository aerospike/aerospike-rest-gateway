import { Grid } from '@material-ui/core';
import { GridProps } from '@material-ui/core/Grid';
import * as React from 'react';
import styled from 'react-emotion';
import theme from '../../core/theme';

export const ContentBoxWrapper = styled(Grid)`
    /* border: 1px solid ${theme.colors.red}; */
    position: relative;
    padding: 10px;
    :before {
        content: ' ';
        display: block;
        position: absolute;
        left: 0;
        top: 0;
        width: 100%;
        height: 100%;
        opacity: 0.9;
        background-image: url('/image/white.png');
        background-repeat: no-repeat;
        background-position: 50% 0;
        -ms-background-size: cover;
        -o-background-size: cover;
        -moz-background-size: cover;
        -webkit-background-size: cover;
        background-size: cover;
        flex: 2;
        margin: 10% 10% 0 10%;
    }
`;

export function ContentBox(props: GridProps) {
    return (
        <div>{props.children}</div>
        // <ContentBoxWrapper>
        //     <Grid container direction="column" className={css({ zIndex: 1, minHeight: '50vh' })}>
        //         {props.children}
        //     </Grid>
        // </ContentBoxWrapper>
    );
}
