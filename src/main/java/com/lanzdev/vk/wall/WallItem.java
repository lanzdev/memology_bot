package com.lanzdev.vk.wall;

import java.util.ArrayList;
import java.util.List;

public class WallItem {


    private Long id;
    private Integer from_id;
    private Long date;
    private String text;
    private List<Photo> photos;

    public WallItem( ) {

        photos = new ArrayList<>();
    }

    public Long getId( ) {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getFrom_id( ) {
        return from_id;
    }

    public void setFrom_id(Integer from_id) {
        this.from_id = from_id;
    }

    public Long getDate( ) {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }

    public String getText( ) {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }


    public List<Photo> getPhotos( ) {
        return photos;
    }

    public void addPhoto(Photo photo) {
        photos.add(photo);
    }


    @Override
    public String toString( ) {

        StringBuilder sb = new StringBuilder();

        sb.append("{");
        sb.append("\n");
        sb.append(String.format("%4s%s: %d,", "", "\"id\"", id));
        sb.append("\n");
        sb.append(String.format("%4s%s: %d,", "", "\"from_id\"", from_id));
        sb.append("\n");
        sb.append(String.format("%4s%s: %d,", "", "\"date\"", date));
        sb.append("\n");
        sb.append(String.format("%4s%s: %s,", "", "\"text\"", "\"" + text + "\""));
        sb.append("\n");
        sb.append(String.format("%4s%s: [", "", "\"photos\""));
        sb.append("\n");
        photos.stream()
                .forEach((photo) ->
                {
                    sb.append(String.format("%6s%s", "", photo));
                    sb.append(String.format("%6s%s", "", "},"));
                    sb.append("\n");
                });
        sb.deleteCharAt(sb.length() - 2);
        sb.append(String.format("%4s%s", "", "]"));
        sb.append("\n");
        sb.append("}");
        return sb.toString();
    }


}