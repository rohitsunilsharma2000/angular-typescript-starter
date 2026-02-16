import { PatientsFacade } from '../data-access/patients.facade';
import { PatientListComponent } from '../components/patient-list.component';

export class PatientsPage {
  constructor(private facade: PatientsFacade) {}

  async init(): Promise<void> {
    await this.facade.load();
  }

  view() {
    const list = new PatientListComponent(this.facade);
    return list.render();
  }
}
