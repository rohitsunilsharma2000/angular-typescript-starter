# 09) async/await + Promise basics

**মূল কথা**
- async ফাংশন সবসময় Promise রিটার্ন করে।
- `await` শুধু Promise-like (thenable) এর ওপর করা যায়।

**হাসপাতাল উদাহরণ**
```ts
interface LabReport { id: string; patientId: string; result: 'positive' | 'negative'; }

function fakeApi<T>(data: T, delay = 500): Promise<T> {
  return new Promise(resolve => setTimeout(() => resolve(data), delay));
}

async function fetchLabReports(patientId: string): Promise<LabReport[]> {
  const reports = await fakeApi<LabReport[]>([
    { id: 'L1', patientId, result: 'negative' },
  ]);
  return reports;
}

async function showFirstReport(patientId: string) {
  try {
    const [first] = await fetchLabReports(patientId);
    console.log(first?.result ?? 'No report');
  } catch (err) {
    console.error('Failed to load', err);
  }
}
```

**Interview Q**
- Promise resolve vs reject flow? finally কোথায় লাগে?
- parallel fetch → `await Promise.all([fetchPatients(), fetchDoctors()])`

**Try it**
- `checkBedAvailability` নামে async ফাংশন বানিয়ে 1 সেকেন্ড পরে boolean resolve করুন।

## ব্রাউজারে কনসোল টেস্ট (async/await)

1) **Basic async**
```ts
async function greet(name) { return `Hi ${name}`; }
greet('Asha').then(console.log);
```

2) **Await + try/catch**
```ts
async function risky(ok = true) {
  if (!ok) throw new Error('fail');
  return 'success';
}
(async () => {
  try { console.log(await risky(false)); }
  catch (e) { console.log('Caught:', e.message); }
})();
```

3) **Parallel calls**
```ts
function fake(delay, value) { return new Promise(r => setTimeout(() => r(value), delay)); }
(async () => {
  const [p, d] = await Promise.all([fake(200, 'patient'), fake(300, 'doctor')]);
  console.log(p, d);
})();
```

4) **Finally usage**
```ts
fake(100, 'done')
  .finally(() => console.log('cleanup after API'));
```
