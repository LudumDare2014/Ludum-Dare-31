package com.webs.kevkawm.Blocks;

import java.util.ArrayList;
import java.util.List;

import com.webs.kevkawm.Main.Main;

import net.bobmandude9889.World.Location;
import net.bobmandude9889.World.OrthogonalLayer;
import net.bobmandude9889.World.OrthogonalMap;
import net.bobmandude9889.World.Tile;
import net.bobmandude9889.World.VelocityHandler;
import net.bobmandude9889.World.World;

public class Transformer {

	public static void map(final World world, final OrthogonalMap end) {
		OrthogonalMap start = (OrthogonalMap) world.map;
		List<Location> startList = getTiles(start);
		List<Location> endList = getTiles(end);
		List<Tile> moveT = new ArrayList<Tile>();
		List<Location> startMove = new ArrayList<Location>();
		List<Location> endMove = new ArrayList<Location>();
		for (int i = 0; i < startList.size(); i++) {
			int random = ((int) Math.random() * startList.size());
			Location startLoc = startList.get(i);
			Location endLoc = endList.get(random);
			moveT.add(start.layers[0].getTile((int) startLoc.x, (int) startLoc.y));
			startMove.add(startLoc);
			endMove.add(endLoc);
			endList.remove(random);
		}

		world.map = Main.clear;

		final List<BlockEntity> blocks = new ArrayList<BlockEntity>();
		for (int t = 0; t < moveT.size(); t++) {
			Tile tile = moveT.get(t);
			final BlockEntity block = new BlockEntity(tile.getImage(), startMove.get(t), endMove.get(t));
			world.addEntity(block);
			blocks.add(block);
			VelocityHandler.add(block);
		}
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				for (BlockEntity block : blocks) {
					world.entityList.remove(block);
					VelocityHandler.remove(block);
					world.map = end;
				}
			}
		}).start();
	}

	private static List<Location> getTiles(OrthogonalMap map) {
		List<Location> ret = new ArrayList<Location>();
		OrthogonalLayer layer = map.layers[0];
		for (int i = 0; i < layer.getTiles().length; i++) {
			Location location = new Location(i % layer.getWidth(), Math.floor(i / layer.getWidth()));
			if (layer.getTile((int) location.x, (int) location.y) != null) {
				System.out.println(location.x + "," + location.y);
				ret.add(location);
			}
		}
		return ret;
	}

}
