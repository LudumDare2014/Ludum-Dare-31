package com.webs.kevkawm.Main;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;

import net.bobmandude9889.Render.Camera;
import net.bobmandude9889.Resource.ResourceLoader;
import net.bobmandude9889.Window.Key;
import net.bobmandude9889.Window.Window;
import net.bobmandude9889.World.Location;
import net.bobmandude9889.World.OrthogonalMap;
import net.bobmandude9889.World.OrthogonalMapLoader;
import net.bobmandude9889.World.VelocityHandler;
import net.bobmandude9889.World.World;

import com.webs.kevkawm.Blocks.Transformer;
import com.webs.kevkawm.Player.Player;

public class Main {

	static GraphicsDevice device = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()[0];
	
	public static OrthogonalMap clear;
	
	public static void main(String[] args) {
		clear = loadMap("clear.tmx");
		World world = new World(loadMap("map 1.tmx"));
		Camera camera = new Camera(new Location(18, 14),25, world);
		Window window = new Window(900, 700, "ld game", camera, ResourceLoader.loadImage("background3.png"));
		Player player = new Player(new Location(10,5), world, ResourceLoader.loadImage("player.png"), camera);
		world.addEntity(player);
		VelocityHandler.init(20);
		VelocityHandler.add(player);
		window.setResizable(false);
		while(!Key.isPressed(Key.ENTER));
		Transformer.map(world, loadMap("map 2.tmx"), camera, player);
	}

	public static OrthogonalMap loadMap(String name){
		OrthogonalMap map = OrthogonalMapLoader.load(name);
		map.layers[2].enabled = false;
		return map;
	}
	
}
