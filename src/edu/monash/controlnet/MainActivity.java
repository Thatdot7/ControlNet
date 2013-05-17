package edu.monash.controlnet;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import edu.monash.controlnet.adapters.CustomExpandableListAdapter;
import edu.monash.controlnet.model.NodeZone;
import edu.monash.controlnet.tasks.ScanNetwork;

import android.net.DhcpInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.TextView;

public class MainActivity extends Activity {
	
	TextView txtStatus;
	ExpandableListView expandList;
	

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       
        txtStatus = (TextView) findViewById(R.id.txtStatus);
        expandList = (ExpandableListView) findViewById(R.id.expandableListView1);
        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    
    
    public void scanNetwork(View view) throws IOException {
    	ArrayList<NodeZone> clientList = new ArrayList<NodeZone>();
		try {
            clientList = new ScanNetwork(this).execute().get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        CustomExpandableListAdapter expandAdapter = new CustomExpandableListAdapter(this, clientList);
        expandList.setAdapter(expandAdapter);
        expandList.refreshDrawableState();

        int number = expandAdapter.getGroupCount();
        String number_str = Integer.toString(number);
        txtStatus.setText(number_str);
    }
    	
}
