package Adapters;

import java.util.ArrayList;

import com.example.localevents.R;
import com.squareup.picasso.Picasso;

import models.EventModel;
import models.UserModel;

import Adapters.EventsAdapter.ViewHolder;
import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class UsersAdapter  extends BaseAdapter{
	ArrayList<UserModel> listData;
	private LayoutInflater layoutInflater;
	public OnClickListener listener;
	private Context mContext;
	
	public UsersAdapter(Context context, ArrayList<UserModel> listData) {
		this.listData = listData;
		layoutInflater = LayoutInflater.from(context);
		mContext = context;

	}
	public void setButtonListener(OnClickListener listener) {
		this.listener = listener;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return listData.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return listData.get(position);
	}
	@Override
	public void notifyDataSetChanged() {
		// TODO Auto-generated method stub
		super.notifyDataSetChanged();
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	@SuppressLint("InflateParams")
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		final ViewHolder holder;
		final UserModel user = (UserModel)listData.get(position);
		if (convertView == null) 
		{
			convertView = layoutInflater.inflate(R.layout.user_item, null);
			holder = new ViewHolder();

			holder.user_name=(TextView) convertView.findViewById(R.id.name);
			holder.user_img=(ImageView) convertView.findViewById(R.id.img);
			
			
			convertView.setTag(holder);

		} 
		else 
		{
			holder = (ViewHolder) convertView.getTag();
		}

		holder.user_name.setText(user.getUser_name());
		try
		{
			String pathurl = user.getUser_image();		
			String url = "http://7girls.byethost7.com/public/"+pathurl;
			Log.d("image url", url);
			Picasso.with(mContext).load(url).resize(120, 120).noFade().into(holder.user_img);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		if (this.listener != null) {

		}
		return convertView;
	}
	static class ViewHolder {
		TextView user_name;
		ImageView user_img;
		
	}

}
