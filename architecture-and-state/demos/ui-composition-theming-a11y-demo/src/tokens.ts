export type Theme = {
  name: 'light' | 'dark';
  colors: {
    bg: string;
    surface: string;
    primary: string;
    onPrimary: string;
    text: string;
    muted: string;
  };
  radius: string;
  spacing: (step: 1 | 2 | 3 | 4) => string;
};

export const lightTheme: Theme = {
  name: 'light',
  colors: {
    bg: '#f8fafc',
    surface: '#ffffff',
    primary: '#2563eb',
    onPrimary: '#ffffff',
    text: '#0f172a',
    muted: '#475569'
  },
  radius: '8px',
  spacing: (s) => `${s * 4}px`
};

export const darkTheme: Theme = {
  name: 'dark',
  colors: {
    bg: '#0f172a',
    surface: '#111827',
    primary: '#60a5fa',
    onPrimary: '#0b1221',
    text: '#e2e8f0',
    muted: '#94a3b8'
  },
  radius: '8px',
  spacing: (s) => `${s * 4}px`
};
