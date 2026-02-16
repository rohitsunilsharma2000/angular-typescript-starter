import { Patient } from './types';

export type Action =
  | { type: 'load page'; page: number; pageSize: number; filter: string }
  | { type: 'load success'; data: Patient[]; total: number; page: number; pageSize: number; filter: string }
  | { type: 'load failure'; error: string };

export const Actions = {
  loadPage: (page: number, pageSize: number, filter = ''): Action => ({ type: 'load page', page, pageSize, filter }),
  loadSuccess: (data: Patient[], total: number, page: number, pageSize: number, filter: string): Action => ({ type: 'load success', data, total, page, pageSize, filter }),
  loadFailure: (error: string): Action => ({ type: 'load failure', error })
};
