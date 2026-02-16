const imgWithPlaceholder = '<img src="low.jpg" width="400" height="300" loading="lazy" />';
const imgWithoutSize = '<img src="high.jpg" loading="lazy" />';

console.log('With size (no CLS):', imgWithPlaceholder);
console.log('No size (risk CLS):', imgWithoutSize);
