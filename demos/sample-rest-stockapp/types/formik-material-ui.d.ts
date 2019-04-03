declare module 'formik-material-ui' {
    import { CheckboxProps as MuiCheckboxProps } from '@material-ui/core/Checkbox';
    import { SelectProps as MuiSelectProps } from '@material-ui/core/Select';
    import { SwitchProps as MuiSwitchProps } from '@material-ui/core/Switch';
    import { TextFieldProps as MuiTextFieldProps } from '@material-ui/core/TextField';
    import { FieldProps } from 'formik';
    import * as React from 'react';

    export interface TextFieldProps extends FieldProps, MuiTextFieldProps {}
    export const TextField: React.ComponentType<TextFieldProps>;
    export type Omit<T, K extends keyof T> = Pick<T, Exclude<keyof T, K>>;

    export interface CheckboxProps extends FieldProps, Omit<MuiCheckboxProps, 'form'> {}
    export const Checkbox: React.ComponentType<CheckboxProps>;

    export interface SelectProps extends FieldProps, MuiSelectProps {}
    const Select: React.ComponentType<SelectProps>;
    export default Select;

    export interface SwitchProps extends FieldProps, Omit<MuiSwitchProps, 'form'> {}
    export const Switch: React.ComponentType<SwitchProps>;
}
