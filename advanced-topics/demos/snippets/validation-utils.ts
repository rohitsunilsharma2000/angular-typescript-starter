import { AbstractControl, ValidationErrors } from '@angular/forms';
export function endAfterStart(ctrl: AbstractControl): ValidationErrors | null {
  const start = ctrl.get('start')?.value;
  const end = ctrl.get('end')?.value;
  return start && end && end < start ? { endBeforeStart: true } : null;
}
