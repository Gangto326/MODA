// if (!self.define) {
//   let e,
//     i = {};
//   const n = (n, r) => (
//     (n = new URL(n + ".js", r).href),
//     i[n] ||
//       new Promise((i) => {
//         if ("document" in self) {
//           const e = document.createElement("script");
//           (e.src = n), (e.onload = i), document.head.appendChild(e);
//         } else (e = n), importScripts(n), i();
//       }).then(() => {
//         let e = i[n];
//         if (!e) throw new Error(`Module ${n} didn’t register its module`);
//         return e;
//       })
//   );
//   self.define = (r, s) => {
//     const o =
//       e ||
//       ("document" in self ? document.currentScript.src : "") ||
//       location.href;
//     if (i[o]) return;
//     let c = {};
//     const d = (e) => n(e, o),
//       f = { module: { uri: o }, exports: c, require: d };
//     i[o] = Promise.all(r.map((e) => f[e] || d(e))).then((e) => (s(...e), c));
//   };
// }
// define(["./workbox-5ffe50d4"], function (e) {
//   "use strict";
//   self.addEventListener("message", (e) => {
//     e.data && "SKIP_WAITING" === e.data.type && self.skipWaiting();
//   }),
//     e.clientsClaim(),
//     e.precacheAndRoute(
//       [
//         {
//           url: "apple-touch-icon-180x180.png",
//           revision: "10b5f88323fdad31bf50c90668d3e84a",
//         },
//         { url: "assets/index-DEkAq3pQ.js", revision: null },
//         { url: "favicon.ico", revision: "89099cfae0775e3e086613bca3190493" },
//         { url: "favicon.svg", revision: "71dcfd191507c31dc79efe3341dfa3b9" },
//         { url: "index.html", revision: "a340d3db9ab08151e6991f742413174b" },
//         {
//           url: "maskable-icon-512x512.png",
//           revision: "126c55dc030a58db716758479c41c570",
//         },
//         {
//           url: "pwa-192x192.png",
//           revision: "14a23cc23a2f5a3157ac52e78135346c",
//         },
//         {
//           url: "pwa-512x512.png",
//           revision: "5a051418936d2f633fc164f78b1662e1",
//         },
//         { url: "pwa-64x64.png", revision: "e364fbdd8a3dde0c6167972af41c9dbf" },
//         {
//           url: "manifest.webmanifest",
//           revision: "dafbd7a64c5d832f4815e136e0edd50e",
//         },
//       ],
//       {}
//     ),
//     e.cleanupOutdatedCaches(),
//     e.registerRoute(
//       new e.NavigationRoute(e.createHandlerBoundToURL("index.html"))
//     );
// });

if (!self.define) {
  let e,
    i = {};
  const n = (n, r) => (
    (n = new URL(n + ".js", r).href),
    i[n] ||
      new Promise((i) => {
        if ("document" in self) {
          const e = document.createElement("script");
          (e.src = n), (e.onload = i), document.head.appendChild(e);
        } else (e = n), importScripts(n), i();
      }).then(() => {
        let e = i[n];
        if (!e) throw new Error(`Module ${n} didn’t register its module`);
        return e;
      })
  );
  self.define = (r, s) => {
    const o =
      e ||
      ("document" in self ? document.currentScript.src : "") ||
      location.href;
    if (i[o]) return;
    let c = {};
    const d = (e) => n(e, o),
      f = { module: { uri: o }, exports: c, require: d };
    i[o] = Promise.all(r.map((e) => f[e] || d(e))).then((e) => (s(...e), c));
  };
}
define([
  "https://storage.googleapis.com/workbox-cdn/releases/5.1.4/workbox-sw.js",
], function (workbox) {
  "use strict";
  self.addEventListener("message", (e) => {
    e.data && "SKIP_WAITING" === e.data.type && self.skipWaiting();
  }),
    workbox.clientsClaim(),
    workbox.precacheAndRoute(
      [
        {
          url: "apple-touch-icon-180x180.png",
          revision: "10b5f88323fdad31bf50c90668d3e84a",
        },
        { url: "assets/index-DEkAq3pQ.js", revision: null },
        { url: "favicon.ico", revision: "89099cfae0775e3e086613bca3190493" },
        { url: "favicon.svg", revision: "71dcfd191507c31dc79efe3341dfa3b9" },
        { url: "index.html", revision: "a340d3db9ab08151e6991f742413174b" },
        {
          url: "maskable-icon-512x512.png",
          revision: "126c55dc030a58db716758479c41c570",
        },
        {
          url: "pwa-192x192.png",
          revision: "14a23cc23a2f5a3157ac52e78135346c",
        },
        {
          url: "pwa-512x512.png",
          revision: "5a051418936d2f633fc164f78b1662e1",
        },
        { url: "pwa-64x64.png", revision: "e364fbdd8a3dde0c6167972af41c9dbf" },
        {
          url: "manifest.webmanifest",
          revision: "dafbd7a64c5d832f4815e136e0edd50e",
        },
      ],
      {}
    ),
    workbox.cleanupOutdatedCaches(),
    workbox.registerRoute(
      new workbox.NavigationRoute(workbox.createHandlerBoundToURL("index.html"))
    );
});
