package com.aadi.rain.graphics;

// This class loads our individual sprite
public class Sprite {

	public final int SIZE;
	private int x, y;
	public int[] pixels;
	private SpriteSheet sheet;

	// This is where we input the coordinates of the sprite we want to extract , We input the x and y Variables
	public static Sprite grass = new Sprite(16, 0, 0, SpriteSheet.tiles);
	public static Sprite voidSprite = new Sprite(16, 0x1B87E0);

	public static Sprite player0 = new Sprite(16, 0, 10, SpriteSheet.tiles);
	public static Sprite player1 = new Sprite(16, 1, 10, SpriteSheet.tiles);
	public static Sprite player2 = new Sprite(16, 0, 11, SpriteSheet.tiles);
	public static Sprite player3 = new Sprite(16, 1, 11, SpriteSheet.tiles);

	public static Sprite player = new Sprite(32, 0, 5, SpriteSheet.tiles);

	public Sprite(int size, int x, int y, SpriteSheet sheet) {
		SIZE = size;
		pixels = new int[SIZE * SIZE];
		this.x = x * size;
		this.y = y * size;
		this.sheet = sheet;
		load();
	}

	public Sprite(int size, int colour) {
		SIZE = size;
		pixels = new int[SIZE * SIZE];
		setColour(colour);
	}

	private void setColour(int colour) {
		for (int i = 0; i < SIZE * SIZE; i++) {
			pixels[i] = colour;
		}
	}

	// This is extracting a single sprite out of our sprite sheet 
	private void load() {
		for (int y = 0; y < SIZE; y++) {
			for (int x = 0; x < SIZE; x++) {
				pixels[x + y * SIZE] = sheet.pixels[(x + this.x) + (y + this.y) * sheet.SIZE];
			}
		}
	}

}