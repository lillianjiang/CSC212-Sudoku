package edu.smith.cs.csc212.p5;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import edu.smith.cs.csc212.p5.SudokuMain.SudokuCell;
import me.jjfoley.gfx.GFX;
import me.jjfoley.gfx.IntPoint;
import me.jjfoley.gfx.TextBox;

public class SudokuMain extends GFX {
	List<SudokuCell> grid = new ArrayList<SudokuCell>();
	List<TextboxCell> cgrid = new ArrayList<TextboxCell>();
	TextBox message = new TextBox("Hello World!");
	SudokuState state = SudokuState.Input;

	static Color base = new Color(255, 242, 230);
	static Color pinky = new Color(195, 211, 217);
	static Color mod = new Color(217, 184, 185);
	static Color selectcolor = new Color(153, 175, 191);
	
	String level = "";

	public SudokuMain() {
		this.setupGame();
	}

	static class TextboxCell {
		boolean mouseHover;
		Rectangle2D area;
		int number;
		TextBox select;

		public TextboxCell(int number, int x, int y, int w, int h) {
			this.area = new Rectangle2D.Double(x, y, w, h);
			this.mouseHover = false;
			this.number = number;
			this.select = new TextBox("" + number);
		}

		public void draw(Graphics2D g) {
			if (mouseHover) {
				g.setColor(selectcolor);
			} else {
				g.setColor(base);
			}
			g.fill(this.area);

			this.select.centerInside(this.area);
			this.select.setFontSize(30.0);
			this.select.setColor(Color.black);
			this.select.draw(g);

		}

		public boolean contains(IntPoint mouse) {
			if (mouse == null) {
				return false;
			}
			return this.area.contains(mouse);
		}

	}

	static class SudokuCell {
		Rectangle2D area;
		boolean mouseHover;
		SudokuGame game;
		int i;
		int j;
		TextBox display;
		final boolean blank;

		public SudokuCell(SudokuGame game, int i, int j, int x, int y, int w, int h) {
			this.area = new Rectangle2D.Double(x, y, w, h);
			this.mouseHover = false;
			this.i = i;
			this.j = j;
			this.game = game;
			this.display = new TextBox("");
			this.blank = game.isModifiable(i, j);
		}

		public int getValue(int i, int j) {
			return game.board[i][j];
		}

		public void setValue(int x) {
			if (game.isModifiable(i, j) && game.isValid(i, j, x)) {
				game.board[i][j] = x;
			}
		}

		public void draw(Graphics2D g) {
			if (mouseHover && blank) {
				g.setColor(pinky);
			} else if (mouseHover && !blank) {
				g.setColor(mod);
			} else {
				g.setColor(base);
			}
			g.fill(this.area);

			switch (this.game.board[i][j]) {
			case 0:
				this.display.setString("_");
				break;
			default:
				// int size = 50;
				// g.fillRect(i*size, j*size, size-2, size-2);
				this.display.setString("" + this.getValue(i, j));
				Rectangle2D spot = new Rectangle2D.Double(i * 50, j * 50, 50 - 2, 50 - 2);
				this.display.centerInside(spot);
				this.display.setFontSize(19.0);
				this.display.setColor(Color.black);
				this.display.draw(g);
				break;
			}
		}

		public boolean contains(IntPoint mouse) {
			if (mouse == null) {
				return false;
			}
			return this.area.contains(mouse);
		}
	}

	public void setupGame() {
		this.level =  JOptionPane
		          .showInputDialog("Which level you want to try? \n easy/medium/difficult");
		SudokuGame game = null;
		if (this.level.equals("easy")) {
			 game = new SudokuGame(SudokuGame.easy);
		} else if (this.level.equals("medium")) {
			 game = new SudokuGame(SudokuGame.medium);
		} else {
			 game = new SudokuGame(SudokuGame.difficult);
		}
		int size = this.getWidth() / 10;
		for (int x = 0; x < 9; x++) {
			for (int y = 0; y < 9; y++) {
				this.grid.add(new SudokuCell(game, x, y, x * size, y * size, size - 2, size - 2));
			}
		}

		for (int x = 0; x < 9; x++) {
			this.cgrid.add(new TextboxCell(x + 1, x * size, this.getHeight() * 93 / 100, size - 3, size / 2));
		}

	}

	SudokuCell selected = null;

	@Override
	public void update(double time) {
		IntPoint mouse = this.getMouseLocation();
		IntPoint click = this.processClick();
		for (SudokuCell cell : this.grid) {
			cell.mouseHover = cell.contains(mouse);
			if (cell.contains(click)) {
				selected = cell;
				break;

			}
		}

		/*
		 * Once We select and clink a cell, we want its to stay green(stay true)
		 * whenever we move the mouse to elsewhere.
		 */
		if (selected != null) {
			selected.mouseHover = true;
		}
		for (TextboxCell numbercell : this.cgrid) {
			numbercell.mouseHover = numbercell.contains(mouse);
			if (numbercell.contains(click)) {
				if (selected.game.isValid(selected.i, selected.j, numbercell.number) && selected.blank) {
					this.state = state.Input;
					selected.setValue(numbercell.number);
				} else if (!selected.blank) {
					this.state = state.Modula;
				} else if (!selected.game.isValid(selected.i, selected.j, numbercell.number)) {
					this.state = state.Wrong;
				}

				selected = null;
			}
		}
	

		/*
		 * When we click on different type of cell,
		 * the message will show differently.
		 * 
		 */
		switch (this.state) {
		case Input:
			this.message.setString("Select a Number From Below:");
			break;
		case Wrong:
			this.message.setString("It is not a good idea.");
			break;
		case Modula:
			this.message.setString("Modula is NOT changable, Player!");
			break;
		case PlayerWin:
			this.message.setString("You won!");
			break;
		default:
			this.message.setString("Select a Number From Below:");
			break;
		}
	}

	@Override
	public void draw(Graphics2D g) {
		g.setColor(Color.white);
		g.fillRect(0, 0, getWidth(), getHeight());

		// draw black line to differentiate 3*3 grid
		int size = this.getWidth() / 10;
		for (int x = 0; x < 9; x += 3) {
			for (int y = 0; y < 9; y++) {
				g.setColor(Color.black);
				g.fillRect(x * size - 2, y * size, 2, size);
			}
		}
		for (int x = 0; x < 9; x++) {
			for (int y = 0; y < 9; y += 3) {
				g.setColor(Color.black);
				g.fillRect(x * size - 2, y * size - 2, size, 2);
			}
		}

		for (SudokuCell cell : this.grid) {
			cell.draw(g);
		}

		/*
		 * Only show the Textbox of number when we click on a cell
		 */
		if (this.selected != null) {
			for (TextboxCell numbercell : this.cgrid) {
				numbercell.draw(g);
			}
		}
		Rectangle2D centerText = new Rectangle2D.Double(0, this.getHeight() * 86 / 100, this.getWidth(),
				this.getHeight() / 10);
		this.message.setFontSize(15.0);
		this.message.setColor(Color.black);
		this.message.centerInside(centerText);
		this.message.draw(g);

	}

	public static void main(String[] args) {
		System.out.println("Sudoku Started!");
		GFX app = new SudokuMain();
		app.start();
	}
}
