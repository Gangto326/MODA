import { useEffect } from "react";
import { useSearchParams } from "react-router-dom";
import { useLinkStore } from "./store/linkStore";

const ShowLink = () => {
  const [searchParams] = useSearchParams();
  const { setSharedLink, sharedLink } = useLinkStore();

  useEffect(() => {
    const url = searchParams.get("url");
    if (url) {
      setSharedLink(url);
    }
  }, [searchParams, setSharedLink]);

  return (
    <div className="flex flex-col items-center justify-center min-h-screen bg-gray-100">
      <h1 className="text-xl font-bold mb-4">Shared Link:</h1>
      {sharedLink ? (
        <a
          href={sharedLink}
          target="_blank"
          rel="noopener noreferrer"
          className="text-blue-500 underline"
        >
          {sharedLink}
        </a>
      ) : (
        <p>No link shared yet.</p>
      )}
    </div>
  );
};

export default ShowLink;
