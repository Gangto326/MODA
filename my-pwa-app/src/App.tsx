// src/App.tsx
import { useState, useEffect } from "react";
import "./App.css";

function App() {
  const [isOnline, setIsOnline] = useState(navigator.onLine);
  const [installPrompt, setInstallPrompt] = useState<Event | null>(null);

  useEffect(() => {
    // 온라인 상태 감지
    const handleOnline = () => setIsOnline(true);
    const handleOffline = () => setIsOnline(false);

    window.addEventListener("online", handleOnline);
    window.addEventListener("offline", handleOffline);

    // PWA 설치 프롬프트 감지
    window.addEventListener("beforeinstallprompt", (e) => {
      e.preventDefault();
      setInstallPrompt(e);
    });

    return () => {
      window.removeEventListener("online", handleOnline);
      window.removeEventListener("offline", handleOffline);
    };
  }, []);

  const handleInstallClick = async () => {
    if (!installPrompt) return;

    const promptEvent = installPrompt as any;
    const result = await promptEvent.prompt();
    console.log(result);
    setInstallPrompt(null);
  };

  return (
    <div className="min-h-screen bg-gray-100">
      <header className="bg-white shadow">
        <div className="max-w-7xl mx-auto py-6 px-4">
          <h1 className="text-3xl font-bold text-gray-900">PWA Demo App</h1>
        </div>
      </header>

      <main className="max-w-7xl mx-auto py-6 px-4">
        <div className="bg-white shadow rounded-lg p-6">
          {/* 네트워크 상태 표시 */}
          <div
            className={`mb-4 p-3 rounded-md ${
              isOnline
                ? "bg-green-100 text-green-800"
                : "bg-red-100 text-red-800"
            }`}
          >
            <p className="text-sm">
              {isOnline ? "온라인 모드" : "오프라인 모드"}
            </p>
          </div>

          {/* 설치 버튼 */}
          {installPrompt && (
            <button
              onClick={handleInstallClick}
              className="bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-600"
            >
              앱 설치하기
            </button>
          )}
        </div>
      </main>
    </div>
  );
}

export default App;
