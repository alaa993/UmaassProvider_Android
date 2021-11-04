package net.umaass_providers.app.data.remote.models;

import com.google.gson.annotations.SerializedName;

public class Service{

	@SerializedName("duration")
	private int duration;

	@SerializedName("industry_id")
	private int industryId;

	@SerializedName("notes_for_the_customer")
	private String notesForTheCustomer;

	@SerializedName("price")
	private int price;

	@SerializedName("id")
	private int id;

	@SerializedName("title")
	private String title;

	@SerializedName("TypeTime")
	private String TypeTime;

	@SerializedName("TypePrice")
	private String TypePrice;

	public void setDuration(int duration){
		this.duration = duration;
	}

	public int getDuration(){
		return duration;
	}

	public void setIndustryId(int industryId){
		this.industryId = industryId;
	}

	public int getIndustryId(){
		return industryId;
	}

	public void setNotesForTheCustomer(String notesForTheCustomer){
		this.notesForTheCustomer = notesForTheCustomer;
	}

	public String getNotesForTheCustomer(){
		return notesForTheCustomer;
	}

	public void setPrice(int price){
		this.price = price;
	}

	public int getPrice(){
		return price;
	}

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}

	public void setTitle(String title){
		this.title = title;
	}

	public String getTitle(){
		return title;
	}

	public void setTypeTime(String TypeTime){
		this.TypeTime = TypeTime;
	}

	public String getTypeTime(){
		return TypeTime;
	}

	public void setTypePrice(String TypePrice){
		this.TypePrice = TypePrice;
	}

	public String getTypePrice(){
		return TypePrice;
	}
}