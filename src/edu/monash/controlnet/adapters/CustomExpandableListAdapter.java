package edu.monash.controlnet.adapters;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;
import edu.monash.controlnet.R;
import edu.monash.controlnet.model.NodeLocation;
import edu.monash.controlnet.model.NodeZone;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class CustomExpandableListAdapter extends BaseExpandableListAdapter{
	
	private Context context;
	private List<NodeZone> list_content;
	
	public CustomExpandableListAdapter(Context context, List<NodeZone> list_content){
		this.context = context;
		this.list_content = list_content;
	}

	@Override
	public NodeLocation getChild(int arg0, int arg1) {
		// TODO Auto-generated method stub
		Log.i("Expand Adapter", "Trying to get Child");
		return list_content.get(arg0).getChildList().get(arg1);
	}

	@Override
	public long getChildId(int arg0, int arg1) {
		// TODO Auto-generated method stub
		
		//arg0 = Group Position
		//arg1 = Child Position
        Log.i("Expand Adapter", "Trying to get ChildId");
		return arg1;
	}

	@Override
	public View getChildView(int arg0, int arg1, boolean arg2, View arg3, ViewGroup arg4) {
		// TODO Auto-generated method stub

		if(arg3 == null){
			LayoutInflater inf = (LayoutInflater) 	context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			arg3 = inf.inflate(R.layout.expandable_child, null);
		}

		TextView txtDevice = (TextView) arg3.findViewById(R.id.txtDevice);
        TextView txtAddress = (TextView) arg3.findViewById(R.id.txtIPAddress);
        try {
            txtDevice.setText(getChild(arg0, arg1).getInfo().getString("Device"));
            txtAddress.setText("IP: " + getChild(arg0, arg1).getAddress().toString().substring(1));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.i("Expandable Adapter", "Child View is being created");
		return arg3;
	}

	@Override
	public int getChildrenCount(int arg0) {
		// TODO Auto-generated method stub

        int count = list_content.get(arg0).getChildList().size();
        Log.i("Expand Adapter", Integer.toString(count));
		return count;
	}

	@Override
	public NodeZone getGroup(int arg0) {
		// TODO Auto-generated method stub
		return list_content.get(arg0);
	}

	@Override
	public int getGroupCount() {
		// TODO Auto-generated method stub
		return list_content.size();
	}

	@Override
	public long getGroupId(int arg0) {
		// TODO Auto-generated method stub
        Log.i("Expand Adapter", Integer.toString(arg0));
        return arg0;
	}

	@Override
	public View getGroupView(int arg0, boolean arg1, View arg2, ViewGroup arg3) {
		// TODO Auto-generated method stub
		NodeZone parent = list_content.get(arg0);
		if(arg2 == null){
			LayoutInflater inflater = (LayoutInflater) 	context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			arg2 = inflater.inflate(R.layout.expandable_parent, null);
			
		}
		
		TextView txtParent = (TextView) arg2.findViewById(R.id.txtExapandableParent);
		txtParent.setText(parent.getName());
        Log.i("Expandable Adapter", "Parent View is being created");
		return arg2;
	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isChildSelectable(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return true;
	}

}
