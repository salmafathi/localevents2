package Adapters;
import generics.JoinEvents;

import java.io.InputStream;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import com.example.localevents.R;
import com.squareup.picasso.Picasso;

import models.EventModel;


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
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View.OnClickListener;


public class EventsAdapter extends BaseAdapter {

	
	private ArrayList<EventModel> listData;
	private LayoutInflater layoutInflater;
	public OnClickListener listener;
	private Context mContext;
	
	
	
	JoinEvents joinobj ;	
	int save_status=2;
	int join_status=2;
	// Users newsItem1;
	Bitmap bitmap ;
	

	public EventsAdapter(Context context, ArrayList<EventModel> listData) {
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
	public void notifyDataSetChanged() {
		// TODO Auto-generated method stub
		super.notifyDataSetChanged();
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@SuppressWarnings("deprecation")
	@SuppressLint("InflateParams")
	public View getView(final int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		final EventModel eventItem = (EventModel) listData.get(position);		
		String join = eventItem.getJoin_Status(); 
		String save = eventItem.getSave_Status();
		
		
		///get the event start date
			String date = eventItem.getEvent_start_date(); 
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			//SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
			try {
				Date eventdate = dateFormat.parse(date);
				if (new Date().before(eventdate)) {
					
					Log.d("this event :",eventItem.getEvent_name()+" : still available");
				}
				else{
					Log.d("this event :",eventItem.getEvent_name()+" : NOT available");
				}
				
//				if(System.currentTimeMillis() > eventdate.getTime()){
//					
//				}
				int event_date = eventdate.getDate();				
				Log.d("event date : ", String.valueOf(event_date));
				} 
			catch (ParseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		
		
		
		//get the current date
			Calendar c = Calendar.getInstance(); 
			int today_date = c.getTime().getDate();
			
			Log.d("today date : ", String.valueOf(today_date));
			
	  //Compare the event_date with the Current_date
			
		
		
		/////
		join_status = Integer.parseInt(join);  //get join status of the event
		save_status = Integer.parseInt(save);
		
		Log.d("staaaaatus",""+join_status);
		
		if (convertView == null) 
		{
			convertView = layoutInflater.inflate(R.layout.event_item, null);
			holder = new ViewHolder();

			holder.event_name=(TextView) convertView.findViewById(R.id.eventname);
			holder.event_start_date=(TextView) convertView.findViewById(R.id.eventdate);
			holder.event_location=(TextView) convertView.findViewById(R.id.eventplace);
			holder.event_img= (ImageView) convertView.findViewById(R.id.eventphoto);
			holder.save_event= (ImageView) convertView.findViewById(R.id.save);
			if(save_status==0)
			{
				holder.save_event.setImageResource(R.drawable.unsaved);
			}
			else if(save_status==1)
			{
				holder.save_event.setImageResource(R.drawable.saved);
			}
			
			holder.join_event=(ImageView) convertView.findViewById(R.id.join);
			if(join_status == 0)
			{
				holder.join_event.setImageResource(R.drawable.joinme);
			}
			else if(join_status == 1)
			{
				holder.join_event.setImageResource(R.drawable.unjoin);	
			}
			else
			{
				Log.d("Join_Status_2",""+join_status);
			}
			convertView.setTag(holder);

		} 
		else 
		{
			holder = (ViewHolder) convertView.getTag();
		}

		  /////////////////////

		holder.event_name.setText(eventItem.getEvent_name());
		holder.event_location.setText("at: "+eventItem.getLocation_name());
		holder.event_start_date.setText("Date: "+eventItem.getEvent_start_date());
		
		try
		{
			String pathurl = eventItem.getEvent_photo();		
			String url = "http://7girls.byethost7.com/public/"+pathurl;
			Log.d("image url", url);
			Picasso.with(mContext).load(url).resize(120, 120).noFade().into(holder.event_img);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		holder.save_event.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(eventItem.getSave_Status().equals("0"))
				{
					holder.save_event.setImageResource(R.drawable.saved);					
					 JoinEvents saveobj = new JoinEvents(mContext) ;
					 eventItem.setSave_Status(String.valueOf(1));
					 listData.set(position, eventItem);
					 saveobj.save(mContext,eventItem.getEvent_id());
					 notifyDataSetChanged();
							
				}
				else if(eventItem.getSave_Status().equals("1"))
				{
					holder.save_event.setImageResource(R.drawable.unsaved);
					eventItem.setJoin_Status(String.valueOf(0));
					listData.set(position, eventItem);
					JoinEvents unsave = new JoinEvents(mContext) ;
					unsave.unsave(mContext, eventItem.getEvent_id());
					notifyDataSetChanged();
				
				
				}	
			}
		});
		
		holder.join_event.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) 
			{
				if(eventItem.getJoin_Status().equals("0"))
				{
					//join_status= 1;
					holder.join_event.setImageResource(R.drawable.unjoin);					
					 joinobj = new JoinEvents(mContext) ;
					 eventItem.setJoin_Status(String.valueOf(1));
					 listData.set(position, eventItem);
					 Log.d("staaaaatus","join status from list join"+listData.get(position).getJoin_Status());
					 joinobj.join(mContext,eventItem.getEvent_id(),eventItem.getEvent_name(),eventItem.getEvent_start_date(),eventItem.getEvent_start_time(),
							 eventItem.getEvent_photo(),eventItem.getLocation_name(),eventItem.getEvent_description(),eventItem.getOrg_name());
				//	Log.d("iiiiddddddddd",  eventItem.getEvent_id());
					 Log.d("staaaaatus", "status here is :"+eventItem.getJoin_Status());
					 notifyDataSetChanged();	
							
				}
				else if(eventItem.getJoin_Status().equals("1"))
				{
					//join_status=0;
					holder.join_event.setImageResource(R.drawable.joinme);
					eventItem.setJoin_Status(String.valueOf(0));
					listData.set(position, eventItem);
					Log.d("staaaaatus","join status from list undelete"+listData.get(position).getJoin_Status());
					Log.d("unnnnnnn", ""+join_status)	;								
					JoinEvents unjoin = new JoinEvents(mContext) ;
					unjoin.unjoin(mContext, eventItem.getEvent_id());
					
			//		Log.d("iiiiddddddddd",  eventItem.getEvent_id());

					notifyDataSetChanged();
				}
				
			}
		});
		
		
		
		
		if (this.listener != null) {

		}
		return convertView;
	}

	static class ViewHolder {
		TextView event_name;
		ImageView event_img;
		TextView event_start_date;
		TextView event_location;
		ImageView save_event;
		ImageView join_event;
	}
}
