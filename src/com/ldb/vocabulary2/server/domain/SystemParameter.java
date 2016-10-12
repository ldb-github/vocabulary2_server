package com.ldb.vocabulary2.server.domain;

public class SystemParameter {

	private String name;
	private String value;
	private String explanation;
	
	public void clear(){
		this.name = null;
		this.value = null;
		this.explanation = null;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getExplanation() {
		return explanation;
	}
	public void setExplanation(String explanation) {
		this.explanation = explanation;
	}
	
	
}
