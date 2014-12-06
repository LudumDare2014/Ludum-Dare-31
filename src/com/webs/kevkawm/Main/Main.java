package com.webs.kevkawm.Main;

import java.awt.Color;
import java.awt.Frame;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;

import net.bobmandude9889.Render.Camera;
import net.bobmandude9889.Window.Window;
import net.bobmandude9889.World.Location;
import net.bobmandude9889.World.OrthogonalMapLoader;
import net.bobmandude9889.World.World;

public class Main {

	static GraphicsDevice device = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()[0];
	
	public static void main(String[] args) {
		World world = new World(OrthogonalMapLoader.load("test-map.tmx"));
		Camera camera = new Camera(new Location(18, 14), 25, world);
		Window window = new Window(900, 700, "ld game", camera, Color.CYAN);
		window.setResizable(false);
	}

}
