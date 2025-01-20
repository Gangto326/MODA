import { create } from "zustand";

export const useLinkStore = create((set) => ({
  sharedLink: null,
  setSharedLink: (link) => set({ sharedLink: link }),
}));
