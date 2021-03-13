package com.example.crisol_Jaen.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Book implements Parcelable {
    String id, title, summary, authorId, image;
    double price;

    public Book(){}


    public Book(String id, String title, String summary, String authorId, String image, double price) {
        this.id = id;
        this.title = title;
        this.summary = summary;
        this.authorId = authorId;
        this.image = image;
        this.price = price;
    }

    protected Book(Parcel in) {
        id = in.readString();
        title = in.readString();
        summary = in.readString();
        authorId = in.readString();
        image = in.readString();
        price = in.readDouble();
    }

    public static final Creator<Book> CREATOR = new Creator<Book>() {
        @Override
        public Book createFromParcel(Parcel in) {
            return new Book(in);
        }

        @Override
        public Book[] newArray(int size) {
            return new Book[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getAuthorId() {
        return authorId;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String authorId) {
        this.image = image;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(title);
        dest.writeString(summary);
        dest.writeString(authorId);
        dest.writeString(image);
        dest.writeDouble(price);
    }
}
