package com.example.photowallfallsdemo;

import java.io.InputStream;
import java.security.acl.LastOwnerException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ImageView;

public class ImageDetailsActivity extends Activity {
	private int window_width, window_height;// 鎺т欢瀹藉害
	private ImageView dragImageView;// 鑷畾涔夋帶浠�
	private int state_height;// 鐘舵�佹爮鐨勯珮搴�
	private static final String TAG = "ImageDetailsActivity";
	private ViewTreeObserver viewTreeObserver;
	private FlingOneGallery gl;
	private GalleryAdapter ga;
	
	private int startLast = -3;
	private int endLast = -999;
	private int number;
	
	private static class resource{
		public final static int[] imgs = {
			R.drawable.test,
			R.drawable.empty_photo,
			R.drawable.ic_launcher, 
		};
	}

	@SuppressWarnings("deprecation")
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main);

		gl = (FlingOneGallery) findViewById(R.id.gallery);
        gl.setSpacing(0);
        
        gl.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Log.v(TAG, "position = " + position);
				//releaseBitmap();
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
				
			}
        });
        
        ArrayList<PicItem> picList = new ArrayList<PicItem>();
		Intent intent = getIntent();
		String[] name = intent.getStringArrayExtra("image_path");
		//number = Images.imageUrls.length;

		Log.v(TAG, "number of pic is " + number);
		for (int i=0; i<name.length; i++) {
	        PicItem picItem = new PicItem(name[i]);
	        picList.add(picItem);
		}
		ga = new GalleryAdapter(this, picList);
        gl.setAdapter(ga);
        
        gl.setSelection(20);
	}
	
	private void releaseBitmap(){

		//鍦ㄨ繖锛屾垜浠垎鍒瀛樺偍浜嗙涓�涓拰鏈�鍚庝竴涓彲瑙佷綅缃箣澶栫殑3涓綅缃殑bitmap

		//鍗砫ataCache涓缁堝彧缂撳瓨浜嗭紙M锛�6锛婫allery褰撳墠鍙view鐨勪釜鏁帮級M涓猙itmap

		int start = gl.getFirstVisiblePosition() -3;

		int end = gl.getLastVisiblePosition() +3;

		Log.v(TAG, "start:"+ start);

		Log.v(TAG, "end:"+ end);
		
		Log.v(TAG, "ga.getDateCache().size = " + ga.getDateCache().size());
		
		if (startLast < start) {
			//閲婃斁position<start涔嬪鐨刡itmap璧勬簮
			Bitmap delBitmap;
			Iterator<?> iter = ga.getDateCache().keySet().iterator();
	
			for (int i=0; i<start; i++) {
				
				if (iter.hasNext()) {
					Object key = iter.next();
					delBitmap = (Bitmap) ga.getDateCache().get(key);
					if(delBitmap != null) {
						//濡傛灉闈炵┖鍒欒〃绀烘湁缂撳瓨鐨刡itmap锛岄渶瑕佹竻鐞�
						//Log.v(TAG, "release");
						
						if (!delBitmap.isRecycled()) {
						//	Log.v(TAG, "!delBitmap.isRecycled(releaseBitmap)");
							iter.remove();
							delBitmap.recycle();
							delBitmap = null;
							break;
						}
					}
				}
			}
			startLast = start;
		}
		
		if (startLast > start) {
			freeBitmapFromIndex(end);
			startLast = start;
		}
		Log.v(TAG, "end");
	}

		 

		/**

		* 浠庢煇涓�浣嶇疆寮�濮嬮噴鏀綽itmap璧勬簮

		* @param index

		*/

		private void freeBitmapFromIndex(int end) {

			//閲婃斁涔嬪鐨刡itmap璧勬簮
			
			Bitmap delBitmap;
			ListIterator <?> lit  = new ArrayList(ga.getDateCache().keySet()).listIterator(ga.getDateCache().size());
			Log.v(TAG, "lit.size = " + ga.getDateCache().size());
			for (int del=number-1; del > end; del--) {
				if (lit.hasPrevious()) {
					Log.v(TAG, "freeBitmapFromIndex");
					Object key = lit.previous();
					delBitmap = (Bitmap) ga.getDateCache().get(key);
			
					if(delBitmap != null){
						if (!delBitmap.isRecycled()) {
							Log.v(TAG, "!delBitmap.isRecycled(freeBitmapFromIndex)");
							lit.remove();
							ga.getDateCache().remove(key);
							delBitmap.recycle();
							delBitmap = null;
							break;
						}
					}
				}
			}
		}
}