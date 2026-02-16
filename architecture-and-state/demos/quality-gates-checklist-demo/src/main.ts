import { gates } from './gates';

async function run() {
  console.log('Quality gates demo (set FAIL=lint,tests to simulate failures)');
  const results = [] as Awaited<ReturnType<(typeof gates)[number]['run']>>[];
  for (const gate of gates) {
    const res = await gate.run();
    results.push(res);
    const icon = res.status === 'pass' ? '✅' : '❌';
    console.log(`${icon} ${gate.name} — ${gate.description}` + (res.details ? ` (${res.details})` : ''));
  }
  const failed = results.filter(r => r.status === 'fail');
  console.log('\nSummary:', failed.length ? `${failed.length} gate(s) failed` : 'all gates passed');
  if (failed.length) {
    console.log('Failed gates:', failed.map(f => f.gate).join(', '));
    process.exitCode = 1;
  }
}

run();
