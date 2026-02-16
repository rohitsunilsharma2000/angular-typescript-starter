import { PatientsFacade } from '../data-access/patients.facade';

export class PatientListComponent {
  constructor(private facade: PatientsFacade) {}

  render() {
    return this.facade.vm();
  }
}
