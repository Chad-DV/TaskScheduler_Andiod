package com.example.ppmtoolmobile.dao;

import com.example.ppmtoolmobile.model.Project;
import com.example.ppmtoolmobile.model.User;

import java.util.List;

public interface ProjectAndUserDAO {

    Boolean register(User user);
    Boolean login(User user);
    Boolean isEmailExists(String email);

    Boolean addProject(Project project);
    Boolean editProject(Project project);
    Boolean deleteProjectById(long id);
    Project getProjectById(long projectId);
    List<Project> getAllProjects();
    int getProjectCount();
    List<Project> getUserProjects(int id);

}
