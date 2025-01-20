// import { StrictMode } from 'react'
// import { createRoot } from 'react-dom/client'
// import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
// import './index.css'
// import App from './App.jsx'


// // createRoot(document.getElementById('root')).render(
// //   <StrictMode>
// //     <App />
// //   </StrictMode>,
// // )

// const queryClient = new QueryClient();

// // createRoot로 변경
// createRoot(document.getElementById("root")).render(
//   <StrictMode>
//     <QueryClientProvider client={queryClient}>
//       <App />
//     </QueryClientProvider>
//   </StrictMode>
// );

import React from "react"; // React import 필요
import ReactDOM from "react-dom/client"; // ReactDOM import 필요
import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import App from "./App.jsx";
import ShowLink from "./ShowLink.jsx"; // ShowLink 컴포넌트 가져오기
import "./index.css"; // Tailwind CSS

const queryClient = new QueryClient();

ReactDOM.createRoot(document.getElementById("root")).render(
  <React.StrictMode>
    <QueryClientProvider client={queryClient}>
      <Router>
        <Routes>
          <Route path="/" element={<App />} />
          <Route path="/show-link" element={<ShowLink />} />
          {/* <Route path="/" element={<ShowLink />} /> */}
        </Routes>
      </Router>
    </QueryClientProvider>
  </React.StrictMode>
);
