package edu.monash.controlnet.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.TextView;
import edu.monash.controlnet.R;
import edu.monash.controlnet.adapters.CustomExpandableListAdapter;
import edu.monash.controlnet.model.NodeZone;
import edu.monash.controlnet.tasks.ScanNetwork;

import java.io.IOException;
import java.util.List;

public class MainActivity extends Activity implements ScanNetwork.NetworkResult{
	
	TextView txtStatus;
	ExpandableListView expandList;
    CustomExpandableListAdapter expandAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtStatus = (TextView) findViewById(R.id.txtStatus);
        expandList = (ExpandableListView) findViewById(R.id.expandableListView1);

        if(savedInstanceState != null){
            expandAdapter = (CustomExpandableListAdapter) getLastNonConfigurationInstance();
            expandList.setAdapter(expandAdapter);
        }

        expandList.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                    Intent i = new Intent(getApplicationContext(), NodeSite.class);
                    TextView txtIPAddress = (TextView) v.findViewById(R.id.txtIPAddress);
                    i.putExtra("IP Address", expandAdapter.getChild(groupPosition, childPosition).getAddress().toString().substring(1));
                    startActivity(i);
                return false;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    
    
    public void scanNetwork(View view) throws IOException {
        new ScanNetwork(this).execute();

        txtStatus.setText("The button has been clicked");
    }

    @Override
    public CustomExpandableListAdapter onRetainNonConfigurationInstance() {
        return expandAdapter;
    }

    public void setupExpandableListView(List<NodeZone> clientList){
        expandAdapter = new CustomExpandableListAdapter(this, clientList);
        expandList.setAdapter(expandAdapter);
    }
}
