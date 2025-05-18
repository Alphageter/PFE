package com.gestionpfes.adnan.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gestionpfes.adnan.Repository.RequestRepository;
import com.gestionpfes.adnan.models.Request;

@Service
public class RequestService {

    @Autowired
    private final RequestRepository requestRepository;

    public RequestService(RequestRepository requestRepository) {
        this.requestRepository = requestRepository;
    }


    //GET Request method 
     public List<Request> findAll() {
        return requestRepository.findAll();
    }

    public List<Request> findByStatus(String status) {
        return requestRepository.findByStatus(status);
    }

    public Optional<Request> findById(Long id) {
        return requestRepository.findById(id);
    }

    public List<Request> findBySubject(String subject) {
        return requestRepository.findBySubject(subject);
    }


    public List<Request> findBySeen(boolean seen) {
        return requestRepository.findBySeen(seen);
    }

    public List<Request> findByUserGeterId(Long userGeterId) {
        return requestRepository.findByUserGeterId(userGeterId);
    }

    public List<Request> findByUserSenderId(Long userSenderId) {
        return requestRepository.findByUserSenderId(userSenderId);
    }


    public List<Request> findByUserGeterIdOrderByidDesc(Long userGeterId) {
        return requestRepository.findByUserGeterIdOrderByIdDesc(userGeterId);
    }

    
    public List<Request> findByUserGeterIdAndSeenOrderByIdDesc(Long userGeterId , boolean seen) {
        return requestRepository.findByUserGeterIdAndSeenOrderByIdDesc(userGeterId,seen);
    }

    
    public List<Request> findByUserSenderIdAndStatus(Long  UserSenderId, String status) {
        return requestRepository.findByUserSenderIdAndStatus(UserSenderId,status);
    }


    


    //CREATE Request method 

    public Request createRequest(Request request) {
        return requestRepository.save(request);
    }

    //UPDATE Request method 

    public Request updateRequest(Long id, Request updatedRequest) {
        Request existingRequest = requestRepository.findById(id).orElse(null);

        if (existingRequest == null) {
            return null;
        }
        existingRequest.setStatus(updatedRequest.getStatus());
        existingRequest.setSubject(updatedRequest.getSubject());
        existingRequest.setSeen(updatedRequest.isSeen());
        existingRequest.setUserGeterId(updatedRequest.getUserGeterId());
        existingRequest.setUserSenderId(updatedRequest.getUserSenderId());
        

        return requestRepository.save(existingRequest);
    }

    public Request updateRequestSubject(Long id, String subject) {
        Request existingRequest = requestRepository.findById(id).orElse(null);

        if (existingRequest == null) {
            return null;
        }

        existingRequest.setSubject(subject);

        return requestRepository.save(existingRequest);
    }

    public Request updateRequestStatus(Long id, String status) {
        Request existingRequest = requestRepository.findById(id).orElse(null);

        if (existingRequest == null) {
            return null;
        }

        existingRequest.setStatus(status);

        return requestRepository.save(existingRequest);
    }

    public void updateSeen(Long id, boolean seen) {
        Optional<Request> optionalRequest = requestRepository.findById(id);
        if (optionalRequest.isPresent()) {
            Request request = optionalRequest.get();
            request.setSeen(seen);
            requestRepository.save(request);
        }
    }

    public Request updateRequestUserGeterId(Long id, Long userGeterId) {
        Request existingRequest = requestRepository.findById(id).orElse(null);

        if (existingRequest == null) {
            return null;
        }

        existingRequest.setUserGeterId(userGeterId);

        return requestRepository.save(existingRequest);
    }

    public Request updateRequestUserSender(Long id, Long UserSenderId) {
        Request existingRequest = requestRepository.findById(id).orElse(null);

        if (existingRequest == null) {
            return null;
        }

        existingRequest.setUserSenderId(UserSenderId);

        return requestRepository.save(existingRequest);
    }

    
    
    //Delte Request method 
    public void deleteRequestById(Long id) {
        requestRepository.deleteById(id);
    }

   
    
    
    

}