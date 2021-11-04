package net.umaass_providers.app.models;

import com.google.gson.annotations.SerializedName;


import net.umaass_providers.app.data.remote.models.User;

import java.util.List;

public class ModelIntroducer {

	@SerializedName("income")
	private int income;

	@SerializedName("name")
	private String name;


	@SerializedName("id")
	private int id;

	@SerializedName("rate")
	private float rate;

	@SerializedName("avatar")
	private Avatar avatar;

	@SerializedName("staffs")
	private List<Stafs> staff;

	public float getRate() {
		return rate;
	}

	public void setRate(float rate) {
		this.rate = rate;
	}
	public List<Stafs> getStaff() {
		return staff;
	}

	public void setStaff(List<Stafs> staff) {
		this.staff = staff;
	}

	public int getIncome(){
		return income;
	}

	public String getName(){
		return name;
	}

	public int getId(){
		return id;
	}

	public Avatar getAvatar(){
		return avatar;
	}

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

		public String getUrlMd(){
			return urlMd;
		}

		public String getUrlSm(){
			return urlSm;
		}

		public int getId(){
			return id;
		}

		public String getUrlLg(){
			return urlLg;
		}

		public String getUrlXs(){
			return urlXs;
		}
	}

	public class Stafs {

		@SerializedName("id")
		private int id;
		@SerializedName("user_id")
		private int user_id;

		@SerializedName("user")
		private User user;

		public int getUser_id() {
			return user_id;
		}

		public void setUser_id(int user_id) {
			this.user_id = user_id;
		}

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}

		public User getUser() {
			return user;
		}

		public void setUser(User user) {
			this.user = user;
		}
	}
}