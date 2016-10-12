package com.ldb.vocabulary2.server.domain;

import java.sql.Timestamp;

public class Vocabulary {


	private String id;
	private String cId;
	private String name;
	private String image;
	private String imageRemote;
	private String username;
	private Timestamp createTime;
	private String translation;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getCId() {
		return cId;
	}
	public void setCId(String cId) {
		this.cId = cId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public String getImageRemote() {
		return imageRemote;
	}
	public void setImageRemote(String imageRemote) {
		this.imageRemote = imageRemote;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public Timestamp getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}
	public String getTranslation() {
		return translation;
	}
	public void setTranslation(String translation) {
		this.translation = translation;
	}
	
}
