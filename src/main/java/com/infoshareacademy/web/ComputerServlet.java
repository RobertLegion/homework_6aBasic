package com.infoshareacademy.web;

import com.infoshareacademy.dao.ComputerDao;
import com.infoshareacademy.model.Computer;
import java.io.IOException;
import java.util.List;
import javax.inject.Inject;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebServlet(urlPatterns = "/computer")
public class ComputerServlet extends HttpServlet {

  private Logger LOG = LoggerFactory.getLogger(ComputerServlet.class);

  @Inject
  private ComputerDao computerDao;

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
      case "findAll":
        findAll(req, resp);
        break;
      case "add":
        addComputer(req, resp);
        break;
      case "delete":
        deleteComputer(req, resp);
        break;
      case "update":
        updateComputer(req, resp);
        break;
      default:
        resp.getWriter().write("Unknown action.");
        break;
    }
  }

  private void updateComputer(HttpServletRequest req, HttpServletResponse resp)
      throws IOException {
    final Long id = Long.parseLong(req.getParameter("id"));
    LOG.info("Updating Computer with id = {}", id);

    final Computer existingComputer = computerDao.findById(id);
    if (existingComputer == null) {
      LOG.info("No Computer found for id = {}, nothing to be updated", id);
    } else {
      existingComputer.setName(req.getParameter("name"));
      existingComputer.setOperatingSystem(req.getParameter("os"));

      computerDao.update(existingComputer);
      LOG.info("Computer object updated: {}", existingComputer);
    }

    // Return all persisted objects
    findAll(req, resp);
  }

  private void addComputer(HttpServletRequest req, HttpServletResponse resp)
      throws IOException {

    final Computer p = new Computer();
    p.setName(req.getParameter("name"));
    p.setOperatingSystem(req.getParameter("os"));

    computerDao.save(p);
    LOG.info("Saved a new Computer object: {}", p);

    // Return all persisted objects
    findAll(req, resp);
  }

  private void deleteComputer(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    final Long id = Long.parseLong(req.getParameter("id"));
    LOG.info("Removing Computer with id = {}", id);

    computerDao.delete(id);

    // Return all persisted objects
    findAll(req, resp);
  }

  private void findAll(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    final List<Computer> result = computerDao.findAll();
    LOG.info("Found {} objects", result.size());
    for (Computer p : result) {
      resp.getWriter().write(p.toString() + "\n");
    }
  }
}

