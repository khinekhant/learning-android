package com.khinekhant.crimereporter;

import java.util.Date;
import java.util.UUID;

/**
 * Created by ${KhineKhant} on 0026,08/26/16.
 */
public class Crime {
    private String mTitle;
    private UUID mId;
    private Date mDate;
    private boolean mIsCrimeSolved;
    private String mSuspect;
    private Long mContactid;



    //Constructors
    public Crime() {
        this(UUID.randomUUID());
       // mId = UUID.randomUUID();
        //mDate=new Date();

    }
    public Crime(UUID uuid) {
        mId=uuid;
        mDate=new Date();
    }


//Getters
    public UUID getId() {
        return mId;
    }
    public String getTitle() {
        return mTitle;
    }
    public Date getDate() {
        return mDate;
    }
    public boolean isCrimeSolved() {
        return mIsCrimeSolved;
    }
    public String getSuspect() {
        return mSuspect;
    }
    public Long getContactid() {
        return mContactid;
    }


//Setters
    public void setCrimeSolved(boolean crimeSolved) {
        mIsCrimeSolved = crimeSolved;
    }
    public void setTitle(String title) {
        mTitle = title;
    }
    public void setDate(Date date) {
        mDate = date;
    }
    public void setSuspect(String suspect) {
        mSuspect = suspect;
    }
    public void setContactid(Long contactid) {
        mContactid = contactid;
    }

    public String getPhotoFilename(){
        return "IMG_"+getId().toString()+".jpg";
    }

}
