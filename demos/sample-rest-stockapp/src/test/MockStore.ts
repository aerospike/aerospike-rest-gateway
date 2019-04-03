import { shallow } from 'enzyme';
import { ReactElement } from 'react';
import { Store } from 'redux';

const shallowWithStore = (component: ReactElement<{}>, store: Store<{}>) => {
    const context = {
        store,
    };
    return shallow(component, { context });
};

export default shallowWithStore;
