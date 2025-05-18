package com.gestionpfes.adnan.Repository;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import com.gestionpfes.adnan.models.Admin;




public interface AdminRepository  extends JpaRepository<Admin, Long> {

    List<Admin> findByRole(String role);
    List<Admin> findAll();
    List<Admin> findAll(Sort sort);
    Admin findById(long id);
    List<Admin> findByName(String name);
    List<Admin> findByImage(String image);
    List<Admin> findByLastname(String lastname);
    Admin findByEmail(String email);
    Admin findByValidation(String email);
    boolean existsByEmail(String email);
}
