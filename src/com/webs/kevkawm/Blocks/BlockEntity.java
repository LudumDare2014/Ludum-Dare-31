package com.webs.kevkawm.Blocks;

import java.awt.Image;

import net.bobmandude9889.World.Entity;
import net.bobmandude9889.World.Location;
import net.bobmandude9889.World.Velocity;

public class BlockEntity implements Entity{

	Image img;
	Location location;
	Velocity velocity;
	
	public BlockEntity(Image img, Location start, Location end){
		this.img = img;
		this.location = start;
		Location p = new Location(end.x - start.x, end.y - start.y);
		
		boolean flip = p.y > 0;
		
		double h = Math.sqrt(Math.pow(p.x,2) + Math.pow(p.y,2));
		double rotation = Math.asin(p.x / h);
		
		if(flip){
			 rotation += 2 * (Math.toRadians(90) - rotation);
		}
		
		if(rotation < 0){
			rotation = Math.toRadians(360) + rotation;
		} 
		
		double distance = Math.sqrt(Math.pow(end.x - start.x, 2) + Math.pow(end.x - start.y, 2));
		double speed = distance / 100;
		this.velocity = new Velocity((Math.sin(rotation) * speed), (Math.cos(rotation) * speed));
		this.velocity.y = - this.velocity.y;
	}
	
	@Override
	public Image getImage() {
		return img;
	}

	@Override
	public Location getLocation() {
		return location;
	}

	@Override
	public Velocity getVelocity() {
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

}
