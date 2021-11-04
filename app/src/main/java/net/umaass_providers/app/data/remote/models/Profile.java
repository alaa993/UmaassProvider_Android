package net.umaass_providers.app.data.remote.models;

import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;

import net.umaass_providers.app.data.remote.utils.EmptyStringAsNullTypeAdapter;

public class Profile{

	@SerializedName("phone")
	private String phone;

	@SerializedName("name")
	private String name;

	@SerializedName("email")
	private String email;

	@SerializedName("gender")
	private int gender;

	@SerializedName("age")
	private int age;


	@SerializedName("birthdate")
	private String birthdate;

	@SerializedName("description")
	private String description;

	@SerializedName("id")
	private int id;

	@SerializedName("avatar")
	@JsonAdapter(EmptyStringAsNullTypeAdapter.class)
	private Avatar avatar;

	@SerializedName("introduce_code")
	private String introduce_code;

	public String getIntroduce_code() {
		return introduce_code;
	}

	public void setIntroduce_code(String introduce_code) {
		this.introduce_code = introduce_code;
	}

	public int getGender() {
		return gender;
	}

	public void setGender(int gender) {
		this.gender = gender;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getBirthdate() {
		return birthdate;
	}

	public void setBirthdate(String birthdate) {
		this.birthdate = birthdate;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setPhone(String phone){
		this.phone = phone;
	}

	public String getPhone(){
		return phone;
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Avatar getAvatar(){
		return avatar;
	}
}