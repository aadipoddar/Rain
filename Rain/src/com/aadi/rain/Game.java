package com.aadi.rain;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import javax.swing.JFrame;

import com.aadi.rain.entity.mob.Player;
import com.aadi.rain.graphics.Font;
import com.aadi.rain.graphics.Screen;
import com.aadi.rain.graphics.ui.UIManager;
import com.aadi.rain.input.Keyboard;
import com.aadi.rain.input.Mouse;
import com.aadi.rain.level.Level;
import com.aadi.rain.level.TileCoordinate;

public class Game extends Canvas implements Runnable {
	private static final long serialVersionUID = 1L;

	private static int width = 300;
	private static int height = 168;
	private static int scale = 3;
	private static String title = "Rain";

	private Thread thread;
	private JFrame frame;
	private Keyboard key;
	private Level level;
	private Player player;
	private boolean running = false;

	private static UIManager uiManager;

	private Screen screen;
	private Font font;

	private BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
	private int[] pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();

	public Game() {
		Dimension size = new Dimension(width * scale, height * scale);
		setPreferredSize(size);

		screen = new Screen(width, height);
		uiManager = new UIManager();
		frame = new JFrame();
		key = new Keyboard();
		level = Level.spawn;
		TileCoordinate playerSpawn = new TileCoordinate(19, 42);
		player = new Player(playerSpawn.x(), playerSpawn.y(), key);
		level.add(player);
		font = new Font();

		addKeyListener(key);

		Mouse mouse = new Mouse();
		addMouseListener(mouse);
		addMouseMotionListener(mouse);
	}

	public static int getWindowWidth() {
		return width * scale;
	}

	public static int getWindowHeight() {
		return height * scale;
	}

	public static UIManager getUIManager() {
		return uiManager;
	}

	public synchronized void start() { // Starts the thread which are series of events that are executed in the background
		running = true;
		thread = new Thread(this, " Display");
		thread.start();
	}

	public synchronized void stop() { // Stops the thread which are series of events that are executed in the background
		running = false;

		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void run() { // Automatically called on Thread Start

		long lastTime = System.nanoTime(); // Computer's Current Time
		long timer = System.currentTimeMillis();
		final double ns = 1000000000.0 / 60.0;
		double delta = 0;
		int frames = 0;
		int updates = 0;

		requestFocus();

		while (running) {

			long now = System.nanoTime(); // Current loops time 
			delta += (now - lastTime) / ns; // Calculates the Time taken to Run the while loop
			lastTime = now;

			while (delta >= 1) {
				update();

				updates++;
				delta--;
			}

			render();
			frames++;

			if (System.currentTimeMillis() - timer > 1000) {
				timer += 1000;

				frame.setTitle(title + "  |  " + updates + " ups, " + frames + " fps");

				updates = 0;
				frames = 0;
			}
		}
		stop();
	}

	public void update() {
		key.update();
		level.update();
		uiManager.update();
	}

	public void render() {
		BufferStrategy bs = getBufferStrategy();

		if (bs == null) {
			createBufferStrategy(3);
			return;
		}

		screen.clear();

		double xScroll = player.getX() - screen.width / 2;
		double yScroll = player.getY() - screen.height / 2;

		level.render((int) xScroll, (int) yScroll, screen);
		uiManager.render(screen);

		for (int i = 0; i < pixels.length; i++) {
			pixels[i] = screen.pixels[i];
		}

		Graphics g = bs.getDrawGraphics();
		g.drawImage(image, 0, 0, getWidth(), getHeight(), null);
		//g.fillRect(Mouse.getX() - 32, Mouse.getY() - 32, 64, 64);
		//if (Mouse.getButton() != -1) g.drawString("Button: " + Mouse.getButton(), 80, 80);
		g.dispose();
		bs.show();
	}

	public static void main(String[] args) {
		Game game = new Game();

		game.frame.setResizable(false);
		game.frame.setTitle(Game.title);
		game.frame.add(game);
		game.frame.pack();
		game.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		game.frame.setLocationRelativeTo(null);
		game.frame.setVisible(true);

		game.start();
	}

}
