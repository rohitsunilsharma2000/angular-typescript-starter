const ssrHtml = '<div id="app"><p>Server rendered</p></div>';
const clientHydrate = () => 'Hydrated: events attached';

console.log('SSR HTML:', ssrHtml);
console.log('Hydration:', clientHydrate());
