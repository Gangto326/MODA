import { create } from "zustand"; // zustand에서 create 함수 불러오기

// "useGameStore" 상태 훅 정의
// 상태를 만들 때 create를 사용
const useGameStore = create((set) => ({
  board: Array(9).fill(null), // 게임 보드 (9개의 칸, 처음엔 모두 빈 칸)
  isXNext: true, // 다음 차례가 X인지 O인지 (X가 먼저 시작)
  winner: null, // 승자가 있으면 그 값 저장 (없으면 null)

  // 턴을 진행하는 함수 (인덱스를 받음)
  // 게임의 각 칸에 X 또는 O를 넣는 함수
  playMove: (index) =>
    set((state) => {
      // 이미 칸에 값이 있거나 게임이 끝난 경우 -> 아무 작업도 하지 않음
      if (state.board[index] || state.winner) return state;

      // 보드 상태 복사해서 업데이트
      const newBoard = [...state.board];
      newBoard[index] = state.isXNext ? "X" : "O"; // X 또는 O 차례대로 배치
      const winner = calculateWinner(newBoard); // 새로운 보드에서 승자 계산

      return {
        board: newBoard,
        isXNext: !state.isXNext, // 턴 변경
        winner, // 승자 업데이트
      };
    }),

  // 게임 초기화하는 함수
  resetGame: () =>
    set(() => ({
      board: Array(9).fill(null), // 보드 초기화
      isXNext: true, // X가 다시 첫 번째 턴
      winner: null, // 승자 초기화
    })),
}));

// 승자 계산하는 함수
function calculateWinner(board) {
  const lines = [
    [0, 1, 2],
    [3, 4, 5],
    [6, 7, 8], // 가로
    [0, 3, 6],
    [1, 4, 7],
    [2, 5, 8], // 세로
    [0, 4, 8],
    [2, 4, 6], // 대각선
  ];

  // 각 가능한 승리 조건 확인
  for (const [a, b, c] of lines) {
    if (board[a] && board[a] === board[b] && board[a] === board[c]) {
      return board[a]; // 승자가 있으면 해당 값 반환
    }
  }
  return null; // 승자 없으면 null 반환
}

export default useGameStore;
