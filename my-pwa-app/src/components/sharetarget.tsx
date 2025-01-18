// src/components/ShareTarget.tsx
import { useEffect, useState } from "react";

interface SharedData {
  title?: string | null;
  text?: string | null;
  url?: string | null;
  media?: string;
}

const ShareTarget = () => {
  const [sharedData, setSharedData] = useState<SharedData | null>(null);

  useEffect(() => {
    // URL 파라미터에서 공유 데이터 확인
    const params = new URLSearchParams(window.location.search);
    const title = params.get("title");
    const text = params.get("text");
    const url = params.get("url");

    if (title || text || url) {
      setSharedData({ title, text, url });
    }

    // POST로 전송된 데이터 처리
    const handleShare = async () => {
      try {
        const formData = await parseFormData();
        if (formData) {
          const mediaFile = formData.get("media");
          if (mediaFile instanceof File) {
            setSharedData((prev) => ({
              ...(prev || {}),
              media: URL.createObjectURL(mediaFile),
            }));
          }
        }
      } catch (error) {
        console.error("Error processing share data:", error);
      }
    };

    handleShare();
  }, []);

  // FormData 파싱 함수
  const parseFormData = async () => {
    // ShareData API를 통해 들어오는 데이터 처리
    if (window.location.pathname === "/share-target") {
      try {
        // navigator.share API를 통한 공유 데이터 처리
        const formData = new FormData();
        return formData;
      } catch (error) {
        console.error("Error parsing form data:", error);
        return null;
      }
    }
    return null;
  };

  if (!sharedData) {
    return (
      <div className="min-h-screen bg-gray-100 p-4">
        <div className="bg-white shadow rounded-lg p-6">
          공유된 데이터가 없습니다.
        </div>
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-gray-100">
      <header className="bg-white shadow">
        <div className="max-w-7xl mx-auto py-6 px-4">
          <h1 className="text-3xl font-bold text-gray-900">공유된 콘텐츠</h1>
        </div>
      </header>

      <main className="max-w-7xl mx-auto py-6 px-4">
        <div className="bg-white shadow rounded-lg p-6 space-y-4">
          {sharedData.title && (
            <div>
              <h3 className="font-semibold">제목:</h3>
              <p>{sharedData.title}</p>
            </div>
          )}

          {sharedData.text && (
            <div>
              <h3 className="font-semibold">텍스트:</h3>
              <p>{sharedData.text}</p>
            </div>
          )}

          {sharedData.url && (
            <div>
              <h3 className="font-semibold">URL:</h3>
              <a
                href={sharedData.url}
                className="text-blue-500 hover:underline"
                target="_blank"
                rel="noopener noreferrer"
              >
                {sharedData.url}
              </a>
            </div>
          )}

          {sharedData.media && (
            <div>
              <h3 className="font-semibold">미디어:</h3>
              <img
                src={sharedData.media}
                alt="Shared media"
                className="max-w-full h-auto rounded-lg shadow-lg"
              />
            </div>
          )}
        </div>
      </main>
    </div>
  );
};

export default ShareTarget;
