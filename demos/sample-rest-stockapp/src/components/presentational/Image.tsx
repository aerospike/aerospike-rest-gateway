import styled from 'react-emotion';

export interface ImageProps {
    padding?: string;
    margin?: string;
}

const Image = styled('img')`
    margin: ${(props: ImageProps) => props.padding || 0};
    padding: ${(props: ImageProps) => props.padding || 0};
`;
export default Image;
