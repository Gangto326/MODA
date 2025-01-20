import React, { useEffect } from 'react';
import { useSearchParams, useNavigate } from 'react-router-dom';
import useSharedLinksStore from './store/sharedLinksStore';

const Share = () => {
  const [searchParams] = useSearchParams();
  const addSharedLink = useSharedLinksStore((state) => state.addSharedLink);
  const navigate = useNavigate();

  useEffect(() => {
    const url = searchParams.get('url');
    if (url) {
      addSharedLink(url); // 상태에 추가
    }
    navigate('/'); // 메인 페이지로 이동
  }, [searchParams, addSharedLink, navigate]);

  return <p>Processing shared link...</p>;
};

export default Share;
