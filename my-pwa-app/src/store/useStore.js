import { create } from "zustand";

const useStore = create((set) => ({
  count: 0,
  name: "Default",
  increaseCount: () => set((state) => ({ count: state.count + 1 })),
  changeName: (newName) => set(() => ({ name: newName })),
}));

export default useStore;
