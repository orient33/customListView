package com.example.listviewtext;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

public class MainActivity extends Activity {

	MyListView mLv;
	ListView normalListView;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        mLv=new MyListView(this);
        ListAdapter adatper =new ArrayAdapter<String>(
        		this,android.R.layout.simple_expandable_list_item_1,getData());
        mLv.setAdapter(adatper);
        
        normalListView=new ListView(this);        
        normalListView.setAdapter(new ArrayAdapter<String>(
        		this,android.R.layout.simple_expandable_list_item_1,getData()));
//        LinearLayout ll=new ListView2(this,R.layout.linear);
        
        
//        ListView2 ll=new ListView2(this);
//        ll.getListView().setAdapter(adatper);
        
        ListViewNoScrollWithPage m2 = new ListViewNoScrollWithPage(this);
        m2.setAdapter(adatper);
        setContentView(m2);
    }


   List<String> getData(){
	   List<String> data=new ArrayList<String>();
	   int i=0;
		do {
			data.add(" == "+(i++) + " ==");
		} while (i < 43);
	   return data;
   }
    
}
