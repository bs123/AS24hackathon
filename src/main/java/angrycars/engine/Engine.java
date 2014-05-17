package angrycars.engine;

import java.util.Random;

public class Engine {
	
	private static final double TIME_TO_SPREAD = 2.0;
	private static final int MAX_X = 51;
	private static final int MAX_Y = 51;
	private static final double SPREAD_PROBABILITY = 0.1;
	private static final double DECAY_PROBABILITY = 0.05;
	
	Cell[][] grid = new Cell[MAX_Y][MAX_X];
	
	public Engine() {
		for (int y = 0; y < MAX_Y; y++) {
			for (int x = 0; x < MAX_X; x++) {
				grid[y][x] = new Cell();
			}
		}
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (int y = 0; y < MAX_Y; y++) {
			for (int x = 0; x < MAX_X; x++) {
				String c = grid[y][x].toString();
				sb.append(c);
			}
			sb.append('\n');
		}
		return sb.toString();
	}
	
	public void insertPoint(int x, int y, int team) {
		grid[y][x].team = team;
		grid[y][x].timeToSpread = TIME_TO_SPREAD;
	}
	
	public void step(double dt) {
		for (int y = 0; y < MAX_Y; y++) {
			for (int x = 0; x < MAX_X; x++) {
				decay(y, x);
			}
		}

		for (int y = 0; y < MAX_Y; y++) {
			for (int x = 0; x < MAX_X; x++) {
				if (grid[y][x].team != 0) {
					spreadInfluence(y, x);
				}
			}
		}

		for (int y = 0; y < MAX_Y; y++) {
			for (int x = 0; x < MAX_X; x++) {
				if (grid[y][x].candidateTeam != 0) {
					convertCell(y, x);
				}
			}
		}
	}
	
	private void decay(int y, int x) {
		if (grid[y][x].team != 0) {
			if (rand.nextDouble() < DECAY_PROBABILITY) {
				grid[y][x].team = 0;
			}
		}
	}

	private Random rand = new Random(123);
	
	private void spreadInfluence(int y, int x) {
      int t = grid[y][x].team;
      for (int dy = -1; dy <= 1; dy++) {
          for (int dx = -1; dx <= 1; dx++) {
        	  int nx = x + dx;
        	  int ny = y + dy;
        	  if (nx < 0 || nx >= MAX_X || ny < 0 || ny >= MAX_Y) {
        		  continue;
        	  }
        	  if (rand.nextDouble() > SPREAD_PROBABILITY) {
        		  continue;
        	  }
        	  if (grid[ny][nx].team == 0) {
        		grid[ny][nx].candidateTeam = t;
        	  }
          }
      }
	}
	
	private void convertCell(int y, int x) {
		Cell cell = grid[y][x];
		if (cell.candidateTeam != 0) {
			cell.team = cell.candidateTeam;
			cell.candidateTeam = 0;
		}
	}

	public Cell[][] getGrid() {
		return grid;
	}
	
	private static class Cell {
		int team = 0;
		int candidateTeam = 0;
		double timeToSpread = 0;
		
		public String toString() {
			if (team == 0) {
				return ".";
			} else {
				return Integer.toString(team);
			}
		}
	}
}
