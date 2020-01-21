package com.infoshareacademy.dao;

import com.infoshareacademy.model.Teacher;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Stateless
public class TeacherDao {

  @PersistenceContext
  private EntityManager entityManager;

  public String save(Teacher t) {
    entityManager.persist(t);
    return t.getPesel();
  }

  public void delete(String pesel) {
    final Teacher t = entityManager.find(Teacher.class, pesel);
    if (t != null) {
      entityManager.remove(t);
    }
  }

  public List<Teacher> teachersList() {

    return entityManager.createNamedQuery("Teacher.findAll")
      .getResultList();

  }

}
