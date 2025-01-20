// import React from 'react'
// import ReactDOM from 'react-dom/client'
// import App from './App.jsx'
// import './index.css'

// ReactDOM.createRoot(document.getElementById('root')).render(
//   <React.StrictMode>
//     <App />
//   </React.StrictMode>,
// )


////////////////////////////////////////////////////////////
// import React from 'react';
// import ReactDOM from 'react-dom/client';
// import { BrowserRouter, Routes, Route } from 'react-router-dom';
// import { QueryClient, QueryClientProvider } from 'react-query';
// import App from './App';
// import Share from './Share';
// import './index.css';

// const queryClient = new QueryClient();

// ReactDOM.createRoot(document.getElementById('root')).render(
//   <React.StrictMode>
//     <QueryClientProvider client={queryClient}>
//       <BrowserRouter>
//         <Routes>
//           <Route path="/" element={<App />} />
//           <Route path="/share" element={<Share />} />
//         </Routes>
//       </BrowserRouter>
//     </QueryClientProvider>
//   </React.StrictMode>
// );


import React, { useEffect } from "react";
import ReactDOM from "react-dom/client";
import { BrowserRouter, Routes, Route, useSearchParams } from "react-router-dom";
import App from "./App";
import { useSharedLinksStore } from "./store/sharedLinksStore";

function SharedLinkHandler() {
  const { addSharedLink } = useSharedLinksStore();
  const [searchParams] = useSearchParams();

  useEffect(() => {
    const sharedUrl = searchParams.get("url"); // 공유된 URL 가져오기
    if (sharedUrl) {
      addSharedLink(sharedUrl); // 상태에 저장
    }
  }, [searchParams, addSharedLink]);

  return <App />;
}

ReactDOM.createRoot(document.getElementById("root")).render(
  <BrowserRouter>
    <Routes>
      <Route path="/*" element={<SharedLinkHandler />} />
    </Routes>
  </BrowserRouter>
);
