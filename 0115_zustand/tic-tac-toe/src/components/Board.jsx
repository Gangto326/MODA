import useGameStore from "../store/gameStore"; // Zustand 상태 훅 불러오기
import Square from "./Square"; // Square 컴포넌트 불러오기

const Board = () => {
  const { board, playMove, winner, resetGame } = useGameStore(); // Zustand 상태 훅에서 필요한 상태 가져오기

  return (
    <div style={{ textAlign: "center", marginTop: "20px" }}>
      <h1>Tic-Tac-Toe</h1>
      <div
        style={{
          display: "grid",
          gridTemplateColumns: "repeat(3, 100px)", // 3x3 그리드 설정
          gap: "5px",
          justifyContent: "center",
        }}
      >
        {/* 보드 상태에 따라 각 칸 렌더링 */}
        {board.map((value, index) => (
          <Square key={index} value={value} onClick={() => playMove(index)} />
        ))}
      </div>
      {winner && <h2>Winner: {winner}</h2>} {/* 승자가 있으면 승자 표시 */}
      <button
        onClick={resetGame}
        style={{ marginTop: "20px", padding: "10px 20px" }}
      >
        Restart {/* 게임 재시작 버튼 */}
      </button>
    </div>
  );
};

export default Board;
