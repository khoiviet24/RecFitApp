package khoiviet24.recfit;

/**
 * Created by khoiviet24 on 1/8/2016.
 */
public class InviteList {

    private String mName;
    private String mPic;

    public InviteList(String name, String pic){
        mName = name;
        mPic = pic;
    }

    public InviteList(String name){
        mName = name;
        mPic = null;
    }

    public String getName(){
        return mName;
    }

    public void setName(String name){
        this.mName = name;
    }

    public void setPic(String pic){
        this.mPic = pic;
    }

    public String getPic(){
        return mPic;
    }
}
