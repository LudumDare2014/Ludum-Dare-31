package com.webs.kevkawm.Player;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;

import net.bobmandude9889.Render.Camera;
import net.bobmandude9889.Resource.ResourceLoader;
import net.bobmandude9889.Resource.Sound;
import net.bobmandude9889.Window.Key;
import net.bobmandude9889.Window.Window;
import net.bobmandude9889.World.Entity;
import net.bobmandude9889.World.Location;
import net.bobmandude9889.World.OrthogonalLayer;
import net.bobmandude9889.World.OrthogonalMap;
import net.bobmandude9889.World.Tile;
import net.bobmandude9889.World.Velocity;
import net.bobmandude9889.World.VelocityHandler;
import net.bobmandude9889.World.World;

import com.webs.kevkawm.Blocks.Transformer;
import com.webs.kevkawm.Main.Main;

public class Player implements Entity {

	Location location;
	Velocity velocity;
	World world;
	Camera camera;
	BufferedImage matrix;
	Window window;
	double imageWidth = 50;
	double imageHeight = 70;

	double gravity = 0.014;
	double jumpForce = -0.4;

	double speed = 0.2;

	Sound jump;

	int direction = 1;

	public Player(Location location, World world, BufferedImage matrix, Camera camera, Window window) {
		this.location = location;
		this.world = world;
		this.matrix = matrix;
		this.velocity = new Velocity(0, 0);
		this.camera = camera;
		this.jump = ResourceLoader.getInstance().loadSound("jump.wav");
		this.jump.setVolume(50f);
		this.jump.play(50f);
		this.window = window;
	}

	@Override
	public Image getImage() {
		BufferedImage img = matrix.getSubimage(0, 0, (int) imageWidth, (int) imageHeight);
		if (!isOnGround()) {
			img = matrix.getSubimage((int) (imageWidth * 1), 0, (int) imageWidth, (int) imageHeight);
		} else if (velocity.x != 0) {
			int updates = 20;
			if (VelocityHandler.updates % updates > 0 && VelocityHandler.updates % updates < updates / 2) {
				img = matrix.getSubimage((int) (imageWidth * 2), 0, (int) imageWidth, (int) imageHeight);
			} else {
				img = matrix.getSubimage((int) (imageWidth * 3), 0, (int) imageWidth, (int) imageHeight);
			}
		}

		if (velocity.x < 0) {
			direction = 0;
		} else if (velocity.x > 0) {
			direction = 1;
		}

		if (direction == 0) {
			BufferedImage draw = new BufferedImage((int) imageWidth, (int) imageHeight, BufferedImage.TRANSLUCENT);
			Graphics g = draw.getGraphics();
			g.drawImage(img, (int) imageWidth, 0, (int) -imageWidth, (int) imageHeight, null);
			img = draw;
		}
		return img;
	}

	@Override
	public Location getLocation() {
		return location;
	}

	@Override
	public Velocity getVelocity() {
		onMove();
		return velocity;
	}

	@Override
	public void setLocation(Location location) {
		this.location = location;
	}

	@Override
	public void setVelocity(Velocity velocity) {
		this.velocity = velocity;
	}

	public boolean canMoveLeft() {
		for (OrthogonalLayer layer : ((OrthogonalMap) world.map).layers) {
			Tile tile = layer.getTile((int) Math.floor(location.x - 0.1), (int) Math.floor(location.y));
			if (tile != null && tile.getProperties().contains("collide")) {
				return false;
			}
			tile = layer.getTile((int) Math.floor(location.x - 0.1), (int) (Math.ceil(location.y + 1) - 0.1));
			if (tile != null && tile.getProperties().contains("collide")) {
				return false;
			}
		}
		return true;
	}

	public boolean canMoveRight() {
		for (OrthogonalLayer layer : ((OrthogonalMap) world.map).layers) {
			Tile tile = layer.getTile((int) Math.ceil(location.x + 0.1), (int) (Math.floor(location.y)));
			if (tile != null && tile.getProperties().contains("collide")) {
				return false;
			}
			tile = layer.getTile((int) Math.ceil(location.x + 0.1), (int) (Math.ceil(location.y + 1) - 0.1));
			if (tile != null && tile.getProperties().contains("collide")) {
				return false;
			}
		}
		return true;
	}

