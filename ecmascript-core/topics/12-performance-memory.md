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

**Interview takeaways**
- Generators/async generators enable pull-based, memory-light pipelines.  
- Resizable/transferable buffers help with large binary data (imaging) without copies.  
- Always measure: avoid premature optimization; watch GC with long-lived arrays/Maps.  

**Try it**
- Build a lazy pager that yields 10 patients at a time from a big list.  
- Simulate transferring a buffer to a Web Worker (or just log the moved buffer).  
- List three micro-optimizations that usually don’t matter (and say why).  
