package com.example.localevents;

import models.CategoriesHelper;
import models.DBHelper;
import models.InterestsHelper;
import generics.sharedprefs;
import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.app.ActionBar.Tab;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class MainActivityUser extends FragmentActivity implements
ActionBar.TabListener {

private ViewPager viewPager;
private TabsPagerAdapter mAdapter;
private ActionBar actionBar;
DBHelper db;
InterestsHelper interestHelper;
CategoriesHelper catHelper;
// Tab titles
//private String[] tabs = { "Browse"};

@Override
protected void onCreate(Bundle savedInstanceState) {
super.onCreate(savedInstanceState);
setContentView(R.layout.main_activity_user);

// Initilization
viewPager = (ViewPager) findViewById(R.id.pager);
actionBar = getActionBar();
mAdapter = new TabsPagerAdapter(getSupportFragmentManager());
final int[] ICONS = new int[] {
        R.drawable.ic_browse,
        R.drawable.ic_categories,
        R.drawable.ic_user,
        R.drawable.search
        
};

viewPager.setAdapter(mAdapter);

db=new DBHelper(this);
InterestsHelper.cn = getApplicationContext();
CategoriesHelper.cn = getApplicationContext();
interestHelper = new InterestsHelper();
catHelper = new CategoriesHelper();

actionBar.setHomeButtonEnabled(false);
actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
//actionBar.setBackgroundDrawable(new ColorDrawable(Color.GREEN));

//viewPager.setCurrentItem(2);
//viewPager.setBackgroundColor(Color.MAGENTA);
//viewPager.setBackground(new ColorDrawable(Color.GREEN));

// Adding Tabs
for (int i=0; i < ICONS.length; i++)
{
actionBar.addTab(actionBar.newTab()
                         .setIcon(getResources().getDrawable(ICONS[i]))
                         .setTabListener(this));
}//endfor

/**
 * on swiping the viewpager make respective tab selected
 * */

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
setTitle("Me");
getActionBar().setIcon(R.drawable.logo);
}

@Override
public void onTabReselected(Tab tab, FragmentTransaction ft) {
}

@Override
public void onTabSelected(Tab tab, FragmentTransaction ft) {
// on tab selected
	if(tab.getPosition()==0)
	{
		tab.setIcon(R.drawable.ic_browse_pressed);
		setTitle("Browse");
	}
	else if(tab.getPosition()==1)
	{	
		tab.setIcon(R.drawable.ic_categories_pressed);
		setTitle("Categories");
	}
	else if(tab.getPosition()==2)
	{	
		tab.setIcon(R.drawable.ic_user_pressed);
		setTitle("Me");
	}
	else if(tab.getPosition()==3)
	{
		tab.setIcon(R.drawable.searchicon);
		setTitle("search");
	}
// show respected fragment view
viewPager.setCurrentItem(tab.getPosition());
}

@Override
public void onTabUnselected(Tab tab, FragmentTransaction ft) {
	if(tab.getPosition()==0)
		tab.setIcon(R.drawable.ic_browse);
	else if(tab.getPosition()==1)
		tab.setIcon(R.drawable.ic_categories);
	else if(tab.getPosition()==2)
		tab.setIcon(R.drawable.ic_user);
	else if(tab.getPosition()==3)
	{
		tab.setIcon(R.drawable.search);
	}
}
private void goToSignOutAndBackToHome() {
	// TODO Auto-generated method stub
	sharedprefs.deleteDefaults(this);
    interestHelper.deleteInterests();
    db.deleteData();
    catHelper.deleteCats();
    Intent i = new Intent(this,com.example.localevents.Home.class);
    startActivity(i);
    finishAffinity();
	
}
@Override
public boolean onCreateOptionsMenu(Menu menu) {
	// TODO Auto-generated method stub
MenuInflater inflater = getMenuInflater();
inflater.inflate(R.menu.main, menu);

return super.onCreateOptionsMenu(menu);
}
@Override
public boolean onOptionsItemSelected(MenuItem item) {
	// TODO Auto-generated method stub
switch (item.getItemId()) {

case R.id.signOut:
{
	goToSignOutAndBackToHome();
	return true;
}
case R.id.edit:
{
	Intent i = new Intent(this,com.example.localevents.EditProfile.class);
    startActivity(i);
    return true;
}
case R.id.action_refresh:{
	//Fragment page = getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.pager + ":" + viewPager.getCurrentItem());
	//Toast.makeText(this, ""+viewPager.getCurrentItem(), Toast.LENGTH_SHORT).show();
//	mAdapter.notifyDataSetChanged();
//	//Toast.makeText(this, ""+page.toString(), Toast.LENGTH_SHORT).show();
//	if(page instanceof ShowAllEvents){
//		Toast.makeText(this, "yes", Toast.LENGTH_SHORT).show();
//		mAdapter.getItem(0);
//		viewPager.getCurrentItem();
//	}
	
	startActivity(new Intent(this,MainActivityUser.class));
	finish();
	return true;
}

default:
	return super.onOptionsItemSelected(item);
}
}
}


