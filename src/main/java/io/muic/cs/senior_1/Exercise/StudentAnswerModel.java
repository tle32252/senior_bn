package io.muic.cs.senior_1.Exercise;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;


public class StudentAnswerModel {

    @Id
    @GeneratedValue
    private Long id;
    private String anschoose;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAnschoose() {
        return anschoose;
    }

    public void setAnschoode(String anschoode) {
        this.anschoose = anschoode;
    }

    @Override
    public String toString() {
        return "StudentAnswerModel{" +
                "id=" + id +
                ", anschoode='" + anschoose + '\'' +
                '}';
    }
}
