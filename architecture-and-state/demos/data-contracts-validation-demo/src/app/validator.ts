import { z } from 'zod';
import { PatientDtoSchema, PatientDto } from './contracts';

export function validatePatients(input: unknown): PatientDto[] {
  const schema = z.array(PatientDtoSchema);
  const result = schema.safeParse(input);
  if (!result.success) {
    const issues = result.error.issues.map(i => `${i.path.join('.') || 'root'}: ${i.message}`);
    throw new Error('Validation failed: ' + issues.join('; '));
  }
  return result.data;
}
