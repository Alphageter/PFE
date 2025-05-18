package com.gestionpfes.adnan.Controllers.PostManTester;


// import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gestionpfes.adnan.models.EtudiantinGroupe;
import com.gestionpfes.adnan.services.EtudiantinGroupeService;

@RestController
@RequestMapping("/api/etudiant-in-groupe")
public class EtudiantinGroupeRestController {

    @Autowired
    private EtudiantinGroupeService etudiantinGroupeService;

    // Create operation
    @PostMapping("/")
    public ResponseEntity<EtudiantinGroupe> createEtudiantinGroupe(@RequestBody EtudiantinGroupe etudiantinGroupe) {
        EtudiantinGroupe createdEtudiantinGroupe = etudiantinGroupeService.createetudiantinGroupe(etudiantinGroupe);
        return new ResponseEntity<>(createdEtudiantinGroupe, HttpStatus.CREATED);
    }

    // Read operation - retrieve all etudiant-in-groupe records
    @GetMapping("/")
    public ResponseEntity<List<EtudiantinGroupe>> getAllEtudiantinGroupes() {
        List<EtudiantinGroupe> etudiantinGroupes = etudiantinGroupeService.getAlletudiantinGroupes();
        return new ResponseEntity<>(etudiantinGroupes, HttpStatus.OK);
    }

    // Read operation - retrieve etudiant-in-groupe records by groupe ID
    @GetMapping("/by-groupe/{groupeId}")
    public ResponseEntity<List<EtudiantinGroupe>> getEtudiantinGroupesByGroupeId(@PathVariable Long groupeId) {
        List<EtudiantinGroupe> etudiantinGroupes = etudiantinGroupeService.getetudiantinGroupesByGroupeID(groupeId);
        return new ResponseEntity<>(etudiantinGroupes, HttpStatus.OK);
    }

    // Update operation
    @PutMapping("/{id}")
    public ResponseEntity<EtudiantinGroupe> updateEtudiantinGroupe(@PathVariable Long id, @RequestBody EtudiantinGroupe etudiantinGroupe) {
        EtudiantinGroupe updatedEtudiantinGroupe = etudiantinGroupeService.updateetudiantinGroupe(id, etudiantinGroupe);
        return new ResponseEntity<>(updatedEtudiantinGroupe, HttpStatus.OK);
    }

    // Delete operation
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEtudiantinGroupe(@PathVariable Long id) {
        etudiantinGroupeService.deleteetudiantinGroupeById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
     // Delete operation
    //  @DeleteMapping("/{id}")
    //  public ResponseEntity<Void> deleteEtudiantinGroupeBygroupID(@PathVariable Long id) {
    //     List<EtudiantinGroupe> etudiantsdelete = new ArrayList<>();
    //     etudiantsdelete = etudiantinGroupeService.getetudiantinGroupesByGroupeID(groupeid);
    //      etudiantinGroupeService.deleteetudiantinGroupeById(id);
    //      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    //  }

}