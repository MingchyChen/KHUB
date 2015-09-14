package com.claridy.common.mechanism.domain;

import java.util.ArrayList;
import java.util.List;

public class JSTree {
	
	private JSTreeData data ;

	// "closed" or "open", defaults to "closed"
	private String state;
	 
	private List<JSTree> children = new ArrayList<JSTree>();
	
	public JSTreeData getData() {
		return data;
	}

	public void setData(JSTreeData data) {
		this.data = data;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	
	public List<JSTree> getChildren() {
		return children;
	}

	public void setChildren(List<JSTree> children) {
		this.children = children;
	}
	
	public void addChildren(JSTree children) {
		this.children.add(children);
	}
}
