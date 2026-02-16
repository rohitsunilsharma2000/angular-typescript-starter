import fs from 'node:fs';
import zlib from 'node:zlib';

const BUDGET_KB = Number(process.env.BUDGET_KB || 350);
const file = process.argv[2];
if (!file) {
  console.error('Usage: node bundle-size-guard.js <path-to-js>');
  process.exit(1);
}
const gz = zlib.gzipSync(fs.readFileSync(file));
const sizeKb = gz.length / 1024;
if (sizeKb > BUDGET_KB) {
  console.error(`Bundle ${sizeKb.toFixed(1)}KB exceeds budget ${BUDGET_KB}KB`);
  process.exit(1);
} else {
  console.log(`Bundle OK: ${sizeKb.toFixed(1)}KB <= ${BUDGET_KB}KB`);
}
