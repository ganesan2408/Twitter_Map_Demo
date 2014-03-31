package com.gans.twittertestapp;

import java.util.List;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MYAdapter extends BaseAdapter {
    
    private Activity activity;
    private List<TweetsData> data;
    private static LayoutInflater inflater=null;
    private DisplayImageOptions imgDispOptions1;

    public MYAdapter(Activity a, List<TweetsData> datas) {
        activity = a; 
        data = datas;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
        		activity.getApplicationContext())
				.threadPriority(Thread.NORM_PRIORITY - 2)
				.discCacheSize(1024*1024*30)
				.denyCacheImageMultipleSizesInMemory()
				.discCacheFileNameGenerator(new Md5FileNameGenerator())
				.build();
		ImageLoader.getInstance().init(config);
		
		imgDispOptions1 = new DisplayImageOptions.Builder()
		.showStubImage(R.drawable.stub)
		.showImageForEmptyUri(R.drawable.stub)
		.showImageOnFail(R.drawable.stub)
		.cacheInMemory(true)
		.cacheOnDisc(true)
		
		.displayer(new RoundedBitmapDisplayer(0))
		//.displayer(new FadeInBitmapDisplayer(3000))
		.build();
    }

    public int getCount() {
        return data.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }
    
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi=convertView;
        
        TweetsData tweet = data.get(position);
        if(convertView==null){
            vi = inflater.inflate(R.layout.item, null);
            
        }
        TextView fname=(TextView)vi.findViewById(R.id.fname);
        TextView lname=(TextView)vi.findViewById(R.id.tweet);
        ImageView image = (ImageView)vi.findViewById(R.id.image);
        fname.setText(tweet.getFirstName().toString());
        lname.setText(tweet.getLastName().toString());
        
        if(tweet.getImageUrl() != null)
        	ImageLoader.getInstance().displayImage(tweet.getImageUrl().toString(), image, imgDispOptions1);
        return vi;
    }
}