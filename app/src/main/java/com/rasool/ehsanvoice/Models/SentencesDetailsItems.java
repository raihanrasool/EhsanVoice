package com.rasool.ehsanvoice.Models;

import java.io.Serializable;

public class SentencesDetailsItems implements Serializable {

    int sentenceId;
    String sentenceText;


    public SentencesDetailsItems(String sentenceText) {
        this.sentenceText = sentenceText;
    }

    public SentencesDetailsItems(int sentenceId, String sentenceText) {
        this.sentenceId = sentenceId;
        this.sentenceText = sentenceText;
    }

    public String getSentenceText() {
        return sentenceText;
    }

    public void setSentenceText(String sentenceText) {
        this.sentenceText = sentenceText;
    }

    public int getSentenceId() {
        return sentenceId;
    }
}
