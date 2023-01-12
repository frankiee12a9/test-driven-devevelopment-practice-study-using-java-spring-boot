package com.mvc.tdd.entity;

import javax.persistence.*;

@Entity
@Table(name = "science_grade")
public class ScienceGrade implements Grade {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int id;

    @Column(name="student_id")
    private int studentId;

    @Column(name="grade")
    private double grade;

    public ScienceGrade() {
    }

    public ScienceGrade(double grade) {
        this.grade = grade;
    }

    public ScienceGrade(double grade, int studentId) {
        this.grade = grade;
        this.studentId = studentId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    @Override
    public double getGrade() {
        return grade;
    }

    public void setGrade(double grade) {
        this.grade = grade;
    }
}
