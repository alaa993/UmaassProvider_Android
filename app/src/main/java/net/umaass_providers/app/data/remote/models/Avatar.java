package net.umaass_providers.app.data.remote.models;

import com.google.gson.annotations.SerializedName;

public class Avatar{

	@SerializedName("url_md")
	private String urlMd;

	@SerializedName("url_sm")
	private String urlSm;

	@SerializedName("id")
	private int id;

	@SerializedName("url_lg")
	private String urlLg;

	@SerializedName("url_xs")
	private String urlXs;

	public void setUrlMd(String urlMd){
		this.urlMd = urlMd;
	}

	public String getUrlMd(){
		return urlMd;
	}

	public void setUrlSm(String urlSm){
		this.urlSm = urlSm;
	}

	public String getUrlSm(){
		return urlSm;
	}

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}

	public void setUrlLg(String urlLg){
		this.urlLg = urlLg;
	}

	public String getUrlLg(){
		return urlLg;
	}

	public void setUrlXs(String urlXs){
		this.urlXs = urlXs;
	}

	public String getUrlXs(){
		return urlXs;
	}
}