package com.example.localevents;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;

import android.app.ActionBar.TabListener;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;



public class JoinedSavedActivity  extends FragmentActivity implements
ActionBar.TabListener
{

	private ViewPager viewPager;
	private SavedJoinedTabAdapter mAdapter;
	private ActionBar actionBar;
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.joinedsavedactivity);
		
		viewPager = (ViewPager) findViewById(R.id.joinedSavedpager);
		actionBar = getActionBar();
		mAdapter = new SavedJoinedTabAdapter(getSupportFragmentManager());
		final int[] ICONS = new int[] {
		        R.drawable.saved,
		        R.drawable.joinme,       
		};
		viewPager.setAdapter(mAdapter);

		actionBar.setHomeButtonEnabled(false);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		
		for (int i=0; i < ICONS.length; i++)
		{
		actionBar.addTab(actionBar.newTab()
		                         .setIcon(getResources().getDrawable(ICONS[i]))
		                         .setTabListener((TabListener) this));
		}
		
		viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				// on changing the page
				// make respected tab selected
				actionBar.setSelectedNavigationItem(position);

			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});
		
		Intent i = getIntent();
		if(i != null){
			//viewPager.setCurrentItem(2);
		}
		//viewPager.setCurrentItem(2);
			setTitle("Events");
			getActionBar().setIcon(R.drawable.logo);
		}
	
	
	@Override
	public void onTabReselected(Tab arg0, FragmentTransaction arg1) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		if(tab.getPosition()==0)
		{
			tab.setIcon(R.drawable.saved);
			setTitle("Saved");
		}
		else if(tab.getPosition()==1)
		{	
			tab.setIcon(R.drawable.joinme);
			setTitle("joined");
		}
		viewPager.setCurrentItem(tab.getPosition());
		
	}
	@Override
	public void onTabUnselected(Tab arg0, FragmentTransaction arg1) {
		// TODO Auto-generated method stub
		
	}
		
	
		
	
	
	

	}
	
	
