package edu.smith.cs.csc212.p5;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

import edu.smith.cs.csc212.p5.SudokuMain.SudokuCell;
import me.jjfoley.gfx.GFX;
import me.jjfoley.gfx.IntPoint;
import me.jjfoley.gfx.TextBox;

public class SudokuMain extends GFX {
	List<SudokuCell> grid = new ArrayList<SudokuCell>();
	List<TextboxCell> cgrid = new ArrayList<TextboxCell>();
	TextBox message = new TextBox("Hello World!");
	SudokuState state = SudokuState.Input;

	public SudokuMain() {
		this.setupGame();
	}
	
	static class TextboxCell{
		boolean mouseHover;
		Rectangle2D area;
		int number;
		TextBox select;
		
		public TextboxCell(int number, int x, int y, int w, int h) {
			this.area = new Rectangle2D.Double(x,y,w,h);
			this.mouseHover = false;
			this.number=number;
			this.select = new TextBox(""+number);
		}
		
		
		public void draw(Graphics2D g) {
			if (mouseHover) {
				g.setColor(Color.YELLOW);
			} else {
				g.setColor(Color.GRAY);
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
//		int symbol;
		TextBox display;
		
		public SudokuCell(int x, int y, int w, int h) {
			this.area = new Rectangle2D.Double(x,y,w,h);
			this.mouseHover = false;
//			this.symbol = 0;
			this.display = new TextBox("X");
		}
		
		public int getValue() {
			return game.board[i][j];	
		}
		
		public void setValue(int x) {
			if(game.isModifiable(i, j) && game.isValid(i,j,x)) {
				game.board[i][j] = x;
			}
		}
	
		
		public void draw(Graphics2D g) {
			if (mouseHover) {
				g.setColor(Color.green);
			} else {
				g.setColor(Color.GRAY);
			}
			g.fill(this.area);

	            
			switch(this.game.board[i][j]) {	
			case 0:
				this.display.setString("_");
				break;
			//dead
//			case 1:
//				
//						this.display.setString(""+game.board[i][j]);
//					}
//				}
			default:
				this.display.setString(""+this.getValue());
				break;
			}
			
			if (mouseHover) {
				g.setColor(Color.GRAY);
			} else {
				g.setColor(Color.GREEN);
			}
			
			this.display.centerInside(this.area);
			this.display.setFontSize(40.0);
			this.display.setColor(Color.black);
			this.display.draw(g);
			
		}

		public boolean contains(IntPoint mouse) {
			if (mouse == null) {
				return false;
			}
			return this.area.contains(mouse);
		}
	 }
	public void setupGame() {
		int size = this.getWidth()/10;
		for(int x=0; x<9;x++) {
			for(int y=0; y<9;y++) {
				this.grid.add(new SudokuCell(x*size, y*size, 
						size-2, size-2));
				
			}
		}
		
//		for(int i=0;i<9;i++) {
//			for(int j=0;j<9;j++) {
//				
//			}
		for(int x=0;x<9;x++) {
				this.cgrid.add(new TextboxCell(x + 1, x * size, this.getHeight() * 93 / 100, size - 3, size / 2));
			}

		}
	
	SudokuCell selected = null;
	@Override
	public void update(double time) {
		IntPoint mouse = this.getMouseLocation();
		IntPoint click = this.processClick();
		for(SudokuCell cell: this.grid) {		
			cell.mouseHover = cell.contains(mouse);
			if (cell.contains(click)) {
				selected = cell;
				break;
				//cell.symbol++;
				//show = true;
//				this.state = state.Input;
			}
		 }
		
		if (selected != null) {
			selected.mouseHover = true;
		
			for(TextboxCell numbercell: this.cgrid) {
				numbercell.mouseHover = numbercell.contains(mouse);
				if(numbercell.contains(click)) {
					selected.setValue(numbercell.number);
					selected = null;
				}
			}
		}
		
		//if we click on a modula, we change it to modula message.
		
		switch(this.state) {
		case Input:
			this.message.setString("Select a Number From Below:");
			break;
		case Modula:
			this.message.setString("Can't change the Modula");
			break;
		case PlayerWin:
			this.message.setString("You won!");
			break;
		default:
			break;
	}
}


	@Override
	public void draw(Graphics2D g) {
		g.setColor(Color.white);
		g.fillRect(0, 0, getWidth(), getHeight());
		
		//draw black line to differentiate 
		int size = this.getWidth()/10;
		for(int x=0; x<9;x+=3) {
			for(int y=0; y<9;y++) {
				g.setColor(Color.black);
				g.fillRect(x*size-2, y*size,2, size);
			} }
		for(int x=0; x<9;x++) {
			for(int y=0; y<9;y+=3) {
				g.setColor(Color.black);
				g.fillRect(x*size-2, y*size-2, size, 2);}
			}
		
		
		for(SudokuCell cell: this.grid) {
			cell.draw(g);} 
		
		if (this.selected != null) {
			for(TextboxCell numbercell: this.cgrid) {
				numbercell.draw(g);
			}
			
			Rectangle2D centerText = new Rectangle2D.Double(
					0,this.getHeight()*86/100, this.getWidth(), this.getHeight()/10);
			this.message.setFontSize(15.0);
			this.message.setColor(Color.black);
			this.message.centerInside(centerText);
			this.message.draw(g);
		}
		}
	

	public static void main(String[] args) {
		System.out.println("Sudoku Started!");
		GFX app = new SudokuMain();
		app.start();
	}
}
