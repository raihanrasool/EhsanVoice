package com.rasool.ehsanvoice.Models;

import java.io.Serializable;

public class Message implements Serializable {
    private int messageID, catId, oderOfMessage;
    private String speakText, Name, Path;
    private long length, time_added;

    public Message(int messageID, int catId, int oderOfMessage, String speakText, String name, String path, long length, long time_added) {
        this.messageID = messageID;
        this.catId = catId;
        this.oderOfMessage = oderOfMessage;
        this.speakText = speakText;
        Name = name;
        Path = path;
        this.length = length;
        this.time_added = time_added;
    }

    public Message(int catId, int oderOfMessage, String speakText, String name, String path, long length, long time_added) {
        this.catId = catId;
        this.oderOfMessage = oderOfMessage;
        this.speakText = speakText;
        Name = name;
        Path = path;
        this.length = length;
        this.time_added = time_added;
    }

    public Message(String speakText) {
        this.speakText = speakText;
        // this.catId = catId;
    }

    public int getMessageID() {
        return messageID;
    }

    public void setMessageID(int messageID) {
        this.messageID = messageID;
    }

    public int getCatId() {
        return catId;
    }

    public void setCatId(int catId) {
        this.catId = catId;
    }

    public int getOderOfMessage() {
        return oderOfMessage;
    }

    public void setOderOfMessage(int oderOfMessage) {
        this.oderOfMessage = oderOfMessage;
    }

    public String getSpeakText() {
        return speakText;
    }

    public void setSpeakText(String speakText) {
        this.speakText = speakText;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPath() {
        return Path;
    }

    public void setPath(String path) {
        Path = path;
    }

    public long getLength() {
        return length;
    }

    public void setLength(long length) {
        this.length = length;
    }

    public long getTime_added() {
        return time_added;
    }

    public void setTime_added(long time_added) {
        this.time_added = time_added;
    }

    @Override
    public String toString() {
        return "Message{" +
                "messageID=" + messageID +
                ", catId=" + catId +
                ", oderOfMessage=" + oderOfMessage +
                ", speakText='" + speakText + '\'' +
                ", Name='" + Name + '\'' +
                ", Path='" + Path + '\'' +
                ", length=" + length +
                ", time_added=" + time_added +
                '}';
    }
}