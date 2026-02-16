import { Action } from './actions';
import { EntityState, getInitialState, setAll } from './entity-adapter';
import { Patient, ViewState } from './types';

const initialState: ViewState = {
  data: getInitialState<Patient>(),
  pagination: { page: 1, pageSize: 5, total: 0, filter: '' },
  loading: false,
  error: undefined
};

export function reducer(state: ViewState = initialState, action: Action): ViewState {
  switch (action.type) {
    case 'load page':
      return {
        ...state,
        loading: true,
        error: undefined,
        pagination: { ...state.pagination, page: action.page, pageSize: action.pageSize, filter: action.filter }
      };
    case 'load success':
      return {
        ...state,
        loading: false,
        data: setAll(state.data, action.data),
        pagination: { page: action.page, pageSize: action.pageSize, total: action.total, filter: action.filter }
      };
    case 'load failure':
      return { ...state, loading: false, error: action.error };
    default:
      return state;
  }
}

export { initialState };
