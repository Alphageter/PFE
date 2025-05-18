package com.gestionpfes.adnan.Repository;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import com.gestionpfes.adnan.models.User;


public interface UserRepository extends JpaRepository<User, Long> {

    
    User findByEmail(String email);
    List<User> findByValidation(boolean validation);
    
    List<User> findAll();
    List<User> findAllByOrderByRoleAsc();
    User findById(long id);
    List<User> findByName(String name);
    List<User> findByLastname(String lastname);
    User findByEmailAndPassword(String email, String password);
    User findByPassword(String password);
    List<User> findByImage(String image);
    List<User> findAll(Sort sort);
    
    
}
