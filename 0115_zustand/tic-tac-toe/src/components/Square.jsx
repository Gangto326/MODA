const Square = ({ value, onClick }) => {
  return (
    <button
      onClick={onClick} // 클릭 시 onClick 함수 실행
      style={{
        width: "100px",
        height: "100px",
        fontSize: "24px",
        display: "flex",
        justifyContent: "center",
        alignItems: "center",
        border: "1px solid black",
        backgroundColor: "#f9f9f9",
      }}
    >
      {value} {/* 칸에 표시되는 값 (X 또는 O) */}
    </button>
  );
};

export default Square;
