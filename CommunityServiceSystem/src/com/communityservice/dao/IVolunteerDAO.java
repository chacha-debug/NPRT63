package com.communityservice.dao;

import com.communityservice.models.Volunteer;
import java.util.List;

public interface IVolunteerDAO {
    // Basic CRUD Operations
    boolean createVolunteerProfile(Volunteer volunteer);
    Volunteer getVolunteerById(int volunteerId);
    Volunteer getVolunteerByUserId(int userId);
    List<Volunteer> getAllVolunteers();
    boolean updateVolunteerProfile(Volunteer volunteer);
    boolean deleteVolunteerProfile(int volunteerId);

    // Query Methods
    List<Volunteer> getVolunteersBySkills(String skills);
    List<Volunteer> getVolunteersByAvailability(String availability);
}