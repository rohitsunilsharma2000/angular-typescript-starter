// Simple ICU-like formatting (plural + select)
function format(locale: 'en'|'bn', count: number, gender: 'male'|'female'|'other') {
  const plural = count === 0 ? (locale==='bn'?'কেউ নেই':'nobody') : count ===1 ? (locale==='bn'?'১ জন':'1 person') : (locale==='bn'?`${count} জন`:`${count} people`);
  const hello = gender === 'female' ? (locale==='bn'?'সুস্বাগতম ম্যাডাম':'Welcome ma’am') : gender === 'male' ? (locale==='bn'?'সুস্বাগতম স্যার':'Welcome sir') : (locale==='bn'?'সুস্বাগতম':'Welcome');
  return `${hello}, queue: ${plural}`;
}

console.log(format('en', 2, 'female'));
console.log(format('bn', 0, 'other'));
