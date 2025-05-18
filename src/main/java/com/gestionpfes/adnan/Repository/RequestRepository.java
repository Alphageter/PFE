package com.gestionpfes.adnan.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;



import com.gestionpfes.adnan.models.Request;


public interface RequestRepository extends JpaRepository<Request, Long> {
    List<Request> findAll();
   
    List<Request> findByStatus(String status);
    
    Optional<Request> findById(Long id);

    List<Request> findBySubject(String subject);

    List<Request> findBySeen(boolean seen);
    
    List<Request> findByUserGeterId(Long  UserGeterId);

    List<Request> findByUserSenderId(Long  UserSenderId);

    List<Request> findByUserGeterIdOrderByIdDesc(Long  UserGeterId);

    List<Request> findByUserGeterIdAndSeenOrderByIdDesc(Long  UserGeterId, boolean seen);

    List<Request> findByUserSenderIdAndStatus(Long  UserSenderId, String status);


}
