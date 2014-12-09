package com.webs.kevkawm.Main;

import net.bobmandude9889.World.Location;
import net.bobmandude9889.World.OrthogonalMap;

public class Level {

	public OrthogonalMap map;
	public Location spawn;
	public String message;
	
	public Level(OrthogonalMap map, Location spawn, String message){
		this.map = map;
		this.spawn = spawn;
		this.message = message;
	}
	
}
