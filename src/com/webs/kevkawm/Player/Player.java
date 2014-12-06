package com.webs.kevkawm.Player;

import java.awt.Image;
import java.awt.image.BufferedImage;

import net.bobmandude9889.Window.Key;
import net.bobmandude9889.World.Entity;
import net.bobmandude9889.World.Location;
import net.bobmandude9889.World.OrthogonalLayer;
import net.bobmandude9889.World.OrthogonalMap;
import net.bobmandude9889.World.Tile;
import net.bobmandude9889.World.Velocity;

public class Player implements Entity {
	
	Location location;
	Velocity velocity;
	OrthogonalMap map;
	BufferedImage matrix;
	double imageWidth = 73;
	double imageHeight = 40;
	
	double gravity = 0.007;
	double jumpForce = -0.18;
	
	double speed = 0.1;
	
	public Player(Location location, OrthogonalMap map, BufferedImage matrix){
		this.location = location;
		this.map = map;
		this.matrix = matrix;
		this.velocity = new Velocity(0,0);
	}
	
	@Override
	public Image getImage() {
		return matrix.getSubimage(0, 0, (int) imageWidth, (int) imageHeight);
	}

	@Override
	public Location getLocation() {
		return location;
	}

	@Override
	public Velocity getVelocity() {
		if(Key.isPressed(Key.SPACE) && isOnGround()){
			this.velocity.y = jumpForce;
		} else if(isOnGround() && velocity.y > 0){
			this.velocity.y = 0;
			this.location.y = Math.floor(location.y + (imageHeight / 50)) - 0.55;
		} else {
			this.velocity.y += gravity;
		}
		
		double xV = 0;
		if(Key.isPressed(Key.A)){
			if(canMoveLeft()){
				xV -= speed;
			} else {
				location.x = Math.round(location.x);
			}
		}
		if(Key.isPressed(Key.D)){
			if(canMoveRight()){
				xV += speed;
			}
		}
		
		this.velocity.x = xV;
		
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

	public boolean canMoveLeft(){
		for(OrthogonalLayer layer : map.layers){
			Tile tile = layer.getTile((int) Math.floor(location.x - 0.1), (int) Math.floor(location.y));
			if(tile != null && tile.getProperties().contains("collide")){
				return false;
			}
			tile = layer.getTile((int) Math.floor(location.x - 0.1), (int) (Math.ceil(location.y) - 0.1));
			if(tile != null && tile.getProperties().contains("collide")){
				return false;
			}
		}
		return true;
	}
	
	public boolean canMoveRight(){
		for(OrthogonalLayer layer : map.layers){
			Tile tile = layer.getTile((int) Math.ceil(location.x + 0.1), (int) (Math.floor(location.y)));
			if(tile != null && tile.getProperties().contains("collide")){
				return false;
			}
			tile = layer.getTile((int) Math.ceil(location.x + 0.1), (int) (Math.ceil(location.y) - 0.1));
			if(tile != null && tile.getProperties().contains("collide")){
				return false;
			}
		}
		return true;
	}
	
	public boolean isOnGround(){
		for(OrthogonalLayer layer : map.layers){
			Tile tile = layer.getTile((int) location.x, (int) Math.floor(location.y + (imageHeight / 50)));
			if(tile != null && tile.getProperties().contains("collide")){
				return true;
			}
			tile = layer.getTile((int) (Math.floor(location.x + (imageWidth / 50)) - 0.1), (int) Math.floor(location.y + (imageHeight / 50)));
			if(tile != null && tile.getProperties().contains("collide")){
				return true;
			}
			tile = layer.getTile((int) Math.floor(location.x + (imageWidth / 50) / 2), (int) Math.floor(location.y + (imageHeight / 50)));
			if(tile != null && tile.getProperties().contains("collide")){
				return true;
			}
		}
		return false;
	}
	
}
