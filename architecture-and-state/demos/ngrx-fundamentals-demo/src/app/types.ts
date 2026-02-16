export type Appointment = { id: string; patient: string; slot: string };
export type State = { data: Appointment[]; loading: boolean; error?: string };
