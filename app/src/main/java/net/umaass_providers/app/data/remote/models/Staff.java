package net.umaass_providers.app.data.remote.models;

import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;

import net.umaass_providers.app.data.remote.utils.EmptyStringAsNullTypeAdapter;

public class Staff{

	@SerializedName("industry_id")
	private int industryId;

	@SerializedName("role")
	private String role;

	@SerializedName("user_id")
	private int userId;

	@SerializedName("name")
	private String name;

	@SerializedName("id")
	private int id;

	@SerializedName("avatar")
	@JsonAdapter(EmptyStringAsNullTypeAdapter.class)
	private Avatar avatar;

	public void setIndustryId(int industryId){
		this.industryId = industryId;
	}

	public int getIndustryId(){
		return industryId;
	}

	public void setRole(String role){
		this.role = role;
	}

	public String getRole(){
		return role;
	}

	public void setUserId(int userId){
		this.userId = userId;
	}

	public int getUserId(){
		return userId;
	}

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}

	public void setAvatar(Avatar avatar){
		this.avatar = avatar;
	}

	public Avatar getAvatar(){
		return avatar;
	}
}