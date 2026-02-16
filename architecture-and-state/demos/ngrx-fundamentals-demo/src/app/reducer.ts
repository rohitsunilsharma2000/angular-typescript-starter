import { Action } from './actions';
import { State } from './types';

export const initialState: State = { data: [], loading: false };

export function reducer(state: State, action: Action): State {
  switch (action.type) {
    case 'load':
      return { ...state, loading: true, error: undefined };
    case 'load success':
      return { ...state, loading: false, data: action.data, error: undefined };
    case 'load failure':
      return { ...state, loading: false, error: action.error };
    default:
      return state;
  }
}
