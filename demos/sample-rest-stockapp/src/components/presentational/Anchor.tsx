import styled from 'react-emotion';
import theme from '../../core/theme';
export const Anchor = styled('a')`
    color: ${theme.link.text};
    cursor: pointer;
    text-decoration: underline;
    &:hover {
        color: ${theme.link.hover};
    }
    &:active {
        color: ${theme.link.active};
    }
`;
