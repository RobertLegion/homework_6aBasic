package com.infoshareacademy.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;


@NamedQueries({
  @NamedQuery(name = "Teacher.findAll", query = "SELECT t FROM Teacher t"),
}
)

@Entity
@Table(name = "TEACHER")
public class Teacher {

  @Id
  @Column(name = "pesel")
  private String pesel;

  @Column(name = "name")
  @NotNull
  private String name;

  @Column(name = "surname")
  @NotNull
  private String surname;

  @ManyToMany
  @JoinTable(name = "TEACHERS_TO_COURSES",
    joinColumns = @JoinColumn(name = "teacher_pesel", referencedColumnName = "pesel"), // TEACHERS
    inverseJoinColumns = @JoinColumn(name = "course_name", referencedColumnName = "name"))

// COURSES
  private List<Course> courses;

  public void setCourses(List<Course> courses) {
    this.courses = courses;
  }

  public String getPesel() {
    return pesel;
  }

  public void setPesel(String pesel) {
    this.pesel = pesel;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getSurname() {
    return surname;
  }

  public void setSurname(String surname) {
    this.surname = surname;
  }

}
