package com.example.localevents;

import generics.JoinEvents;

import java.util.ArrayList;

import org.w3c.dom.Text;

import com.squareup.picasso.Picasso;

import models.EventModel;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Event_Datails extends Activity {
	
	EventModel em = new EventModel();
	TextView eventname ;
	TextView eventdescription ;
	TextView eventstartdate;
	TextView eventenddate;
	
	TextView eventstarttime ;
	TextView eventendtime ;
	
	TextView eventlocation ;
	ImageView eventimage ;
	
	TextView eventorganizer ;
	ImageView eventjoin;
	ImageView eventsave;
	ImageView attending;
	TextView event_category ;
	int save_status=2;
	int join_status=2;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.event__datails);
		
		Intent i =  getIntent();
		em =  (EventModel) i.getSerializableExtra("event_details");
		
		
		String join = em.getJoin_Status(); 
		String save = em.getSave_Status();	
		join_status = Integer.parseInt(join);  //get join status of the event
		save_status = Integer.parseInt(save);
		
		eventname = (TextView)findViewById(R.id.event_name);
		eventdescription = (TextView)findViewById(R.id.event_description);
		eventstartdate = (TextView)findViewById(R.id.event_date);
		eventenddate = (TextView)findViewById(R.id.end_date);
		eventstarttime = (TextView)findViewById(R.id.event_time);
		eventendtime = (TextView)findViewById(R.id.end_time);
		eventorganizer = (TextView)findViewById(R.id.event_org);
		eventimage = (ImageView)findViewById(R.id.eventphoto);
		eventlocation = (TextView)findViewById(R.id.event_location);
		event_category = (TextView)findViewById(R.id.event_category);
		// Attending button
		attending = (ImageView) findViewById(R.id.showattend);
		attending.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) 
			{
				String evt_id = em.getEvent_id();
//				Toast.makeText(Event_Datails.this, "event details id"+evt_id, Toast.LENGTH_LONG).show();
				Intent intent = new Intent(getApplicationContext(),com.example.localevents.Attendings.class);
				intent.putExtra("event_id",evt_id);
				startActivity(intent);
				
			}
		});
		
		
		eventsave = (ImageView)findViewById(R.id.save_event_icon);
		if(save_status==0)
		{
			eventsave.setImageResource(R.drawable.unsaved);
		}
		else if(save_status==1)
		{
			eventsave.setImageResource(R.drawable.saved);
		}
		
		eventjoin = (ImageView)findViewById(R.id.join_event_icon);
		if(join_status == 0)
		{
			eventjoin.setImageResource(R.drawable.joinme);
		}
		else if(join_status == 1)
		{
			eventjoin.setImageResource(R.drawable.unjoin);	
		}
		else
		{
			Log.d("Join_Status_2",""+join_status);
		}
		
		
		
		eventname.setText(em.getEvent_name());
		eventdescription.setText(em.getEvent_description());
		eventstartdate.setText(em.getEvent_start_date());
		eventstarttime.setText(em.getEvent_start_time());
		eventlocation.setText(em.getcity_name()+", "+em.getLocation_name());
		eventenddate.setText(em.getEvent_end_date());
		eventendtime.setText(em.getEvent_end_time());
		eventorganizer.setText(em.getOrg_name());
		event_category.setText(em.getCat_name()+" Category");
		
		String pathurl = em.getEvent_photo();		
		String url = "http://7girls.byethost7.com/public/"+pathurl;
		Picasso.with(Event_Datails.this).load(url).resize(350, 350).noFade().into(eventimage);
		
		//Toast.makeText(Event_Details.this, ""+em.getEvent_description(), Toast.LENGTH_LONG).show();
		
		
		eventsave.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(em.getSave_Status().equals("0"))
				{
					eventsave.setImageResource(R.drawable.saved);					
					 JoinEvents saveobj = new JoinEvents(Event_Datails.this) ;
					 em.setSave_Status(String.valueOf(1));
					 //listData.set(position, eventItem);
					 saveobj.save(Event_Datails.this,em.getEvent_id());
							
				}
				else if(em.getSave_Status().equals("1"))
				{
					eventsave.setImageResource(R.drawable.unsaved);
					em.setJoin_Status(String.valueOf(0));
					//listData.set(position, eventItem);
					JoinEvents unsave = new JoinEvents(Event_Datails.this) ;
					unsave.unsave(Event_Datails.this, em.getEvent_id());				
				
				}	
			}
		});
		
		
		
		eventjoin.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) 
			{
				if(em.getJoin_Status().equals("0"))
				{
					eventjoin.setImageResource(R.drawable.unjoin);					
					 JoinEvents joinobj = new JoinEvents(Event_Datails.this) ;
					 em.setJoin_Status(String.valueOf(1));
					 joinobj.join(Event_Datails.this,em.getEvent_id(),em.getEvent_name(),em.getEvent_start_date(),em.getEvent_start_time(),
							 em.getEvent_photo(),em.getLocation_name(),em.getEvent_description(),em.getOrg_name());
				}
				else if(em.getJoin_Status().equals("1"))
				{
					eventjoin.setImageResource(R.drawable.joinme);
					em.setJoin_Status(String.valueOf(0));
//					listData.set(position, eventItem);
					JoinEvents unjoin = new JoinEvents(Event_Datails.this) ;
					unjoin.unjoin(Event_Datails.this, em.getEvent_id());
					
				}
				
			}
		});
	}



}
