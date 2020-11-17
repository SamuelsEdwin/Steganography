//This program is a modification of the codelab friendly chat tutorial
package com.google.firebase.codelab.littleAlcove;

public class AlcoveMessage {

    private String id;
    private String text;
    private String name;
    private String photoUrl;
    private String imageUrl;
    private String audioUrl;
    private String hiddenText;

    public AlcoveMessage() {
    }

    public AlcoveMessage(String text, String name, String photoUrl, String imageUrl) {
        this.text = text;
        this.name = name;
        this.photoUrl = photoUrl;
        this.imageUrl = imageUrl;
    }

    public AlcoveMessage(String text, String name, String photoUrl, String imageUrl, String audioUrl) {
        this.text = text;
        this.name = name;
        this.photoUrl = photoUrl;
        this.imageUrl = imageUrl;
        this.audioUrl = audioUrl;
    }
    public AlcoveMessage(String text, String name, String photoUrl, String imageUrl, String audioUrl, String hiddenText) {
        this.text = text;
        this.name = name;
        this.photoUrl = photoUrl;
        this.imageUrl = imageUrl;
        this.audioUrl = audioUrl;
        this.hiddenText = hiddenText;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public String getText() {
        return text;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }
    public String getAudioUrl() {
        return audioUrl ;
    }
    public void setAudioUrl(String audioUrl) {
        this.audioUrl = audioUrl;
    }


    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
