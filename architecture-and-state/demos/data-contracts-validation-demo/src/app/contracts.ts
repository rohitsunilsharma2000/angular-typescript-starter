import { z } from 'zod';

export const PatientDtoSchema = z.object({
  id: z.string().min(1),
  name: z.string().min(1),
  age: z.number().int().nonnegative(),
  ward: z.string().min(1),
  allergies: z.array(z.string()).default([])
});

export type PatientDto = z.infer<typeof PatientDtoSchema>;
