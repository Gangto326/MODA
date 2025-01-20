// src/service-worker.js
self.addEventListener('install', (event) => {
    console.log('Service Worker installed');
    event.waitUntil(
      caches.open('my-cache').then((cache) => {
        return cache.addAll([
          '/',
          '/index.html',
          '/app.js', // 빌드 후 app.jsx가 app.js로 변환됨
          '/index.css',
        ]);
      })
    );
  });
  
  self.addEventListener('fetch', (event) => {
    event.respondWith(
      caches.match(event.request).then((response) => {
        return response || fetch(event.request);
      })
    );
  });
  