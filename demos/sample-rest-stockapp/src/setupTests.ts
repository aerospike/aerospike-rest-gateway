import { configure } from 'enzyme';

// Odd require from adapter TODO: investigate types
const Adapter = require('enzyme-adapter-react-16');

configure({ adapter: new Adapter() });
