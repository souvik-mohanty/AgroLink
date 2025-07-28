package com.agrolink.admin.service;

import com.agrolink.admin.model.Complaint;
import com.agrolink.admin.model.User;
import com.agrolink.admin.repository.ComplaintRepository;
import com.agrolink.admin.repository.ListingRepository;
import com.agrolink.admin.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;
    private final ListingRepository listingRepository;
    private final ComplaintRepository complaintRepository;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public void suspendUser(String id) {
        userRepository.findById(id).ifPresent(user -> {
            user.setSuspended(true);
            userRepository.save(user);
        });
    }

    public void approveListing(String listingId) {
        listingRepository.findById(listingId).ifPresent(listing -> {
            listing.setApproved(true);
            listingRepository.save(listing);
        });
    }

    public List<Complaint> getComplaints() {
        return complaintRepository.findAll();
    }

    public void resolveComplaint(String id) {
        complaintRepository.findById(id).ifPresent(complaint -> {
            complaint.setResolved(true);
            complaintRepository.save(complaint);
        });
    }
}

