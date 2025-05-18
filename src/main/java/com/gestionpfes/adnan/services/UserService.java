package com.gestionpfes.adnan.services;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.gestionpfes.adnan.Repository.UserRepository;
import com.gestionpfes.adnan.models.User;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    
    //get methods userservices___________
    
    
    public User getUserById(long id) {
        return userRepository.findById(id);
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    public User getUserByPassword(String password) {
        return userRepository.findByPassword(password);
    }

    public User getUserByEmailAndPassword(String email, String password) {
        return userRepository.findByEmailAndPassword(email, password);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    public List<User> getAllUsersSortedByName() {
        return userRepository.findAll(Sort.by("name"));
    }
    public List<User> getAllUsersSortedByRole() {
        return userRepository.findAllByOrderByRoleAsc();
    }

    public List<User> getUsersByName(String name) {
        return userRepository.findByName(name);
    }
    

    public List<User> getUsersByLastname(String lastname) {
        return userRepository.findByLastname(lastname);
    }

    public List<User> getUsersByValidation(boolean validation) {
        return userRepository.findByValidation(validation);
    }

    public List<User> getUserByImage(String image) {
        return userRepository.findByImage(image);   
   }


   

        
    //delete methods userservices___________

   public boolean deleteUser(long id) {
      if (userRepository.existsById(id)) {
        userRepository.deleteById(id);
        return true;
    } else {
        // Handle error: Etudiant with given ID not found
        return false;
    }
}

public long countUsers() {
    return userRepository.count();
}


//update methods userservices___________

public User updateUser(long id, User updatedUser) {
    User user = userRepository.findById(id);
    if (user == null) {
        // Handle error: User with given ID not found
        return null;
    }
    
    // Update properties of User with values from updatedUser
    user.setName(updatedUser.getName());
    user.setLastname(updatedUser.getLastname());
    user.setEmail(updatedUser.getEmail());
    user.setPassword(updatedUser.getPassword());
    user.setValidation(updatedUser.isValidation());
    user.setImage(updatedUser.getImage());
    
    return userRepository.save(user);
}

public User updateUserInfo(long id, User updatedUser) {
    User user = userRepository.findById(id);
    if (user == null) {
        // Handle error: User with given ID not found
        return null;
    }
    
    // Update properties of User with values from updatedUser
    user.setName(updatedUser.getName());
    user.setLastname(updatedUser.getLastname());
    user.setEmail(updatedUser.getEmail());
    
    return userRepository.save(user);
}

public User updateUserPassword(long id, String password) {
    User user = userRepository.findById(id);
    if (user == null) {
        // Handle error: User with given ID not found
        return null;
    }
    
    // Update properties of User with values from updatedUser
    
    user.setPassword(password);
    
    return userRepository.save(user);
}

public User updateUserValidation(long id, boolean validation) {
    User user = userRepository.findById(id);
    if (user == null) {
        // Handle error: User with given ID not found
        return null;
    }
    
    user.setValidation(validation);
    return userRepository.save(user);
}

public User updateUserImage(long id, String image) {
    User user = userRepository.findById(id);
    user.setImage(image);
    return userRepository.save(user);
}



 //create new User

 public void saveUser(User user){
    userRepository.save(user);
}
 





   


   
}