	public boolean canMoveUp() {
		for (OrthogonalLayer layer : ((OrthogonalMap) world.map).layers) {
			Tile tile = layer.getTile((int) (Math.floor(location.x + 0.1)), (int) (Math.floor(location.y)));
			if (tile != null && tile.getProperties().contains("collide")) {
				return false;
			}
			tile = layer.getTile((int) (Math.floor(location.x + (imageWidth / 50))), (int) (Math.floor(location.y)));
			if (tile != null && tile.getProperties().contains("collide")) {
				return false;
			}
			tile = layer.getTile((int) Math.floor(location.x + (imageWidth / 50) / 2), (int) Math.floor(location.y));
			if (tile != null && tile.getProperties().contains("collide")) {
				return false;
			}
		}
		return true;
	}

	public boolean isOnGround() {
		for (OrthogonalLayer layer : ((OrthogonalMap) world.map).layers) {
			Tile tile = layer.getTile((int) (Math.floor(location.x + 0.1)), (int) (Math.floor(location.y + (imageHeight / 50))));
			if (tile != null && tile.getProperties().contains("collide")) {
				return true;
			}
			tile = layer.getTile((int) (Math.floor(location.x + (imageWidth / 50))), (int) (Math.floor(location.y + (imageHeight / 50))));
			if (tile != null && tile.getProperties().contains("collide")) {
				return true;
			}
			tile = layer.getTile((int) ((Math.floor(location.x + (imageWidth / 50) / 2))), (int) (Math.floor(location.y + (imageHeight / 50))));
			if (tile != null && tile.getProperties().contains("collide")) {
				return true;
			}
		}
		return false;
	}

	public boolean touchDeadly() {
		for (OrthogonalLayer layer : ((OrthogonalMap) world.map).layers) {
			Tile tile = layer.getTile((int) (location.x), (int) (Math.floor(location.y + (imageHeight / 50)) - 0.2));
			if (tile != null && tile.getProperties().contains("deadly")) {
				return true;
			}
			tile = layer.getTile((int) (Math.floor(location.x + (imageWidth / 50))), (int) (Math.floor(location.y + (imageHeight / 50)) - 0.2));
			if (tile != null && tile.getProperties().contains("deadly")) {
				return true;
			}
			tile = layer.getTile((int) (Math.floor(location.x + (imageWidth / 50) / 2)), (int) (Math.floor(location.y + (imageHeight / 50)) - 0.2));
			if (tile != null && tile.getProperties().contains("deadly")) {
				return true;
			}
			tile = layer.getTile((int) Math.floor(location.x - 0.1), (int) (Math.floor(location.y) + 0.2));
			if (tile != null && tile.getProperties().contains("deadly")) {
				return true;
			}
			tile = layer.getTile((int) Math.ceil(location.x + 0.1), (int) (Math.floor(location.y) + 0.2));
			if (tile != null && tile.getProperties().contains("deadly")) {
				return true;
			}
			tile = layer.getTile((int) Math.floor(location.x + (imageWidth / 50) / 2), (int) (Math.floor(location.y) + 0.2));
			if (tile != null && tile.getProperties().contains("deadly")) {
				return true;
			}
		}
		return false;
	}

	public boolean touchDeadlyFeet() {
		for (OrthogonalLayer layer : ((OrthogonalMap) world.map).layers) {
			Tile tile = layer.getTile((int) (location.x), (int) (Math.floor(location.y + (imageHeight / 50)) - 0.2));
			if (tile != null && tile.getProperties().contains("deadly")) {
				return true;
			}
			tile = layer.getTile((int) (Math.floor(location.x + (imageWidth / 50))), (int) (Math.floor(location.y + (imageHeight / 50)) - 0.2));
			if (tile != null && tile.getProperties().contains("deadly")) {
				return true;
			}
			tile = layer.getTile((int) (Math.floor(location.x + (imageWidth / 50) / 2)), (int) (Math.floor(location.y + (imageHeight / 50)) - 0.2));
			if (tile != null && tile.getProperties().contains("deadly")) {
				return true;
			}
		}
		return false;
	}

	public boolean isInDeadly(){
		for (OrthogonalLayer layer : ((OrthogonalMap) world.map).layers) {
			Tile tile = layer.getTile((int) (Math.ceil(location.x + 0.2)), (int) (Math.floor(location.y + (imageHeight / 50)) - 1));
			if (tile != null && tile.getProperties().contains("deadly")) {
				return true;
			}
			tile = layer.getTile((int) (Math.floor(location.x + (imageWidth / 50))), (int) (Math.floor(location.y + (imageHeight / 50)) - 1));
			if (tile != null && tile.getProperties().contains("deadly")) {
				return true;
			}
			tile = layer.getTile((int) (Math.floor(location.x + (imageWidth / 50) / 2)), (int) (Math.floor(location.y + (imageHeight / 50)) - 1));
			if (tile != null && tile.getProperties().contains("deadly")) {
				return true;
			}
		}
		return false;
	}
	
