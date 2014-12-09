package com.webs.kevkawm.Main;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import net.bobmandude9889.Render.Camera;
import net.bobmandude9889.Resource.ResourceLoader;
import net.bobmandude9889.Window.Window;
import net.bobmandude9889.World.Location;
import net.bobmandude9889.World.OrthogonalLayer;
import net.bobmandude9889.World.OrthogonalMap;
import net.bobmandude9889.World.OrthogonalMapLoader;
import net.bobmandude9889.World.Tile;
import net.bobmandude9889.World.VelocityHandler;
import net.bobmandude9889.World.World;

import com.webs.kevkawm.Blocks.Transformer;
import com.webs.kevkawm.Player.Player;

public class Main {

	static GraphicsDevice device = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()[0];

	public static OrthogonalMap clear;

	public static List<Level> levels;

	public static int level = 0;
	public static int amount = 15;
	
	public static void main(String[] args) {
		try {
			levels = new ArrayList<Level>();
			clear = loadMap("clear.tmx");
			loadLevels();
			World world = new World(clear);
			Camera camera = new Camera(new Location(-50, -50), 25, world);
			Window window = new Window(900, 700, "Spike Jumper", camera, ResourceLoader.getInstance().loadImage("load.png"));
			Player player = new Player(levels.get(0).spawn, world, ResourceLoader.getInstance().loadImage("player.png"), camera, window);
			window.setResizable(false);
			window.setSize(900, 700);
			window.setLocationRelativeTo(null);
			window.setBackgroundImage(ResourceLoader.getInstance().loadImage("background4.png"));
			camera.location = new Location(18, 14);
			VelocityHandler.init(20);
			VelocityHandler.add(player);
			Transformer.map(world, levels.get(level).map, camera, player);
			Music.loop(ResourceLoader.getInstance().loadSound("song.wav"), 70f);
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, e.getStackTrace());
		}
	}

	public static OrthogonalMap loadMap(String name) {
		OrthogonalMap map = OrthogonalMapLoader.load(name);
		return map;
	}

	public static void loadLevels() {
		for (int i = 1; i <= amount; i++) {
			OrthogonalMap map = OrthogonalMapLoader.load("map_" + i + ".tmx");
			OrthogonalLayer spawn = map.layers[2];
			Location start = null;
			spawn.enabled = false;
			for (int x = 0; x < spawn.getWidth(); x++) {
				for (int y = 0; y < spawn.getHeight(); y++) {
					Tile tile = spawn.getTile(x, y);
					if (tile != null && tile.getProperties().contains("start")) {
						start = new Location(x, y - 1);
						x = spawn.getWidth();
						break;
					}
				}
			}
			String message = "";
			switch (i) {
			case 1:
				message = "Use A and D to move and Space to jump. Jump over spikes and get to the green orb to move on to the next level.";
				break;
			case 3:
				message = "Sometimes there are upside down spikes.";
				break;
			case 4:
				message = "Spikes can also be sideways.";
				break;
			}
			levels.add(new Level(map, start, message));
		}
	}

}
