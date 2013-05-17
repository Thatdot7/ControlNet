package edu.monash.controlnet.tasks;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.nio.charset.Charset;
import java.util.ArrayList;

import edu.monash.controlnet.model.NodeLocation;
import edu.monash.controlnet.model.NodeZone;

import android.content.Context;
import android.net.DhcpInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.util.Log;

public class ScanNetwork extends AsyncTask<Void, Void, ArrayList<NodeZone>> {
	Context context;
	
	public ScanNetwork(Context context){
		this.context = context;
	}
	
	private InetAddress getBroadcastAddress() throws IOException {
        WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        DhcpInfo dhcp = wifi.getDhcpInfo();
        // handle null somehow

        int broadcast = (dhcp.ipAddress & dhcp.netmask) | ~dhcp.netmask;
        byte[] quads = new byte[4];
        for (int k = 0; k < 4; k++)
          quads[k] = (byte) ((broadcast >> k * 8) & 0xFF);
        return InetAddress.getByAddress(quads);
    }

	@Override
	protected ArrayList<NodeZone> doInBackground(Void... params) {
		// TODO Auto-generated method stub
		ArrayList<NodeZone> result_list = new ArrayList<NodeZone>();
		try {
			DatagramSocket socket = new DatagramSocket();
			socket.setBroadcast(true);
	    	String data = "Oh Hai!";
	    	DatagramPacket packet_send = new DatagramPacket(data.getBytes(), data.length(), getBroadcastAddress(), 1234);
	    	socket.send(packet_send);
	    	Log.i("Packets Task", "Packet Sent");

	    	socket.setSoTimeout(5000);
	    	
	    	boolean hasZone = false;
	    	int index = 0;
	    	
	    	while(true){
	    	DatagramPacket packet_receive = new DatagramPacket(new byte[1024], 1024);
	    		try{
	    			socket.receive(packet_receive);


                    byte[] data_receive = packet_receive.getData();
//                    InputStreamReader input = new InputStreamReader(new ByteArrayInputStream(data_receive), Charset.forName("UTF-8"));
//
//                    StringBuilder response_builder = new StringBuilder();
//                    for (int value; (value = input.read()) != -1; ){
//                        response_builder.append((char) value);
//                    }
                    String response = new String(data_receive, "UTF-8");
                    NodeLocation temp_nodeLocation = new NodeLocation(response, packet_receive.getAddress());

                    Log.i("Received Message", response);
                    if(result_list != null){
                        for(int i=0; i < result_list.size(); i++){
                            String zoneName = result_list.get(i).getName();
                            if (response.equals(zoneName)) {
                                hasZone = true;
                                index = i;
                                break;
                            }
                        }
                    }

	    			if(!hasZone){
	    				ArrayList<NodeLocation> childList = new ArrayList<NodeLocation>();
	    				childList.add(temp_nodeLocation);
	    				NodeZone temp_nodeZone = new NodeZone(response, childList);
	    				result_list.add(temp_nodeZone);
	    			} else {
	    				ArrayList<NodeLocation> childList = result_list.get(index).getChildList();
	    				childList.add(temp_nodeLocation);
	    			}
	    			
	    			Log.i("Device IP", packet_receive.getAddress().toString());
	    		} catch (SocketTimeoutException e){
	    			Log.i("Packet Task", "Receive Loop has been broken");
	    			break;
	    		}
	    	}
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
		Log.i("Packet Task", "End of Task");
		return result_list;
	}
}
