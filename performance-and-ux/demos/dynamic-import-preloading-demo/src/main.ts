async function loadFeature(name: string) {
  console.log(`preloading ${name}...`);
  await new Promise(r => setTimeout(r, 20));
  console.log(`${name} loaded`);
}

(async () => {
  await Promise.all([loadFeature('patients'), loadFeature('billing')]);
  console.log('ready to navigate without delay');
})();
