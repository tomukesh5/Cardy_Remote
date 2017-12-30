package com.cardyapp.Models;

import com.google.gson.Gson;

import java.io.Serializable;

/**
 * Created by Priyanka on 12/28/2017.
 */

public class SocialUserData implements Serializable {

    private String firstname;
    private String lastname;
    private String fullname;
    private String mobileno;
    private String dob; //(1997-10-05)
    private String gender; //( 0 =male, 1= female)
    private String personalemail;
    private String officialemail;
    private String curcompany;
    private String prevcompany;
    private String fbuserid;
    private String fbprofilelink;
    private String googleuserid;
    private String googleprofilelink;
    private String linkedinuserid;
    private String linkedinprofilelink;
    private String timeofavail; //(13:38 );
    private String qualification;
    private String biography;

    public SocialUserData() {

    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getMobileno() {
        return mobileno;
    }

    public void setMobileno(String mobileno) {
        this.mobileno = mobileno;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPersonalemail() {
        return personalemail;
    }

    public void setPersonalemail(String personalemail) {
        this.personalemail = personalemail;
    }

    public String getOfficialemail() {
        return officialemail;
    }

    public void setOfficialemail(String officialemail) {
        this.officialemail = officialemail;
    }

    public String getCurcompany() {
        return curcompany;
    }

    public void setCurcompany(String curcompany) {
        this.curcompany = curcompany;
    }

    public String getPrevcompany() {
        return prevcompany;
    }

    public void setPrevcompany(String prevcompany) {
        this.prevcompany = prevcompany;
    }

    public String getFbuserid() {
        return fbuserid;
    }

    public void setFbuserid(String fbuserid) {
        this.fbuserid = fbuserid;
    }

    public String getFbprofilelink() {
        return fbprofilelink;
    }

    public void setFbprofilelink(String fbprofilelink) {
        this.fbprofilelink = fbprofilelink;
    }

    public String getGoogleuserid() {
        return googleuserid;
    }

    public void setGoogleuserid(String googleuserid) {
        this.googleuserid = googleuserid;
    }

    public String getGoogleprofilelink() {
        return googleprofilelink;
    }

    public void setGoogleprofilelink(String googleprofilelink) {
        this.googleprofilelink = googleprofilelink;
    }

    public String getLinkedinuserid() {
        return linkedinuserid;
    }

    public void setLinkedinuserid(String linkedinuserid) {
        this.linkedinuserid = linkedinuserid;
    }

    public String getLinkedinprofilelink() {
        return linkedinprofilelink;
    }

    public void setLinkedinprofilelink(String linkedinprofilelink) {
        this.linkedinprofilelink = linkedinprofilelink;
    }

    public String getTimeofavail() {
        return timeofavail;
    }

    public void setTimeofavail(String timeofavail) {
        this.timeofavail = timeofavail;
    }

    public String getQualification() {
        return qualification;
    }

    public void setQualification(String qualification) {
        this.qualification = qualification;
    }

    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
