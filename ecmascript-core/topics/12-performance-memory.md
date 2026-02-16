# 12) Performance & memory — lazy data, resizable buffers

**কি শিখবেন**
- Lazy generators to avoid loading everything
- Resizable/transferable ArrayBuffer (ES2024) for imaging bytes
- Quick perf hygiene tips

**Code**
```js
// Lazy vitals stream
async function* vitalsStream(limit = 3) {
  let count = 0;
  while (count < limit) {
    await new Promise(r => setTimeout(r, 10));
    yield { hr: 88 + Math.random() * 10 };
    count++;
  }
}

(async () => {
  for await (const v of vitalsStream()) {
    console.log('vital', v.hr.toFixed(1));
  }
})();

// Resizable buffer for CT scan slices (needs modern runtime)
const buf = new ArrayBuffer(16, { maxByteLength: 64 });
const view = new Uint8Array(buf);
view.set([1, 2, 3, 4]);
buf.resize(32); // add more slices later
const transferred = buf.transfer(24); // move without copy to worker
console.log('sizes', transferred.byteLength);
```

**আরো উদাহরণ (beginner → advanced)**
1) Beginner — simple generator pipeline  
```js
function* patients(ids) {
  for (const id of ids) yield `Patient ${id}`;
}
for (const p of patients([1, 2, 3])) console.log(p);
```

2) Beginner — avoid extra array with lazy map  
```js
function* double(iterable) {
  for (const val of iterable) yield val * 2;
}
console.log([...double([1, 2, 3])]);
```

3) Intermediate — async generator batching  
```js
async function* pager(list, size = 2) {
  for (let i = 0; i < list.length; i += size) {
    await new Promise(r => setTimeout(r, 5));
    yield list.slice(i, i + size);
  }
}
(async () => {
  for await (const chunk of pager(['P1', 'P2', 'P3', 'P4'])) console.log(chunk);
})();
```

4) Intermediate — reuse buffer with TypedArray view  
```js
const buffer = new ArrayBuffer(8);
const view1 = new Uint8Array(buffer);
view1.set([1, 2, 3, 4]);
const view2 = new Uint16Array(buffer);
console.log(view2[0], view2[1]);
```

5) Advanced — resizable ArrayBuffer guard  
```js
const resizable = new ArrayBuffer(8, { maxByteLength: 32 });
const bytes = new Uint8Array(resizable);
bytes.set([9, 9, 9, 9]);
resizable.resize(16);
console.log('new length', resizable.byteLength, bytes[0]);
```

**Interview takeaways**
- Generators/async generators enable pull-based, memory-light pipelines.  
- Resizable/transferable buffers help with large binary data (imaging) without copies.  
- Always measure: avoid premature optimization; watch GC with long-lived arrays/Maps.  

**Try it**
- Build a lazy pager that yields 10 patients at a time from a big list.  
- Simulate transferring a buffer to a Web Worker (or just log the moved buffer).  
- List three micro-optimizations that usually don’t matter (and say why).  
