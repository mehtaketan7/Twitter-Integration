package com.indianic.twitdemo;

import java.util.ArrayList;

public class TwitterModel {

	private static ArrayList<String> frnd=new ArrayList<String>();
	private static ArrayList<String> frndinfo=new ArrayList<String>();
	private  Long frndid;
	private  Long followersid;
	private String frndname;
	private String followersname;
	private int size;
	public static ArrayList<String> getFrnd() {
		return frnd;
	}

	public static void setFrnd(String frnd) {
		TwitterModel.frnd.add(frnd);
	}

	public static ArrayList<String> getFrndinfo() {
		return frndinfo;
	}

	public static void setFrndinfo(String frndinfo) {
		TwitterModel.frndinfo.add(frndinfo);
	}

	public Long getFrndid() {
		return frndid;
	}

	public void setFrndid(Long frndid) {
		this.frndid = frndid;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public Long getFollowersid() {
		return followersid;
	}

	public void setFollowersid(Long followersid) {
		this.followersid = followersid;
	}

	public String getFollowersname() {
		return followersname;
	}

	public void setFollowersname(String followersname) {
		this.followersname = followersname;
	}

	public String getFrndname() {
		return frndname;
	}

	public void setFrndname(String frndname) {
		this.frndname = frndname;
	}

	

	
}
