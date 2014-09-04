package com.kidgeniushq.findatrainger.models;

public class Trainer {
String fullName, youtubeLink,birthDate,email,aboutMe;
int phoneNumber;
double lat,lng;
byte[] image;

public Trainer(){
	
}
public void setName(String text){
	this.fullName=text;
}
public String getName(){
	return fullName;
}
public void setYoutubeLink(String text){
	this.youtubeLink=text;
}
public String getYoutubeLink(){
	return youtubeLink;
}
public void setBirthDate(String text){
	this.birthDate=text;
}
public String getBirthDate(){
	return birthDate;
}
public void setLat(int text){
	this.lat=text;
}
public double getLat(){
	return lat;
}
public void setLng(int text){
	this.lng=text;
}
public double getLng(){
	return lng;
}
public void setEmail(String text){
	this.email=text;
}
public String getEmail(){
	return email;
}
public void setAboutMe(String text){
	this.aboutMe=text;
}
public String getAboutMe(){
	return aboutMe;
}
public void setNumber(int text){
	this.phoneNumber=text;
}
public int getNumber(){
	return phoneNumber;
}
public void setImage(byte[] text){
	this.image=text;
}
public byte[] getImage(){
	return image;
}
}
