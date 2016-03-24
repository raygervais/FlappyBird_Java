package com.example.raygervais;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.Timer;
import javax.swing.JFrame;

public class FlappyBird implements ActionListener, MouseListener, KeyListener {

	public static FlappyBird flappyBird;

	public final int WIDTH = 1000, HEIGHT = 1000;

	public Renderer renderer;

	public Rectangle bird;

	public boolean gameOver, started;

	public int ticks, yMotion, score;

	public ArrayList<Rectangle> columns;

	public Random rand = new Random();

	public FlappyBird() {
		JFrame jFrame = new JFrame();
		jFrame.setTitle("Flappy Bird");

		// This will be the refresh rate for each frame.
		Timer timer = new Timer(20, this);

		// Create renderer for graphics
		renderer = new Renderer();
		jFrame.add(renderer);
		
		//Create listeners
		jFrame.addMouseListener(this);
		jFrame.addKeyListener(this);
		
		// Make it so that when closed, program terminates
		jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Create new jFrame with predefined size, not sizable
		jFrame.setSize(WIDTH, HEIGHT);
		jFrame.setResizable(true);

		// Make the jFrame visible
		jFrame.setVisible(true);

		// Create Bird size
		bird = new Rectangle(WIDTH / 2, HEIGHT / 2, 40, 40);

		// Create Columns
		columns = new ArrayList<Rectangle>();

		addColumn(true);
		addColumn(true);
		addColumn(true);
		addColumn(true);

		// Start the timer
		timer.start();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// Bird yMotion per render is done here
		ticks++;

		int speed = 10;

		// Screen will not refresh until started becomes true
		if (started) {

			// Move columns by reducing their xCoordinate
			for (int i = 0; i < columns.size(); i++) {
				Rectangle column = columns.get(i);
				column.x -= speed;
			}

			// Bird yMotion
			if (ticks % 2 == 0 && yMotion < 15) {
				yMotion += 2;
			}

			// iterate through the list
			for (int i = 0; i < columns.size(); i++) {
				Rectangle column = columns.get(i);

				// remove old columns
				if (column.x + column.width < 0) {
					columns.remove(column);

					// Add new columns
					if (column.y == 0) {
						addColumn(false);
					}
				}
			}

			// add the yMotion to bird's yCoordinate
			bird.y += yMotion;

			// Collision checking
			for (Rectangle column : columns) {
				if(column.y == 0 && bird.x + bird.width/2 > column.x + column.width/2-10 && bird.x + bird.width / 2 < column.x + column.width/2 + 10){
					score++;
				}
				
				if (column.intersects(bird)) {
					bird.x = column.x-bird.width;
					gameOver = true;
				}
			}

			if (bird.y > HEIGHT - 120 || bird.y < 0) {
				gameOver = true;
			}

		}
		// Redraws entire frame
		renderer.repaint();
	}

	public void paintColumn(Graphics g, Rectangle c) {
		g.setColor(Color.green.darker());
		g.fillRect(c.x, c.y, c.width, c.height);
	}

	public void addColumn(boolean start) {
		int space = 300;
		int width = 100;
		int height = 50 + rand.nextInt(300);

		if (start) {
			columns.add(new Rectangle(WIDTH + width + columns.size() * 300, HEIGHT - height - 120, width, height));
			columns.add(new Rectangle(WIDTH + width + (columns.size() - 1) * 300, 0, width, HEIGHT - height - space));
		} else {
			columns.add(new Rectangle(columns.get(columns.size() - 1).x + 600, HEIGHT - height - 120, width, height));
			columns.add(new Rectangle(columns.get(columns.size() - 1).x, 0, width, HEIGHT - height - space));
		}
	}

	public void repaint(Graphics g) {
		// Sky
		g.setColor(Color.cyan);
		g.fillRect(0, 0, WIDTH, HEIGHT);

		// Ground
		g.setColor(Color.orange);
		g.fillRect(0, HEIGHT - 120, WIDTH, 120);

		// Grass
		g.setColor(Color.green);
		g.fillRect(0, HEIGHT - 120, WIDTH, 20);

		// Bird
		g.setColor(Color.red);
		g.fillRect(bird.x, bird.y, bird.width, bird.height);

		// Columns
		for (Rectangle column : columns) {
			paintColumn(g, column);
		}

		// Gameover
		g.setColor(Color.white);
		g.setFont(new Font("Arial", 1, 100));

		if (gameOver) {
			g.drawString("GameOver!", 150, HEIGHT / 2 - 50);
		}

		if (!gameOver && !started) {
			g.drawString("Click to Start!", 150, HEIGHT / 2 - 50);
		}
		
		if(!gameOver && started){
			g.drawString(String.valueOf(score), WIDTH/2  -25, 100);
		}

	}

	// Called via mouse click
	public void jump() {
		if (gameOver) {

			bird = new Rectangle(WIDTH / 2, HEIGHT / 2, 40, 40);

			// Refresh columns
			columns.clear();
			yMotion = 0;
			score = 0;

			addColumn(true);
			addColumn(true);
			addColumn(true);
			addColumn(true);

			gameOver = false;
		}

		if (!started) {
			started = true;
		} else if (!gameOver) {

			if (yMotion > 0) {
				yMotion = 0;
			}

			yMotion -= 10;

		}
	}

	public static void main(String[] args) {
		System.out.println("Starting Game");

		// Creating new instance of flappyBird
		flappyBird = new FlappyBird();

	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		jump();
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyPressed(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_SPACE){
			jump();
		}
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

}
