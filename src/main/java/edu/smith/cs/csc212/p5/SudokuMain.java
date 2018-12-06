package edu.smith.cs.csc212.p5;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

import me.jjfoley.gfx.GFX;
import me.jjfoley.gfx.IntPoint;
import me.jjfoley.gfx.TextBox;

public class SudokuMain extends GFX {
	List<SudokuCell> grid = new ArrayList();

	public SudokuMain() {
		this.setupGame();
	}

	static class SudokuCell {
		Rectangle2D area;
		boolean mouseHover;
		TextBox display;
		
		public SudokuCell(int x, int y, int w, int h) {
			this.area = new Rectangle2D.Double(x,y,w,h);
			this.mouseHover = false;	
//			this.display = new TextBox("X");
		}
		
		public void draw(Graphics2D g) {
			if (mouseHover) {
				g.setColor(Color.green);
			} else {
				g.setColor(Color.GRAY);
			}
			g.fill(this.area);
			
//			this.display.centerInside(this.area);
//			this.display.setFontSize(72.0);
//			this.display.setColor(Color.black);
//			this.display.draw(g);
//		}
//	}
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
		//draw black line to differentiate 
	
	}
	
	@Override
	public void update(double time) {
		IntPoint mouse = this.getMouseLocation();
		for(SudokuCell cell: this.grid) {		
			cell.mouseHover = cell.contains(mouse);
		 }
	}

	@Override
	public void draw(Graphics2D g) {
		g.setColor(Color.white);
		g.fillRect(0, 0, getWidth(), getHeight());
		
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
		}
	

	public static void main(String[] args) {
		System.out.println("Sudoku Started!");
		GFX app = new SudokuMain();
		app.start();
	}
}
