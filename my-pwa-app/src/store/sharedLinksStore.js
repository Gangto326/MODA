import { create } from 'zustand';


// zustand 상태 정의
export const useSharedLinksStore = create((set) => ({
    sharedLinks: [],
    addSharedLink: (link) => set((state) => ({
      sharedLinks: [...state.sharedLinks, link],
    })),
  }));