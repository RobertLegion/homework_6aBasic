package com.infoshareacademy.web;

import javax.inject.Inject;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

import com.infoshareacademy.dao.CourseDao;
import com.infoshareacademy.dao.TeacherDao;
import com.infoshareacademy.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@WebServlet(urlPatterns = "/teacher")
@Transactional
public class TeacherServlet extends HttpServlet {

  private Logger LOG = LoggerFactory.getLogger(TeacherServlet.class);

  @Inject
  private TeacherDao teacherDao;

  @Inject
  private CourseDao courseDao;

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp)
    throws IOException {

    final String action = req.getParameter("action");
    LOG.info("Requested action: {}", action);
    if (action == null || action.isEmpty()) {
      resp.getWriter().write("Empty action parameter.");
      return;
    }

    switch (action) {
      case "list":
        listTeacher(req, resp);
        break;
      case "add":
        addTeacher(req, resp);
        break;
      case "delete":
        deleteTeacher(req, resp);
        break;
      default:
        resp.getWriter().write("Unknown action.");
        break;
    }
  }

  private void addTeacher(HttpServletRequest req, HttpServletResponse resp)
    throws IOException {

    final Teacher t = new Teacher();
    t.setName(req.getParameter("name"));
    t.setSurname(req.getParameter("surname"));
    t.setPesel(req.getParameter("pesel"));

    String courseName = req.getParameter("courseName");
    Course course = courseDao.findByName(courseName);
    t.setCourses(Arrays.asList(course));

    teacherDao.save(t);
    LOG.info("Saved a new Teacher object: {}", t);

    // Return all persisted objects
    listTeacher(req, resp);
  }

  private void deleteTeacher(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    final String pesel = req.getParameter("pesel");
    LOG.info("Removing Teacher with pesel = {}", pesel);

    teacherDao.delete(pesel);

    // Return all persisted objects
    listTeacher(req, resp);
  }

  private void listTeacher(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    final List<Teacher> result = teacherDao.teachersList();
    LOG.info("Found {} teachers", result.size());
    for (Teacher t : result) {
      resp.getWriter().write(t.toString() + "\n");
    }
  }
}