	public boolean isInGround() {
		for (OrthogonalLayer layer : ((OrthogonalMap) world.map).layers) {
			Tile tile = layer.getTile((int) (Math.ceil(location.x + 0.2)), (int) (Math.floor(location.y + (imageHeight / 50)) - 1));
			if (tile != null && tile.getProperties().contains("collide")) {
				return true;
			}
			tile = layer.getTile((int) (Math.floor(location.x + (imageWidth / 50))), (int) (Math.floor(location.y + (imageHeight / 50)) - 1));
			if (tile != null && tile.getProperties().contains("collide")) {
				return true;
			}
			tile = layer.getTile((int) (Math.floor(location.x + (imageWidth / 50) / 2)), (int) (Math.floor(location.y + (imageHeight / 50)) - 1));
			if (tile != null && tile.getProperties().contains("collide")) {
				return true;
			}
		}
		return false;
	}

	public boolean touchEnd() {
		for (OrthogonalLayer layer : ((OrthogonalMap) world.map).layers) {
			Tile tile = layer.getTile((int) (location.x), (int) (Math.floor(location.y + (imageHeight / 50)) - 0.1));
			if (tile != null && tile.getProperties().contains("end")) {
				return true;
			}
			tile = layer.getTile((int) (Math.floor(location.x + (imageWidth / 50))), (int) (Math.floor(location.y + (imageHeight / 50)) - 0.1));
			if (tile != null && tile.getProperties().contains("end")) {
				return true;
			}
			tile = layer.getTile((int) (Math.floor(location.x + (imageWidth / 50) / 2)), (int) (Math.floor(location.y + (imageHeight / 50)) - 0.1));
			if (tile != null && tile.getProperties().contains("end")) {
				return true;
			}
			tile = layer.getTile((int) Math.floor(location.x - 0.1), (int) (Math.floor(location.y)));
			if (tile != null && tile.getProperties().contains("end")) {
				return true;
			}
			tile = layer.getTile((int) Math.ceil(location.x + 0.1), (int) (Math.floor(location.y)));
			if (tile != null && tile.getProperties().contains("end")) {
				return true;
			}
			tile = layer.getTile((int) Math.floor(location.x + (imageWidth / 50) / 2), (int) (Math.floor(location.y)));
			if (tile != null && tile.getProperties().contains("end")) {
				return true;
			}
		}
		return false;
	}

	public void onMove() {
		// Gravity
		if (Key.isPressed(Key.SPACE) && isOnGround()) {
			this.velocity.y = jumpForce;
			jump.play(60f);
		} else if (isOnGround() && velocity.y > 0) {
			this.velocity.y = 0;
			this.location.y = Math.floor(location.y) + 0.6;
			if (isInGround() && !canMoveLeft() && !canMoveRight()) {
				location.y--;
			}
		} else {
			this.velocity.y += gravity;
		}
		if (!Key.isPressed(Key.SPACE) && velocity.y < 0) {
			this.velocity.y = 0;
		}

		if (velocity.y < 0 && !canMoveUp()) {
			velocity.y = 0;
		}

		double xV = 0;
		if (Key.isPressed(Key.A)) {
			if (canMoveLeft()) {
				xV -= speed;
			}
		}
		if (Key.isPressed(Key.D)) {
			if (canMoveRight()) {
				xV += speed;
			}
		}

		this.velocity.x = xV;

		// Spikes
		if (touchDeadly()) {
			if (!(touchDeadlyFeet() && isOnGround()) || isInDeadly()) {
				Location spawn = Main.levels.get(Main.level).spawn;
				this.velocity = new Velocity(0, 0);
				this.location = new Location(spawn.x, spawn.y);
			}
		}

		// Out of world
		if (location.y > 27) {
			this.location.y = 25;
		}

		// End
		if (touchEnd()) {
			if (Main.levels.size() - 1 == Main.level) {
				camera.location = new Location(-50, -50);
				VelocityHandler.remove(this);
				this.location = new Location(-50, -52);
				window.setBackgroundImage(ResourceLoader.getInstance().loadImage("win.png"));
			} else {
				Main.level++;
				Transformer.map(world, Main.levels.get(Main.level).map, camera, this);
			}
		}
	}

}
