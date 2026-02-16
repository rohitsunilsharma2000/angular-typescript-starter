// WCAG contrast helpers (relative luminance & ratio)
function hexToRgb(hex: string): [number, number, number] {
  const clean = hex.replace('#', '');
  const bigint = parseInt(clean, 16);
  const len = clean.length;
  if (len === 3) {
    const r = (bigint >> 8) & 0xf;
    const g = (bigint >> 4) & 0xf;
    const b = bigint & 0xf;
    return [r / 15, g / 15, b / 15];
  }
  const r = (bigint >> 16) & 255;
  const g = (bigint >> 8) & 255;
  const b = bigint & 255;
  return [r / 255, g / 255, b / 255];
}

function relLum([r, g, b]: [number, number, number]) {
  const f = (c: number) => (c <= 0.03928 ? c / 12.92 : Math.pow((c + 0.055) / 1.055, 2.4));
  return 0.2126 * f(r) + 0.7152 * f(g) + 0.0722 * f(b);
}

export function contrastRatio(fgHex: string, bgHex: string): number {
  const L1 = relLum(hexToRgb(fgHex));
  const L2 = relLum(hexToRgb(bgHex));
  const [light, dark] = L1 > L2 ? [L1, L2] : [L2, L1];
  return (light + 0.05) / (dark + 0.05);
}

export function assertContrast(fgHex: string, bgHex: string, min = 4.5) {
  const ratio = contrastRatio(fgHex, bgHex);
  if (ratio < min) {
    throw new Error(`Contrast ${ratio.toFixed(2)} < ${min} for fg ${fgHex} on ${bgHex}`);
  }
  return ratio;
}
