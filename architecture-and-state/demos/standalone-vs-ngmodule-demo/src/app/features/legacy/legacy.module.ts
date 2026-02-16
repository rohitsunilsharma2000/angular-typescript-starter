import { LegacyDashboardComponent } from './legacy-dashboard.component';

export class LegacyModule {
  getRootComponent() {
    return new LegacyDashboardComponent();
  }
}
