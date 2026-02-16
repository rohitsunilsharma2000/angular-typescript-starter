const messages = {
  en: { hello: 'Hello', patients: (n: number) => (n === 1 ? '1 patient' : `${n} patients`) },
  bn: { hello: 'হ্যালো', patients: (n: number) => (n === 1 ? '১ জন রোগী' : `${n} জন রোগী`) }
};

function t(locale: 'en'|'bn', key: 'hello', n?: number) {
  if (key === 'hello') return messages[locale].hello;
  return messages[locale].patients(n ?? 0);
}

console.log('en hello:', t('en','hello'));
console.log('bn hello:', t('bn','hello'));
console.log('en plural:', t('en','hello', 2));
console.log('bn plural:', t('bn','hello', 2));
