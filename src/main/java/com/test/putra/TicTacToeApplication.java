package com.test.putra;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.test.putra.model.Play;
import com.test.putra.model.PlayHistory;
import com.test.putra.repository.PlayHistoryRepository;
import com.test.putra.repository.PlayRepository;
import com.test.putra.service.GameService;

@SpringBootApplication
public class TicTacToeApplication implements CommandLineRunner {
	
	private PlayRepository playRepo;
	private PlayHistoryRepository playHistoryRepo;
	
	public TicTacToeApplication(PlayRepository playRepo, PlayHistoryRepository playHistoryRepo) {
		this.playRepo = playRepo;
		this.playHistoryRepo = playHistoryRepo;
	}

	public static void main(String[] args) {
		SpringApplication.run(TicTacToeApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		System.out.println("Let's play Tic Tac Toe!");

		try (Scanner in = new Scanner(System.in)) {
			System.out.print("Please input the board size (3 for 3x3, 5 for 5x5..) : ");
			int boardSize = Integer.parseInt(in.nextLine());

			Play play = Play.builder()
					.boardSize(boardSize)
					.build();
			Play savedPlay = playRepo.save(play);
			
			int n = boardSize;

			char[][] board = new char[n][n];

			for(int i = 0; i < n; i++) {
				for(int j = 0; j < n; j++) {
					board[i][j] = '-';
				}
			}

			List<PlayHistory> histories = new ArrayList<>();

			System.out.print("Player 1, what is your name? : ");
			String p1 = in.nextLine();
			System.out.print("Player 2, what is your name? : ");
			String p2 = in.nextLine();

			boolean player1 = true;
			String activePlayerName = "";

			boolean gameEnded = false;
			while(!gameEnded) {
				
				PlayHistory.PlayHistoryBuilder historyBuilder = PlayHistory.builder();
				
				GameService.drawBoard(board);

				if(player1) {
					activePlayerName = p1;
					System.out.println(p1 + "'s Turn (x):");
				} else {
					activePlayerName = p2;
					System.out.println(p2 + "'s Turn (o):");
				}

				historyBuilder.player(activePlayerName);

				char c = '-';
				if(player1) {
					c = 'x';
				} else {
					c = 'o';
				}

				int row = 0;
				int col = 0;

				while(true) {
					System.out.print("Enter a row number : ");
					row = in.nextInt();
					System.out.print("Enter a column number : ");
					col = in.nextInt();

					if(row < 0 || col < 0 || row >= n || col >= n) {
						System.out.println("This position is off the bounds of the board! Try again.");

					} else if(board[row][col] != '-') {
						System.out.println("Someone has already made a move at this position! Try again.");

					} else {
						break;
					}

				}

				board[row][col] = c;
				historyBuilder.rowNum(row);
				historyBuilder.columnNum(col);
				historyBuilder.isWin(Boolean.FALSE);
				historyBuilder.isTie(Boolean.FALSE);

				if(GameService.playerHasWon(board) == 'x') {
					System.out.println(p1 + " has won!");
					historyBuilder.isWin(Boolean.TRUE);
					gameEnded = true;
				} else if(GameService.playerHasWon(board) == 'o') {
					System.out.println(p2 + " has won!");
					historyBuilder.isWin(Boolean.TRUE);
					gameEnded = true;
				} else {

					if(GameService.boardIsFull(board)) {
						System.out.println("It's a tie!");
						historyBuilder.isTie(Boolean.TRUE);
						gameEnded = true;
					} else {
						player1 = !player1;
					}

				}
				
				historyBuilder.playId(savedPlay.getId());
				historyBuilder.execTimestamp(LocalDateTime.now());
				PlayHistory playHistory = historyBuilder.build();
				histories.add(playHistory);

			}

			playHistoryRepo.saveAll(histories);

			GameService.drawBoard(board);

			// show play history
			List<PlayHistory> playResult = playHistoryRepo.findByPlayId(savedPlay.getId());
			System.out.println();
			System.out.println("Play History :");
			System.out.printf("Board size : %d x %d%n", savedPlay.getBoardSize(), savedPlay.getBoardSize());
			System.out.println("Player\t\tRow\t\tColumn\t\tisWin\t\tisTie\t\tTimestamp");
			for (PlayHistory his : playResult) {
				System.out.printf("%s\t\t%d\t\t%d\t\t%s\t\t%s\t\t%s%n",
						his.getPlayer(),
						his.getRowNum(),
						his.getColumnNum(),
						his.getIsWin().toString(),
						his.getIsTie().toString(),
						his.getExecTimestamp().toString());
						
			}
		}
		
	}

}
