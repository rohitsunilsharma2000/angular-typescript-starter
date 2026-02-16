type GateResult = { gate: string; status: 'pass' | 'fail'; details?: string };

export type Gate = { name: string; description: string; run: () => Promise<GateResult> };

const maybeFail = (name: string): boolean => {
  const env = process.env.FAIL?.split(',').map(s => s.trim().toLowerCase()) ?? [];
  return env.includes(name.toLowerCase());
};

export const gates: Gate[] = [
  {
    name: 'lint',
    description: 'ESLint/import-boundary rules',
    run: async () => ({ gate: 'lint', status: maybeFail('lint') ? 'fail' : 'pass', details: 'mocked ESLint run' })
  },
  {
    name: 'typecheck',
    description: 'tsc --noEmit',
    run: async () => ({ gate: 'typecheck', status: maybeFail('typecheck') ? 'fail' : 'pass', details: 'mocked tsc run' })
  },
  {
    name: 'tests',
    description: 'unit/integration tests',
    run: async () => ({ gate: 'tests', status: maybeFail('tests') ? 'fail' : 'pass', details: 'mocked test suite' })
  },
  {
    name: 'a11y',
    description: 'basic accessibility checks (aria/contrast)',
    run: async () => ({ gate: 'a11y', status: maybeFail('a11y') ? 'fail' : 'pass', details: 'mocked axe run' })
  },
  {
    name: 'bundle-size',
    description: 'bundle size budget',
    run: async () => ({ gate: 'bundle-size', status: maybeFail('bundle-size') ? 'fail' : 'pass', details: 'mocked budget check' })
  }
];
