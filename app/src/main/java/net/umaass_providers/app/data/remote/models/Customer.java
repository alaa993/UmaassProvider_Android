package net.umaass_providers.app.data.remote.models;

import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;

import net.umaass_providers.app.data.remote.utils.EmptyStringAsNullTypeAdapter;

public class Customer{

	@SerializedName("name")
	private String name;

	@SerializedName("phone")
	private String phone;

	@SerializedName("description")
	private String description;

	@SerializedName("id")
	private int id;

	@SerializedName("avatar")
	@JsonAdapter(EmptyStringAsNullTypeAdapter.class)
	private Avatar avatar;

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
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

	public void setAvatar(Avatar avatar){
		this.avatar = avatar;
	}

	public Avatar getAvatar(){
		return avatar;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Customer customer = (Customer) o;

		if (id != customer.id) return false;
		if (name != null ? !name.equals(customer.name) : customer.name != null) return false;
		if (description != null ? !description.equals(customer.description) : customer.description != null)
			return false;
		return avatar != null ? avatar.equals(customer.avatar) : customer.avatar == null;
	}

	@Override
	public int hashCode() {
		int result = name != null ? name.hashCode() : 0;
		result = 31 * result + (description != null ? description.hashCode() : 0);
		result = 31 * result + id;
		result = 31 * result + (avatar != null ? avatar.hashCode() : 0);
		return result;
	}
}