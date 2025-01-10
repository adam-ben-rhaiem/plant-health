const cacheName = "Plant-Health-V1";
const assets = [
    "./scripts/main.js",
    "./styles/main.css",
    "./index.html",
    

]
//install Event Listener
self.addEventListener("install",installEvent=>{
    installEvent.waitUntil(
        caches.open(cacheName).then(cache=>{
            cache.addAll(assets);
        })
    )
});

self.addEventListener("fetch",fetchEvent=>{
    // console.log(fetchEvent.request);
    fetchEvent.respondWith(
        caches.match(fetchEvent.request).then(res=> {
            return res || fetch(fetchEvent.request);
        })
    );
});
