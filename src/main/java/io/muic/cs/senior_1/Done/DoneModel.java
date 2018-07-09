package io.muic.cs.senior_1.Done;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class DoneModel {
    @Id
    @GeneratedValue
    private Long id;
    private String username;
    private Long exerciseid;
    private Integer score;
    private Integer outof;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getExerciseid() {
        return exerciseid;
    }

    public void setExerciseid(Long exerciseid) {
        this.exerciseid = exerciseid;
    }

    //    public String getExercise() {
//        return exerciseid;
//    }
//
//    public void setExercise(String exercise) {
//        this.exercise = exercise;
//    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public Integer getOutof() {
        return outof;
    }

    public void setOutof(Integer outof) {
        this.outof = outof;
    }
}
