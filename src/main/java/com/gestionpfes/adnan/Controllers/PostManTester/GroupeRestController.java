package com.gestionpfes.adnan.Controllers.PostManTester;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gestionpfes.adnan.models.Groupe;
import com.gestionpfes.adnan.services.GroupeService;

@RestController
@RequestMapping("/groupes")
public class GroupeRestController {
    
    @Autowired
    private GroupeService groupeService;
    
    @GetMapping("/")
    public List<Groupe> listAllGroupes() {
        return groupeService.findAll();
    }
    
    @GetMapping("/{id}")
    public Optional<Groupe> getGroupeById(@PathVariable Long id) {
        return groupeService.findById(id);
    }
    
    @GetMapping("/bySubject/{subjectId}")
    public List<Groupe> getGroupesBySubjectId(@PathVariable Long subjectId) {
        return groupeService.findBySubjectID(subjectId);
    }
    
    @PostMapping("/")
    public Groupe createGroupe(@RequestBody Groupe groupe) {
        return groupeService.createGroupe(groupe);
    }
    
    @PutMapping("/{id}")
    public Groupe updateGroupe(@PathVariable Long id, @RequestBody Groupe groupe) {
        groupe.setId(id);
        return groupeService.updateGroupe(id,groupe);
    }
    
    @DeleteMapping("/{id}")
    public void deleteGroupe(@PathVariable Long id) {
        groupeService.deleteGroupe(id);
    }
}