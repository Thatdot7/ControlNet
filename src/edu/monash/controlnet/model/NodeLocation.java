package edu.monash.controlnet.model;

import org.json.JSONObject;

import java.net.InetAddress;

public class NodeLocation {
	private InetAddress address;
	private JSONObject info;
	
	public NodeLocation(JSONObject info, InetAddress address) {
		this.info = info;
		this.address = address;
	}
	
	public void setAddress(InetAddress address){
		this.address = address;
	}
	public void setNodeType(JSONObject info){
		this.info = info;
	}
	public InetAddress getAddress(){
		return address;
	}
	public JSONObject getInfo(){
		return info;
	}
	
}
