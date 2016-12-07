public class Osero {

	public static void main(String[] args) {
		int[][] board = { { 0, 0, 0, 0, 1, 0, 0, 0 }, { 0, 0, 0, 0, 1, 1, 1, 1 }, { 0, 0, 0, 0, 0, 1, 1, 1 },
				{ 0, 1, 1, 0, 1, 1, 0, 1 }, { 0, 0, 1, 1, 0, 1, 1, 1 }, { 0, 0, 1, 1, 0, 0, 1, 1 },
				{ 1, 1, 1, 1, 1, 1, 0, 1 }, { 1, 0, 0, 0, 0, 0, 0, 0 } };

		int white = 0;
		int black = 0;

		// 普通に数えた数
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {
				if(board[i][j] == 0) {
					white++;
				} else {
					black++;
				}
			}
		}

		// ハンデ
		int newBlack = (int) (black * 1.2);
		int newWhite = white - (newBlack - black);

		//並び替え
		int[][] resultBoard = new int[8][8];
		for (int i = 0; i < resultBoard.length; i++) {
			for (int j = 0; j < resultBoard[i].length; j++) {
				if(i * 8 + j < newWhite) {
					resultBoard[i][j] = 0;
				} else {
					resultBoard[i][j] = 1;
				}
			}
		}

		printBoard(resultBoard);
		// System.out.println(black + "対" + white + "で");
		// if(black > white) {
		// System.out.println("黒の勝ち");
		// } else if(black < white) {
		// System.out.println("白の勝ち");
		// } else {
		// System.out.println("引き分け");
		// }
	}

	static void printBoard(int[][] board) {
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {
				System.out.print(board[i][j] + ",");
			}
			System.out.println();
		}
	}
}
