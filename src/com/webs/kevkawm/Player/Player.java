package com.webs.kevkawm.Player;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;

import net.bobmandude9889.Render.Camera;
import net.bobmandude9889.Window.Key;
import net.bobmandude9889.World.Entity;
import net.bobmandude9889.World.Location;
import net.bobmandude9889.World.OrthogonalLayer;
import net.bobmandude9889.World.OrthogonalMap;
import net.bobmandude9889.World.Tile;
import net.bobmandude9889.World.Velocity;
import net.bobmandude9889.World.VelocityHandler;
import net.bobmandude9889.World.World;

public class Player implements Entity {

	Location location;
	Velocity velocity;
	World world;
	Camera camera;
	BufferedImage matrix;
	double imageWidth = 50;
	double imageHeight = 70;

	double gravity = 0.009;
	double jumpForce = -0.3;

	double speed = 0.2;

	public Player(Location location, World world, BufferedImage matrix, Camera camera) {
		this.location = location;
		this.world = world;
		this.matrix = matrix;
		this.velocity = new Velocity(0, 0);
		this.camera = camera;
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
			Tile tile = layer.getTile((int) Math.floor(location.x - 0.1), (int) (Math.floor(location.y)));
			if (tile != null && tile.getProperties().contains("collide")) {
				return false;
			}
			tile = layer.getTile((int) Math.ceil(location.x + 0.1), (int) (Math.floor(location.y)));
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
			Tile tile = layer.getTile((int) (location.x - 0.1), (int) (Math.floor(location.y + (imageHeight / 50))));
			if (tile != null && tile.getProperties().contains("collide")) {
				return true;
			}
			tile = layer.getTile((int) (Math.floor(location.x + (imageWidth / 50))), (int) (Math.floor(location.y + (imageHeight / 50))));
			if (tile != null && tile.getProperties().contains("collide")) {
				return true;
			}
			tile = layer.getTile((int) (Math.floor(location.x + (imageWidth / 50) / 2)), (int) (Math.floor(location.y + (imageHeight / 50))));
			if (tile != null && tile.getProperties().contains("collide")) {
				return true;
			}
		}
		return false;
	}

	public void onMove(){
		//Gravity
		if (Key.isPressed(Key.SPACE) && isOnGround()) {
			this.velocity.y = jumpForce;
		} else if (isOnGround() && velocity.y > 0) {
			this.velocity.y = 0;
			this.location.y = Math.floor(location.y) + 0.6;
		} else {
			this.velocity.y += gravity;
		}

		if (velocity.y < 0 && !canMoveUp()) {
			velocity.y = 0;
		}

		double xV = 0;
		if (Key.isPressed(Key.A)) {
			if (canMoveLeft()) {
				xV -= speed;
			} else {
				location.x = Math.round(location.x);
			}
		}
		if (Key.isPressed(Key.D)) {
			if (canMoveRight()) {
				xV += speed;
			}
		}

		this.velocity.x = xV;

		//Spikes
	}
	
}
