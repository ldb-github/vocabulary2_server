package com.ldb.vocabulary2.server.domain;

import java.sql.Timestamp;

public class Category {

	private String id;
	private String name;
	private String image;
	private String imageRemote;
	private long favoriteCount;
	private long wordCount;
	private String language;
	private String username;
	private Timestamp createTime;
	private String translation;
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
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
	public long getFavoriteCount() {
		return favoriteCount;
	}
	public void setFavoriteCount(long favorateCount) {
		this.favoriteCount = favorateCount;
	}
	public long getWordCount() {
		return wordCount;
	}
	public void setWordCount(long wordCount) {
		this.wordCount = wordCount;
	}
	public String getLanguage() {
		return language;
	}
	public void setLanguage(String language) {
		this.language = language;
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

