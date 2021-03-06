package edu.monash.controlnet.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
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

        expandList = (ExpandableListView) findViewById(R.id.expandableListView1);

        if(savedInstanceState != null){
            expandAdapter = (CustomExpandableListAdapter) getLastNonConfigurationInstance();
            expandList.setAdapter(expandAdapter);
        }

        expandList.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                TextView address = (TextView) v.findViewById(R.id.txtIPAddress);
                String url = "http://" + address.getText().toString().substring(4);
                Intent launch_browser = new Intent(Intent.ACTION_VIEW);
                launch_browser.setData(Uri.parse(url));
                startActivity(launch_browser);
                return true;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.action_scan:
                try {
                    scanNetwork();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    
    
    public void scanNetwork() throws IOException {
        new ScanNetwork(this).execute();
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
