package com.stylezone.demo.models;

public class Picture {
    int idPictures;
    String pictureName;



    public Picture(){

    }

    public Picture(int idPictures, String pictureName) {
        this.idPictures = idPictures;
        this.pictureName = pictureName;
    }

    public int getIdPictures() {
        return idPictures;
    }

    public void setIdPictures(int idPictures) {
        this.idPictures = idPictures;
    }

    public String getPictureName() {
        return pictureName;
    }

    public void setPictureName(String pictureName) {
        this.pictureName = pictureName;
    }
}
