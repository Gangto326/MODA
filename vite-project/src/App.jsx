// import { useState } from 'react'
// import reactLogo from './assets/react.svg'
// import appLogo from '/favicon.svg'
// import PWABadge from './PWABadge.jsx'
// import './App.css'

// function App() {
//   const [count, setCount] = useState(0)

//   return (
//     <>
//       <div>
//         <a href="https://vitejs.dev" target="_blank">
//           <img src={appLogo} className="logo" alt="vite-project logo" />
//         </a>
//         <a href="https://react.dev" target="_blank">
//           <img src={reactLogo} className="logo react" alt="React logo" />
//         </a>
//       </div>
//       <h1>vite-project</h1>
//       <div className="card">
//         <button onClick={() => setCount((count) => count + 1)}>
//           count is {count}
//         </button>
//         <p>
//           Edit <code>src/App.jsx</code> and save to test HMR
//         </p>
//       </div>
//       <p className="read-the-docs">
//         Click on the Vite and React logos to learn more
//       </p>
//       <PWABadge />
//     </>
//   )
// }

// export default App



///////////////////////////////////////
// import React from 'react';
// import useSharedLinksStore from './store/sharedLinksStore';

// const App = () => {
//   const sharedLinks = useSharedLinksStore((state) => state.sharedLinks);

//   return (
//     <div className="app-container">
//       <h1>Shared Links</h1>
//       {sharedLinks.length > 0 ? (
//         <ul>
//           {sharedLinks.map((link, index) => (
//             <li key={index}>
//               <a href={link} target="_blank" rel="noopener noreferrer">
//                 {link}
//               </a>
//             </li>
//           ))}
//         </ul>
//       ) : (
//         <p>No links shared yet.</p>
//       )}
//     </div>
//   );
// };

// export default App;



// App.jsx
import React from "react";
import { useSharedLinksStore } from "./store/sharedLinksStore";

export default function App() {
  const sharedLinks = useSharedLinksStore((state) => state.sharedLinks);

  return (
    <div>
      <h1>공유된 링크</h1>
      <ul>
        {sharedLinks.map((link, index) => (
          <li key={index}>{link}</li>
        ))}
      </ul>
    </div>
  );
}
