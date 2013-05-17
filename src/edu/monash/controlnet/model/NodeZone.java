package edu.monash.controlnet.model;

import java.util.ArrayList;

public class NodeZone {
	private String name;
	private ArrayList<NodeLocation> childList;
	
	public NodeZone(String name, ArrayList<NodeLocation> childList){
		this.name = name;
		this.childList = childList;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	public void setChildList(ArrayList<NodeLocation> childlist) {
		this.childList = childlist;
	}
	public String getName(){
		return this.name;
	}
	public ArrayList<NodeLocation> getChildList() {
		return this.childList;
	}
}
