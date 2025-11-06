package com.example.movieticketapp.Model;

import java.security.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class FilmModel {

    private String PrimaryImage;
    private String name;
    private Timestamp movieBeginDate;
    private Timestamp movieEndDate;
    private String id;
    private String BackgroundImage;
    private float vote;
    private String genre;
    private String description;
    private String PosterImage;
    private String durationTime;

    public FilmModel(){

    }

    private List<String> trailer=new ArrayList<>();
    public List<String> getTrailer() {
        return trailer;
    }

    public void setTrailer(List<String> trailer) {
        this.trailer = trailer;
    }

    public FilmModel(String id, String PrimaryImage, String name, String BackgroundImage, String PosterImage, float vote, String genre, String description, String durationTime, Timestamp movieBeginDate, Timestamp movieEndDate, List<String> trailer){
        this.id = id;
        this.PrimaryImage = PrimaryImage;
        this.name = name;
        this.BackgroundImage = BackgroundImage;
        this.vote = vote;
        this.genre = genre;
        this.description = description;
        this.PosterImage = PosterImage;
        this.durationTime = durationTime;
        this.movieBeginDate = movieBeginDate;
        this.trailer=trailer;
        this.movieEndDate= movieEndDate;
    }

    public Timestamp getMovieBeginDate(){
        return movieBeginDate;
    }


}
