package com.gans.twittertestapp;

public class TweetsData {

	private String sFirstName;
	private String sTweet;
	private String imageUrl;

	public TweetsData(String sFirstName, String sTweet, String imageUrl) {
		this.sFirstName = sFirstName;
		this.sTweet = sTweet;
		this.imageUrl = imageUrl;
	}

	public String getFirstName() {
		return sFirstName;
	}

	public String getLastName() {
		return sTweet;
	}


	public String getImageUrl() {
		return imageUrl;
	}
}