import { css } from 'emotion';
import * as React from 'react';

interface BaseIconProps {
    readonly icon?: string;
    readonly src?: string;
    readonly width?: number | string;
    readonly height?: number | string;
    readonly fill?: string;
    readonly bg?: string;
    readonly m?: number | string;
    readonly p?: number | string;
}

interface RefIconProps extends BaseIconProps {
    readonly icon: string;
}

interface ImgIconProps extends BaseIconProps {
    readonly src: string;
}

export type IconProps = RefIconProps | ImgIconProps;

const Icon = ({
    icon,
    src,
    fill,
    bg,
    m = '0',
    p = '0',
    width = 'auto',
    height = width,
}: IconProps) => {
    width = typeof width === 'number' ? `${width}px` : width;
    height = typeof height === 'number' ? `${height}px` : height;
    m = typeof m === 'number' ? `${m}px` : m;
    p = typeof p === 'number' ? `${p}px` : p;

    const markup = css`
        fill: ${fill};
        background: ${bg};
        width: ${width};
        height: ${height || width};
        margin: ${m};
        padding: ${p};
    `;

    let image: JSX.Element;
    if (icon && icon.length) {
        image = (
            <svg className={markup}>
                <use xlinkHref={`#${icon}`} />
            </svg>
        );
    } else if (src && src.length) {
        image = <img className={markup} src={src} />;
    } else {
        throw new Error('src, nor icon has content');
    }
    return image;
};

export default Icon;
