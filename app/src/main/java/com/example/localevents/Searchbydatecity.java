package com.example.localevents;

import java.util.ArrayList;

import models.EventModel;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;
import Adapters.EventsAdapter;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.ListView;

public class Searchbydatecity extends Activity {
	Context searchcontext;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_searchbydatecity);
		
		  Intent in = getIntent();

		     ArrayList<EventModel> events_list = new ArrayList<EventModel>();

		     events_list = (ArrayList<EventModel>) in.getSerializableExtra("events");
		     final ArrayList<EventModel> ev_list= events_list;
		     ListView listView = (ListView) findViewById(R.id.SearchEventsResult);
		     listView.setAdapter(new EventsAdapter(this, events_list));
//		     View view= View.inflate(ge, resource, root)
		     listView.setOnItemClickListener(new OnItemClickListener() {
				

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						Toast.makeText(getBaseContext(),"item clicked", Toast.LENGTH_LONG).show();
						// TODO Auto-generated method stub
						Intent i = new Intent(getBaseContext(),Event_Datails.class); 
						i.putExtra("event_details", ev_list.get(position));
						startActivity(i);
						
					}
				});
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.searchbydatecity, menu);
		return true;
	}

}
