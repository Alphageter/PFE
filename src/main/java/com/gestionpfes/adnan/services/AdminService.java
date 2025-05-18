package com.gestionpfes.adnan.services;


import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


import com.gestionpfes.adnan.Repository.AdminRepository;
import com.gestionpfes.adnan.models.Admin;



@Service
public class AdminService {

    @Autowired
    private AdminRepository adminRepository;
     
    //get methodes AdminService

    public Admin getAdminById(long id) {
        return adminRepository.findById(id);
    }
    public List<Admin> getAdminByName(String name) {
        return adminRepository.findByName(name);
    }
    public List<Admin> getAdminByLastname(String lastname) {
        return adminRepository.findByLastname(lastname);
    }
    public Admin getAdminByEmail(String email) {
        return adminRepository.findByEmail(email);
    }


    public List<Admin> getAllAdmins() {
        return adminRepository.findAll();
    }
    public List<Admin> getAllAdminsSortedByName() {
        
        return adminRepository.findAll(Sort.by("name"));
    }

    public List<Admin> getAdminsByRole(String role) {
        return adminRepository.findByRole(role);
    }

    public List<Admin> getAdminsByImage(String image) {
        return adminRepository.findByImage(image);
    }


    


    //update methodes AdminService

    public Admin updateAdmin(long id, Admin updatedAdmin) {
        Admin admin = adminRepository.findById(id);
        if (admin == null) {
            // Handle error: User with given ID not found
            return null;
        }
        
        // Update properties of User with values from updatedUser
        admin.setName(updatedAdmin.getName());
        admin.setLastname(updatedAdmin.getLastname());
        admin.setEmail(updatedAdmin.getEmail());
        admin.setPassword(updatedAdmin.getPassword());
        admin.setValidation(updatedAdmin.isValidation());
        admin.setImage(updatedAdmin.getImage());
        
        return adminRepository.save(admin);
    }

    public Admin updateAdminInfo(long id, Admin updatedAdmin) {
        Admin admin = adminRepository.findById(id);
        if (admin == null) {
            // Handle error: User with given ID not found
            return null;
        }
        
        // Update properties of User with values from updatedUser
        admin.setName(updatedAdmin.getName());
        admin.setLastname(updatedAdmin.getLastname());
        admin.setEmail(updatedAdmin.getEmail());
        
        
        return adminRepository.save(admin);
    }

    public Admin updateAdminPassword(long id, String updatePassword) {
        Admin admin = adminRepository.findById(id);
        if (admin == null) {
            // Handle error: User with given ID not found
            return null;
        }
        
        admin.setPassword(updatePassword);
        
        
        return adminRepository.save(admin);
    }

    public Admin updateAdminRole(long id, String role) {
        Admin admin = adminRepository.findById(id);
        admin.setRole(role);
        return adminRepository.save(admin);
    }

    public Admin updateAdminImage(long id, String image) {
        Admin admin = adminRepository.findById(id);
        admin.setImage(image);
        return adminRepository.save(admin);
    }


  
    //delete method adminservices

    public boolean deleteAdmin(long id) {
        if (adminRepository.existsById(id)) {
            adminRepository.deleteById(id);
            return true;
        } else {
            // Handle error: Etudiant with given ID not found
            return false;
        }
    }

    //create new Admin

    public void save(Admin admin){
        adminRepository.save(admin);
    }
    //check if existe
    public boolean ExisteById(long id){
        return adminRepository.existsById(id);
    }
    public boolean ExisteByEmail(String email){
        return adminRepository.existsByEmail(email);
    }


     
    
}


