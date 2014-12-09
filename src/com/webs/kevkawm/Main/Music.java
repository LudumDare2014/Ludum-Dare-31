package com.webs.kevkawm.Main;

import net.bobmandude9889.Resource.Sound;

public class Music {

	public static void loop(final Sound sound, final float percent){
		sound.play(percent);
		new Thread(new Runnable(){
			@Override
			public void run(){
				while(true){
					if(sound.clip.getFramePosition() == sound.clip.getFrameLength()){
						sound.play(percent);
					}
				}
			}
		}).start();
	}
	
}
