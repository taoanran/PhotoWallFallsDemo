package com.example.photowallfallsdemo;
//Bitmap bittmp = Bitmap.createScaledBitmap(bmp, window_width, window_height, true);
import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ListIterator;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.util.Log;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class GalleryAdapter extends BaseAdapter {

    private static final String TAG = "gallery";

    private ArrayList<PicItem> mPicItemList;

    private LayoutInflater mInflater;
    private HashMap<Integer, View> mViewMaps;

    private int mCount;
    private Context mContext;
    private int window_width, window_height;// 鎺т欢瀹藉害
    private ViewTreeObserver viewTreeObserver;
    private int state_height;// 鐘舵�佹爮鐨勯珮搴�
    private ImageView mDragimageview;
    private LinkedHashMap<String,Bitmap> dateCache = new LinkedHashMap<String,Bitmap>();
    private int lastIndex = -1;
    //private ImageLoader2 imageLoader;
    
    public LinkedHashMap<String,Bitmap> getDateCache() {
    	return dateCache;
    }
    
    private void putFromStart(String key, Bitmap insertBitmap) {

    	LinkedHashMap<String,Bitmap> tmpCache = new LinkedHashMap<String,Bitmap>();
    	
    	tmpCache.put(key, insertBitmap);
    	ListIterator <?> lit  = new ArrayList(dateCache.keySet()).listIterator();
    	while(lit.hasNext()) {
    		Object keyTmp = lit.next();
    		tmpCache.put((String) keyTmp, dateCache.get(keyTmp));
    	}
    	
    	dateCache.clear();
    	
    	lit  = new ArrayList(tmpCache.keySet()).listIterator();
    	while(lit.hasNext()) {
    		Object keyTmp = lit.next();
    		dateCache.put((String) keyTmp, tmpCache.get(keyTmp));
    	}
    	
    	tmpCache = null;
    }
    
  //鐢ㄦ潵淇濆瓨鍚勪釜鎺т欢鐨勫紩鐢�
  	private static class ViewHolder {
  		ImageView dragimageview;
  	}
    
    @SuppressLint("NewApi")
	public GalleryAdapter(Context context, ArrayList<PicItem> picItemList) {
        // TODO Auto-generated constructor stub
        mPicItemList = picItemList;

        mCount = picItemList.size();

        mInflater = LayoutInflater.from(context);
        mViewMaps = new HashMap<Integer, View>(mCount);
        mContext = context;
        //imageLoader = ImageLoader2.getInstance();
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mPicItemList.size();
    }

    @Override
    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return mPicItemList.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return arg0;
    }

    @Override
    public View getView(int arg0, View convertView, ViewGroup arg2) {
        // TODO Auto-generated method stub
    	Log.v(TAG, "**************getView****************");
        View viewGroup = mViewMaps.get(arg0 % mCount);
        ViewHolder holder = null;
        
 		WindowManager manager = ((Activity)mContext).getWindowManager();
 		window_width = manager.getDefaultDisplay().getWidth();
 		window_height = manager.getDefaultDisplay().getHeight();
 		
        if (viewGroup == null) {
            viewGroup = mInflater.inflate(R.layout.gallery_item, null);
            mViewMaps.put(arg0 % mCount, viewGroup);
         }  
        
         if (convertView == null) {
        	 convertView = mInflater.inflate(R.layout.gallery_item, null);
        	 holder = new ViewHolder();
        	 holder.dragimageview = (ImageView) viewGroup.findViewById(R.id.gl_image);
             convertView.setTag(holder);
         } else {
        	 holder = (ViewHolder) convertView.getTag();
         }
         
        Bitmap current=null;
        current = dateCache.get(mPicItemList.get(arg0 % mCount).name);
        if (current != null) {
        	Log.v(TAG, "current != null");
    		holder.dragimageview.setImageBitmap(current);
        } else {
        	Log.v(TAG, "current ==== null");
	        current = BitmapFactory.decodeFile(mPicItemList.get(arg0 % mCount).name);
	        if (lastIndex < arg0) {
	        	dateCache.put(mPicItemList.get(arg0 % mCount).name,current);
	        	lastIndex = arg0;
	        } else {
	        	putFromStart(mPicItemList.get(arg0 % mCount).name,current);
	        	lastIndex = arg0;
	        }
	        holder.dragimageview.setImageBitmap(current);
        }
        
//         mDragimageview = holder.dragimageview;
//         holder.dragimageview.setmActivity((Activity)mContext);//娉ㄥ叆Activity.
//    		/** 娴嬮噺鐘舵�佹爮楂樺害 **/
//    		viewTreeObserver = holder.dragimageview.getViewTreeObserver();
//    		viewTreeObserver
//    				.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
//    
//    					@Override
//    					public void onGlobalLayout() {
//    						if (state_height == 0) {
//    							// 鑾峰彇鐘跺喌鏍忛珮搴�
//    							Rect frame = new Rect();
//    							((Activity)mContext).getWindow().getDecorView()
//    									.getWindowVisibleDisplayFrame(frame);
//    							state_height = frame.top;
//    							mDragimageview.setScreen_H(window_height-state_height);
//    							mDragimageview.setScreen_W(window_width);
//    						}
//    
//    					}
//    				});
            
            
            Log.i(TAG, "arg0 : " + arg0);

        return viewGroup;
    }
	
}