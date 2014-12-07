package com.webs.kevkawm.Blocks;

import java.util.ArrayList;
import java.util.List;

import net.bobmandude9889.Render.Camera;
import net.bobmandude9889.World.Entity;
import net.bobmandude9889.World.GlobalTileSet;
import net.bobmandude9889.World.Location;
import net.bobmandude9889.World.OrthogonalLayer;
import net.bobmandude9889.World.OrthogonalMap;
import net.bobmandude9889.World.Tile;
import net.bobmandude9889.World.VelocityHandler;
import net.bobmandude9889.World.World;

import com.webs.kevkawm.Main.Main;
import com.webs.kevkawm.Player.Player;

public class Transformer {

	public static void map(final World world, final OrthogonalMap end, final Camera camera, final Player player) {
		for(Entity e : world.entityList){
			world.entityList.remove(e);
		}
		OrthogonalMap start = (OrthogonalMap) world.map;
		List<Location> startList = getTiles(start);
		List<Location> endList = getTiles(end);
		List<Tile> moveT = new ArrayList<Tile>();
		List<Location> startMove = new ArrayList<Location>();
		List<Location> endMove = new ArrayList<Location>();
		System.out.println("start " + startList.size() + " end " + endList.size());
		boolean spawn = false;
		boolean remove = false;
		int amount = startList.size();
		if (startList.size() < endList.size()) {
			spawn = true;
		} else if(startList.size() > endList.size()){
			remove = true;
			amount = endList.size();
		}
		for (int i = 0; i < amount; i++) {
			Location startLoc = startList.get(0);
			int random = (int) (Math.random() * endList.size());
			Location endLoc = endList.get(random);
			moveT.add(start.layers[0].getTile((int) startLoc.x, (int) startLoc.y));
			startMove.add(startLoc);
			endMove.add(endLoc);
			endList.remove(random);
			startList.remove(0);
		}

		if (spawn) {
			for (int i = 0; i < endList.size(); i++) {
				moveT.add(GlobalTileSet.get(1));
				startMove.add(new Location(Math.random() * 36,-1));
				endMove.add(endList.get(i));
			}
		} else if(remove){
			for(int i = 0; i < startList.size(); i++){
				moveT.add(GlobalTileSet.get(1));
				startMove.add(startList.get(i));
				endMove.add(new Location(Math.random() * 36, -1));
			}
		}
		
		world.map = Main.clear;

		final List<Entity> blocks = new ArrayList<Entity>();
		final List<Integer> starts = new ArrayList<Integer>();
		for (int t = 0; t < moveT.size(); t++) {
			Tile tile = moveT.get(t);
			final BlockEntity block = new BlockEntity(tile.getImage(), startMove.get(t), endMove.get(t));
			world.addEntity(block);
			blocks.add(block);
			starts.add(VelocityHandler.updates);
			VelocityHandler.add(block);
		}
		new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					List<BlockEntity> remove = new ArrayList<BlockEntity>();
					for (int i = 0; i < blocks.size(); i++) {
						BlockEntity block = (BlockEntity) blocks.get(i);
						if (VelocityHandler.updates - 100 >= starts.get(i)) {
							world.entityList.remove(block);
							VelocityHandler.remove(block);
							remove.add(block);
						}
					}
					for (int i = 0; i < remove.size(); i++) {
						starts.remove(blocks.indexOf(remove.get(i)));
						blocks.remove(remove.get(i));
					}
					if (blocks.size() == 0) {
						world.map = end;
						OrthogonalLayer entityLayer = end.layers[2];
						for(int x = 0; x < entityLayer.getWidth(); x++){
							for(int y = 0; y < entityLayer.getHeight(); y++){
								Tile tile = entityLayer.getTile(x, y);
								if(tile != null){
									
								}
							}
						}
						world.addEntity(player);
						break;
					}
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
