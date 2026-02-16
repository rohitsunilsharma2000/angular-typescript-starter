import { Appointment } from './types';

export type Action =
  | { type: 'load' }
  | { type: 'load success'; data: Appointment[] }
  | { type: 'load failure'; error: string };

export const AppointmentsActions = {
  load: (): Action => ({ type: 'load' }),
  loadSuccess: (data: Appointment[]): Action => ({ type: 'load success', data }),
  loadFailure: (error: string): Action => ({ type: 'load failure', error })
};
