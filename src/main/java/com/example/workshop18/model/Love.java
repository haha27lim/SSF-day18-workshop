package com.example.workshop18.model;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URLDecoder;
import java.util.Random;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;


public class Love implements Serializable {

    private String firstName;
    private String secondName;
    private Integer percentage;
    private String result;
    private String id;
    
    public Love() {
        this.id = generateId(8);
    }

    public Love(String firstName, String secondName) {
        this.id = generateId(8);
        this.firstName =firstName;
        this.secondName = secondName;
    }

    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public String getSecondName() {
        return secondName;
    }
    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }
    public Integer getPercentage() {
        return percentage;
    }
    public void setPercentage(Integer percentage) {
        this.percentage = percentage;
    }
    public String getResult() {
        return result;
    }
    public void setResult(String result) {
        this.result = result;
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    private synchronized String generateId(int numChars) {
        Random r = new Random();
        StringBuilder strBuilder = new StringBuilder();
        while (strBuilder.length() < numChars) {
            strBuilder.append(Integer.toHexString(r.nextInt()));
        }
        return strBuilder.toString().substring(0, numChars);
    }

    public static Love create(String json) throws IOException {
        Love l = new Love();
        try (InputStream is = new ByteArrayInputStream(json.getBytes())) {
            JsonReader r = Json.createReader(is);
            JsonObject o = r.readObject();
            // remove encoding chars from API
            String person1Name = URLDecoder.decode(o.getString("fname"), "UTF-8");
            String person2Name = URLDecoder.decode(o.getString("sname"), "UTF-8");

            l.setFirstName(person1Name);
            l.setSecondName(person2Name);
            l.setPercentage(Integer.parseInt(o.getString("percentage")));
            l.setResult(o.getString("result"));
        }
        return l;

    }
}
    
