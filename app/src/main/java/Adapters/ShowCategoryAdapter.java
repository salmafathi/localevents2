package Adapters;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

import com.example.localevents.R;
import com.squareup.picasso.Picasso;

import models.CategoryModel;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;



public class ShowCategoryAdapter extends BaseAdapter {

	
	private ArrayList<CategoryModel> listData;
	private LayoutInflater layoutInflater;
	public OnClickListener listener;
	private Context mContext;
	// Users newsItem1;
	String iii;
	String feed_id;
	String loginId;
	int idUser;
	Bitmap bitmap ;
	SharedPreferences sp;
	InputStream in;
	public SharedPreferences preferences;

	public ShowCategoryAdapter(Context context, ArrayList<CategoryModel> listData) {
		this.listData = listData;
		layoutInflater = LayoutInflater.from(context);
		mContext = context;

	}

	public void setButtonListener(OnClickListener listener) {
		this.listener = listener;
	}

	@Override
	public int getCount() {
		return listData.size();
	}

	@Override
	public Object getItem(int position) {
		return listData.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@SuppressLint("InflateParams")
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder;

		if (convertView == null) {

			convertView = layoutInflater.inflate(R.layout.activity_catitem, null);
			holder = new ViewHolder();

			holder.name = (TextView) convertView.findViewById(R.id.categoryname);
			holder.img = (ImageView) convertView.findViewById(R.id.categoryimage);


			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		final CategoryModel newsItem = (CategoryModel) listData.get(position);

		holder.name.setText(newsItem.getCat_name());
		//holder.img.get
		
		try{
			String pathurl = newsItem.getCat_photo();
			
			String url = "http://7girls.byethost7.com/public/"+pathurl;
			Picasso.with(mContext).load(url).noFade().into(holder.img);
//			Log.i("imageurllllll", url);
//			 in = (InputStream) new URL(url).getContent();
//			Bitmap bitmap = BitmapFactory.decodeStream(in);
//			newsItem.setbitmap(bitmap);
//			Log.i("bitmaaaaaap",bitmap.toString());
//			holder.img.setImageBitmap(newsItem.getbitmap());
//			in.close();
			
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		if (this.listener != null) {

		}
		return convertView;
	}

	static class ViewHolder {
		TextView name;
		ImageView img;
	}
}
