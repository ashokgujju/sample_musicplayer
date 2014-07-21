package com.ashok.musicplayer;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class Act1 extends Activity{
	MediaPlayer mp;
	TextView tv2,tv3;
	String c;
	int inc;
	Thread myTread;
	SeekBar sb;
	Thread pbar;
	String d ;
	int duration, cpos;
	public void onCreate(Bundle b){
		super.onCreate(b);
		setContentView(R.layout.first);
		tv2=(TextView)findViewById(R.id.textView2);
		tv3=(TextView)findViewById(R.id.textView3);
		sb=(SeekBar)findViewById(R.id.seekBar1);
		mp=MediaPlayer.create(this, R.raw.jayajayahe);
		initControls();	
		duration = mp.getDuration();
		tv2.setText(duration/1000+"");
		myTread = new Thread() {        	
	        @Override
	        public void run() {
	            try {
	        		while(!(myTread.isInterrupted()) && ((cpos=mp.getCurrentPosition()) <= duration)) {
	                	if(mp.isPlaying())
	                		threadHandler.sendEmptyMessage(cpos/1000);
	                	sleep(1000);
	                }
	            } catch(InterruptedException e) {
	            }
	        }
	    };
	    myTread.start();
	    pbar = new Thread(){
	    	public void run(){
	    		try{
	    			inc = 0;
	    			while(inc<=100 && !(pbar.isInterrupted())){
	    				if(mp.isPlaying()){
	    					sb.setProgress(inc);
	    				}
	    				sleep(Math.round(duration/100));
	    				inc++;
	    			}
	    		}catch(InterruptedException e) {
	            }
	    	}
	    }; pbar.start();e
	}
	private void initControls() {
			try {
			    sb.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			    public void onStopTrackingTouch(SeekBar arg0) {
			    	
			    }
			    public void onStartTrackingTouch(SeekBar arg0) {
			        }
			    public void onProgressChanged(SeekBar arg0,
			        int progress, boolean arg2) {
			    	mp.seekTo(progress*(duration/100));
			    	inc=progress;
			    }
			 });
			} catch (Exception e) {
			    e.printStackTrace();
			}
			}
	public void play(View v){
		mp.start();
	}
	public void pause(View v){
		mp.pause();
	}
	public void stop(View v){
		mp.release();
		mp=MediaPlayer.create(this, R.raw.jayajayahe);
		duration = mp.getDuration();
		tv3.setText(null);
		inc=0;
		sb.setProgress(0);
	}
	public void onDestroy(){
		mp.release();
		super.onDestroy();
		myTread.interrupt();
		pbar.interrupt();
	} 
	private Handler threadHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			tv3.setText(Integer.toString(msg.what));
		}
	};
}