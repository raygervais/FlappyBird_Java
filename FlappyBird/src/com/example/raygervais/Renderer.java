package com.example.raygervais;

import javax.swing.JPanel;
import java.awt.Graphics;

class Renderer extends JPanel{
	
	private static final long serialVersionUID = 1L;
	
	@Override
	protected void paintComponent(Graphics g){
		super.paintComponent(g);
		
		FlappyBird.flappyBird.repaint(g);
	}
}
