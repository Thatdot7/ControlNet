package edu.monash.controlnet.tasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.DhcpInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.util.Log;
import edu.monash.controlnet.model.NodeLocation;
import edu.monash.controlnet.model.NodeZone;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

public class ScanNetwork extends AsyncTask<Void, Void, Void> {
	Context context;
    private ProgressDialog dialog;
    List<NodeZone> result_list;
    NetworkResult mNetworkResult;
    DhcpInfo dhcp;
	
	public ScanNetwork(Context context){
		this.context = context;
        dialog = new ProgressDialog(context);
        mNetworkResult = (NetworkResult) context;
	}

    public interface NetworkResult{
        public void setupExpandableListView(List<NodeZone> clientList);
    }

	private InetAddress getBroadcastAddress() throws IOException {
        WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        dhcp = wifi.getDhcpInfo();
        // handle null somehow

        int broadcast = (dhcp.ipAddress & dhcp.netmask) | ~dhcp.netmask;
        byte[] quads = new byte[4];
        for (int k = 0; k < 4; k++)
          quads[k] = (byte) ((broadcast >> k * 8) & 0xFF);
        return InetAddress.getByAddress(quads);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        dialog.setMessage("Scanning...");
        dialog.setCancelable(false);
        dialog.show();
    }

    @Override
	protected Void doInBackground(Void... params) {
		// TODO Auto-generated method stub
		List<NodeZone> result_list = new ArrayList<NodeZone>();
		try {
			DatagramSocket socket = new DatagramSocket();
			socket.setBroadcast(true);
	    	JSONObject request_packet = new JSONObject();
            try {
                request_packet.put("type", "Device Request");
                request_packet.put("source", null);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            String data = request_packet.toString();
            DatagramPacket packet_send = new DatagramPacket(data.getBytes(), data.length(), getBroadcastAddress(), 1234);
	    	socket.send(packet_send);

	    	socket.setSoTimeout(5000);

	    	boolean hasZone = false;
	    	int index = 0;

            while(true){
                DatagramPacket packet_receive = new DatagramPacket(new byte[1024], 1024);
                try{
                    socket.receive(packet_receive);


                    byte[] data_receive = packet_receive.getData();

                    String response = new String(data_receive, "UTF-8");
                    try {
                        JSONArray response_jsonarray = new JSONArray(response);
                        JSONObject response_jsonobject = response_jsonarray.getJSONObject(0);


                        NodeLocation temp_nodeLocation = new NodeLocation(response_jsonobject, packet_receive.getAddress());

                        Log.i("Received Message", response_jsonobject.getString("Device"));

                        for(int i=0; i < result_list.size(); i++){
                            String zoneName = result_list.get(i).getName();
                            if (response_jsonobject.getString("Zone").equals(zoneName)) {
                                hasZone = true;
                                index = i;
                                break;
                            }
                        }

                        if(!hasZone){
                            ArrayList<NodeLocation> childList = new ArrayList<NodeLocation>();
                            childList.add(temp_nodeLocation);
                            NodeZone temp_nodeZone = new NodeZone(response_jsonobject.getString("Zone"), childList);
                            result_list.add(temp_nodeZone);
                        } else {
                            ArrayList<NodeLocation> childList = result_list.get(index).getChildList();
                            childList.add(temp_nodeLocation);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
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
        this.result_list = result_list;
		return null;
	}

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        mNetworkResult.setupExpandableListView(result_list);
        dialog.dismiss();
    }
}
