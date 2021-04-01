package Adapters;

import java.util.ArrayList;

import com.example.localevents.R;
import com.squareup.picasso.Picasso;

import models.CategoryModel;
import models.CityModel;
import Adapters.Spinner_Cat_Adapt.ViewHolder;
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class Spinner_City_Adapter extends BaseAdapter {
	
	
	private ArrayList<CityModel> listcites;
	private LayoutInflater layoutInflater;
	public OnClickListener listener;
	private Context mContext;
	
	public Spinner_City_Adapter(Context context, ArrayList<CityModel> listData) {
		this.listcites = listData;
		layoutInflater = LayoutInflater.from(context);
		mContext = context;

	}
	

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return listcites.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return listcites.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@SuppressLint("InflateParams")
	public View getView(int position, View convertView, ViewGroup parent) 
	{
		CityViewHolder cityholder;
		if (convertView == null) 
		{
			convertView = layoutInflater.inflate(R.layout.cityspinner, null);
			cityholder = new CityViewHolder();
			cityholder.cityname = (TextView) convertView.findViewById(R.id.cityname);
			convertView.setTag(cityholder);
		}
		else 
		{
			cityholder = (CityViewHolder) convertView.getTag();
		}
		final CityModel cityitem = (CityModel) listcites.get(position);
		cityholder.cityname.setText(cityitem.getCity_name());
		return convertView;
		
	}
	
	static class CityViewHolder {
		TextView cityname;
	}

}
