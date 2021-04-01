package Adapters;

import java.io.InputStream;
import java.util.ArrayList;

import com.example.localevents.R;
import com.squareup.picasso.Picasso;
import models.CategoryModel;
import Adapters.ShowCategoryAdapter.ViewHolder;
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class Spinner_Cat_Adapt extends BaseAdapter {
	

	private ArrayList<CategoryModel> listCategories;
	private LayoutInflater layoutInflater;
	public OnClickListener listener;
	private Context mContext;

	public Spinner_Cat_Adapt(Context context, ArrayList<CategoryModel> listData) {
		this.listCategories = listData;
		layoutInflater = LayoutInflater.from(context);
		mContext = context;

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return listCategories.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return listCategories.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@SuppressLint("InflateParams")
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder;

		if (convertView == null) {

			convertView = layoutInflater.inflate(R.layout.spinner, null);
			holder = new ViewHolder();
			holder.name = (TextView) convertView.findViewById(R.id.categoryname);
			holder.img = (ImageView) convertView.findViewById(R.id.categoryimage);

			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		final CategoryModel newsItem = (CategoryModel) listCategories.get(position);

		holder.name.setText(newsItem.getCat_name());
		
		try{
			String pathurl = newsItem.getCat_photo();			
			String url = "http://7girls.byethost7.com/public/"+pathurl;
			Picasso.with(mContext).load(url).noFade().into(holder.img);
			
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		if (this.listener != null) {

		}
		return convertView;
	}
	
	
	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder;

		if (convertView == null) {

			convertView = layoutInflater.inflate(R.layout.spinner, null);
			holder = new ViewHolder();
			holder.name = (TextView) convertView.findViewById(R.id.categoryname);
			holder.img = (ImageView) convertView.findViewById(R.id.categoryimage);

			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		final CategoryModel newsItem = (CategoryModel) listCategories.get(position);

		holder.name.setText(newsItem.getCat_name());
		
		try{
			String pathurl = newsItem.getCat_photo();			
			String url = "http://7girls.byethost7.com/public/"+pathurl;
			Picasso.with(mContext).load(url).noFade().into(holder.img);
			
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
