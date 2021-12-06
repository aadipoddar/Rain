package com.aadi.rain.entity.mob;

import com.aadi.rain.graphics.Screen;
import com.aadi.rain.graphics.Sprite;
import com.aadi.rain.input.Keyboard;

public class Player extends Mob {

	private Keyboard input;

	public Player(Keyboard input) {
		this.input = input;
	}

	public Player(int x, int y, Keyboard input) {
		this.x = x;
		this.y = y;
		this.input = input;
	}

	public void update() {
		int xa = 0, ya = 0;

		if (input.up) ya--;
		if (input.down) ya++;
		if (input.left) xa--;
		if (input.right) xa++;

		if (xa != 0 || ya != 0) move(xa, ya);
	}

	public void render(Screen screen) {
		screen.renderPlayer(x - 16, y - 16, Sprite.player);
	}

}