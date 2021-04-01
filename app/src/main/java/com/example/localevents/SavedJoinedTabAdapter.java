package com.example.localevents;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

public class SavedJoinedTabAdapter extends FragmentPagerAdapter {

	public SavedJoinedTabAdapter(FragmentManager fm) {
		super(fm);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Fragment getItem(int index) {
		// TODO Auto-generated method stub
		
		switch (index) {
		case 0:{
			// Top Rated fragment activity
			Log.d("pooooooosition", index+"");

			return new SavedEventsTab();

		}
		case 1:
			// Games fragment activity
			Log.d("pooooooosition", index+"");

			return new JoinedEventsTab();

		}
		return null;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 2;
	}

}
