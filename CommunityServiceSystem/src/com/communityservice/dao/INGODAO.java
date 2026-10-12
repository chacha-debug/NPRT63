package com.communityservice.dao;

import com.communityservice.models.NGO;
import java.util.List;

public interface INGODAO {
    // Basic CRUD Operations
    boolean createNGOProfile(NGO ngo);
    NGO getNGOById(int ngoId);
    NGO getNGOByUserId(int userId);
    List<NGO> getAllNGOs();
    boolean updateNGOProfile(NGO ngo);
    boolean deleteNGOProfile(int ngoId);

    // NGO Specific Logic
    List<NGO> getPendingVerificationNGOs();
    boolean verifyNGO(int ngoId);
    boolean updateVerificationStatus(int ngoId, boolean verified);
    boolean updateRegistrationDetails(int userId, String operatingRegion, String orgScale, String mission, String focusAreas, String preferredSkills);
}