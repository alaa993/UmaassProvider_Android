package net.umaass_providers.app.data.remote.models;

import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;

import net.umaass_providers.app.data.remote.utils.EmptyStringAsNullTypeAdapter;

public class Industry{

	@SerializedName("image")
	@JsonAdapter(EmptyStringAsNullTypeAdapter.class)
	private Image image;

	@SerializedName("address")
	private String address;

	@SerializedName("lng")
	@JsonAdapter(EmptyStringAsNullTypeAdapter.class)
	private double lng;

	@SerializedName("phone")
	private String phone;

	@SerializedName("description")
	private String description;

	@SerializedName("id")
	private int id;

	@SerializedName("title")
	private String title;

	@SerializedName("category")
	@JsonAdapter(EmptyStringAsNullTypeAdapter.class)
	private Category category;

	@SerializedName("lat")
	@JsonAdapter(EmptyStringAsNullTypeAdapter.class)
	private double lat;

	public void setImage(Image image){
		this.image = image;
	}

	public Image getImage(){
		return image;
	}

	public void setAddress(String address){
		this.address = address;
	}

	public String getAddress(){
		return address;
	}

	public void setLng(double lng){
		this.lng = lng;
	}

	public double getLng(){
		return lng;
	}

	public void setPhone(String phone){
		this.phone = phone;
	}

	public String getPhone(){
		return phone;
	}

	public void setDescription(String description){
		this.description = description;
	}

	public String getDescription(){
		return description;
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

	public void setCategory(Category category){
		this.category = category;
	}

	public Category getCategory(){
		return category;
	}

	public void setLat(double lat){
		this.lat = lat;
	}

	public double getLat(){
		return lat;
	}
}