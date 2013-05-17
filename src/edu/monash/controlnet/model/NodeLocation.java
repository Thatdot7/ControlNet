package edu.monash.controlnet.model;

import java.net.InetAddress;

public class NodeLocation {
	private InetAddress address;
	private String node_type;
	
	public NodeLocation(String node_type, InetAddress address) {
		this.node_type = node_type;
		this.address = address;
	}
	
	public void setAddress(InetAddress address){
		this.address = address;
	}
	public void setNodeType(String node_type){
		this.node_type = node_type;
	}
	public InetAddress getAddress(){
		return address;
	}
	public String getNodeType(){
		return node_type;
	}
	
}
