import * as fs from 'node:fs';
import * as path from 'node:path';

const bad: string[] = [];

function walk(dir: string) {
  for (const f of fs.readdirSync(dir)) {
    const p = path.join(dir, f);
    if (fs.statSync(p).isDirectory()) walk(p);
    else if (p.endsWith('.ts')) scan(p);
  }
}

function scan(file: string) {
  const text = fs.readFileSync(file, 'utf-8');
  const imports = [...text.matchAll(/from ['"](.+?)['"]/g)].map(m => m[1]);
  for (const i of imports) {
    if (i.includes('features/') && !file.includes('/features/')) bad.push(`${file} -> ${i}`);
    if (i.includes('shared/ui') && file.includes('/core/')) bad.push(`${file} -> ${i}`);
  }
}

walk(path.join(process.cwd(), 'src/app'));

if (bad.length) {
  console.error('Boundary violations:\n' + bad.join('\n'));
  process.exit(1);
}

console.log('No boundary issues');
