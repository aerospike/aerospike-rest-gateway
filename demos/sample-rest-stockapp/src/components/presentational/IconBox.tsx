import { Grid } from '@material-ui/core';
import * as React from 'react';
import styled from 'react-emotion';
import theme from '../../core/theme';
import Icon, { IconProps } from './Icon';

const Label = styled('div')`
    font-weight: bold;
    color: ${theme.colors.black};
    text-align: center;
`;

export interface IconBoxProps {
    readonly iconProps: IconProps;
    readonly title?: string;
    readonly width?: number;
    readonly height?: number;
    readonly bg?: string;
}

interface IconBoxWrapperProps {
    readonly width?: number;
    readonly height?: number;
}
const IconBoxWrapper = styled(Grid)`
    width: ${(props: IconBoxWrapperProps) => props.width};
    height: ${(props: IconBoxWrapperProps) => props.height};
`;
export class IconBox extends React.PureComponent<IconBoxProps> {
    render() {
        const { width, height } = this.props;

        return (
            <IconBoxWrapper
                width={width}
                height={height}
                direction="column"
                alignItems="center"
                container
            >
                <Icon {...this.props.iconProps} />
                <Label>{this.props.title}</Label>
            </IconBoxWrapper>
        );
    }
}
