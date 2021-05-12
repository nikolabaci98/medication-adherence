// this class will need to use the remote db jar file
package edu.cuny.qc.cs.medication_management.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.sql.*;
import java.util.*;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class User implements Parcelable {

	private String userID;
	private String username;
	private String phoneNumber;
	private String fullname;
	private boolean hasCaregiver;
	boolean changeUserName = false;
	private MedicationList list;
	public User() {
		this.userID = null;
		this.fullname = null;
		this.username = null;
		this.phoneNumber = null;
		this.hasCaregiver = false;
		list = new MedicationList();
	}
	public User(String uname, String pword, String fname, boolean hasCaregiver) {
		this.username = uname;
		this.phoneNumber = pword;
		this.fullname = fname;
		this.hasCaregiver = hasCaregiver;
		list = new MedicationList();
		//setUserName();
		//setUserID(); --> to generate a unique code for the user's id
	}

	public static final Creator<User> CREATOR = new Creator<User>() {
		@Override
		public User createFromParcel(Parcel in) {
			return new User(in);
		}

		@Override
		public User[] newArray(int size) {
			return new User[size];
		}
	};

	public String getUserID() {
		return userID;
	}
	public String getUserName() {
		return username;
	}
	public String getphoneNumber() {
		return phoneNumber;
	}
	public String getFullName() {
		return fullname;
	}
	public boolean getCaregiverStatus(){ return hasCaregiver;}

	public MedicationList getMedicationList() {
		return list;
	}

	public void setUserID(boolean t) {
		String temp = "";
		Random rand = new Random();
		for(int i = 0; i < 10; i++){
			int x = rand.nextInt(10);
			temp += x;
		}
		userID = temp;
		accountChecks ac = new accountChecks();
		while(!ac.checkUserIDUnique(this)){
			setUserID(t);
		}
		t = true;
	}
	public void setUserID2(String ID){
		this.userID = ID;
	}
	public void setUserName(String uname) {
		this.username = uname;
	}
	public boolean isUniqueUserName() {
		if(this.username.equals("ERRORafterPost979")) return false;
		accountChecks ac = new accountChecks();
		//ac.checkUserNameUnique(this);
		if(ac.checkUserNameUnique(this)){
			return true;
		}
		else return false;
	}
	public void setphoneNumber(String pN) {
		this.phoneNumber = pN;
	}
	public void setFullName(String fname) {
		this.fullname = fname;
	}
	public void setCaregiverStatus(boolean hasCaregiver){this.hasCaregiver =  hasCaregiver;}
	public void setMedicationList(){
		if(this.list == null){
			this.list=  new MedicationList();
		}
		list.populateList(userID);
	}
	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(username);
		dest.writeString(phoneNumber);
		dest.writeString(userID);
		dest.writeString(fullname);
		String hcg = String.valueOf(hasCaregiver);
		dest.writeString(hcg);

	}
	public User(Parcel in){
		username = in.readString();
		phoneNumber = in.readString();
		userID = in.readString();
		fullname = in.readString();
		String test = in.readString();
		if(test.equals("false")) hasCaregiver = false;
		else hasCaregiver = true;
	}

	private class accountChecks{
		Executor exec =  Executors.newSingleThreadExecutor();
		boolean done = false;
		public void doUserIDCheck(User temp){

			exec.execute(() -> {
				try {
					URL link = new URL("http://68.198.11.61:8089/testCheckID/PerformCheck");
					HttpURLConnection connect = (HttpURLConnection) link.openConnection();
					connect.setRequestMethod("POST");
					connect.setDoOutput(true);
					connect.setDoInput(true);
					OutputStream os = connect.getOutputStream();
					BufferedWriter write = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
					String requestBody = URLEncoder.encode("userID", "UTF-8") + "=" + URLEncoder.encode(temp.getUserID(), "UTF-8");
					write.write(requestBody);
					write.flush();
					write.close();
					os.close();
					connect.connect();
					InputStream in = new BufferedInputStream(connect.getInputStream());
					BufferedReader b = new BufferedReader(new InputStreamReader(in));
					String read;
					if ((read = b.readLine()) != null) {
						if (read.equals("bad")){
							temp.setUserID2("ERRORafterPost979");
						}
					}
					done = true;
				} catch (Exception e) {
					e.printStackTrace();
				}
			});

		}
		public void doUserNameCheck(User temp){

			exec.execute( ()-> {
				try {
					URL link = new URL("http://68.198.11.61:8089/testCheckUsrName/PerformCheck");
					HttpURLConnection connect = (HttpURLConnection) link.openConnection();
					connect.setRequestMethod("POST");
					connect.setDoOutput(true);
					connect.setDoInput(true);
					OutputStream os = connect.getOutputStream();
					BufferedWriter write = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
					String requestBody = URLEncoder.encode("userID", "UTF-8") + "=" + URLEncoder.encode(temp.getUserName(), "UTF-8");
					write.write(requestBody);
					write.flush();
					write.close();
					os.close();
					connect.connect();
					InputStream in = new BufferedInputStream(connect.getInputStream());
					BufferedReader b = new BufferedReader(new InputStreamReader(in));
					String x;
					if ((x = b.readLine()) != null) {
						//System.out.println(x);
						if (x.equals("bad")){
							temp.setUserName("ERRORafterPost979");

						}
						// System.out.println(temp.getUserName());

					}
					else{
						System.out.println("good");
					}
					done = true;
				} catch (Exception e) {
					e.printStackTrace();
				}

			});



		}
		public boolean checkUserNameUnique(User temp){

			boolean t = true;
			done = false;
			doUserNameCheck(temp);

			while(done == false){
				try {   // counter = 0;
					Thread.sleep(1000);
				}
				catch(Exception e){
					e.printStackTrace();
					System.exit(0);
				}
			}

			if(temp.getUserName().equals("ERRORafterPost979")) {
				System.out.println("name isn't valid");
				return false;
			}
			return t;
		}
		public boolean checkUserIDUnique(User temp){
			boolean t = true;
			done = false;
			int i = 0;
			doUserIDCheck(temp);
			while(done == false){
				try {   // counter = 0;
					Thread.sleep(1000);
				}
				catch(Exception e){
					e.printStackTrace();
					System.exit(0);
				}
			}
			if(temp.getUserID().equals("ERRORafterPost979")) {
				System.out.println("id isn't valid");
				return false;
			}
			System.out.println(userID);
			return t;
		}
	}

}
