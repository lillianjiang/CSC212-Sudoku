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
	TextBox message = new TextBox("Hello World!");
	SudokuState state = SudokuState.Input;

	public SudokuMain() {
		this.setupGame();
	}

	static class SudokuCell {
		Rectangle2D area;
		boolean mouseHover;
		int symbol;
		TextBox display;
		
		public SudokuCell(int x, int y, int w, int h) {
			this.area = new Rectangle2D.Double(x,y,w,h);
			this.mouseHover = false;
			this.symbol = 0;
			this.display = new TextBox("X");
		}
		
//		public boolean inPlay() {
//			return symbol == SudokuState.Modula;
//		}
		
		public void draw(Graphics2D g) {
			if (mouseHover) {
				g.setColor(Color.green);
			} else {
				g.setColor(Color.GRAY);
			}
			g.fill(this.area);
			
			int[][] model = new int[][] {
	            {3, 0, 6, 5, 0, 8, 4, 0, 0},
	            {5, 2, 0, 0, 0, 0, 0, 0, 0},
	            {0, 8, 7, 0, 0, 0, 0, 3, 1},
	            {0, 0, 3, 0, 1, 0, 0, 8, 0},
	            {9, 0, 0, 8, 6, 3, 0, 0, 5},
	            {0, 5, 0, 0, 9, 0, 6, 0, 0},
	            {1, 3, 0, 0, 0, 0, 2, 5, 0},
	            {0, 0, 0, 0, 0, 0, 0, 7, 4},
	            {0, 0, 5, 2, 0, 6, 3, 0, 0} }; 
	            
			switch(this.symbol) {	
			case 0:
				this.display.setString("_");
				break;
			//dead
			case 1:
				for(int i=0;i<9;i++) {
					for(int j=0;j<9;j++) {
						this.display.setString(""+model[i][j]);
					}
				}
			default:
				this.display.setString(""+this.symbol);
				break;
			}
			
			if (mouseHover) {
				g.setColor(Color.GRAY);
			} else {
				g.setColor(Color.GREEN);
			}
			
			this.display.centerInside(this.area);
			this.display.setFontSize(45.0);
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
	
	
	}
	
	@Override
	public void update(double time) {
		IntPoint mouse = this.getMouseLocation();
		IntPoint click = this.processClick();
		Boolean show = false;
		for(SudokuCell cell: this.grid) {		
			cell.mouseHover = cell.contains(mouse);
			if (cell.contains(click)) {
				cell.symbol++;
				show = true;
//				this.state = state.Input;
			}
		 }
		
		switch(this.state) {
		case Input:
			this.message.setString("select number from below:");
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
		
		
		Rectangle2D centerText = new Rectangle2D.Double(
				0,this.getHeight()*9/10, this.getWidth(), this.getHeight()/10);
		this.message.setFontSize(20.0);
		this.message.setColor(Color.black);
	//	this.message.centerInside(centerText);
		this.message.draw(g);
		}
	

	public static void main(String[] args) {
		System.out.println("Sudoku Started!");
		GFX app = new SudokuMain();
		app.start();
	}
}
