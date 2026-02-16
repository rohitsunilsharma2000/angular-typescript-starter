import { patientsRoutes } from './features/patients/patients.routes';
import { billingRoutes } from './features/billing/billing.routes';

export const appConfig = {
  routes: [...patientsRoutes, ...billingRoutes]
};
