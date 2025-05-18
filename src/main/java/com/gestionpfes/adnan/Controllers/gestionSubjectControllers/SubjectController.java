package com.gestionpfes.adnan.Controllers.gestionSubjectControllers;

import java.io.IOException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.gestionpfes.adnan.Repository.SubjectRepository;
import com.gestionpfes.adnan.models.Subject;

@Controller
public class SubjectController {

   @Autowired
   private SubjectRepository subjectRepository ;


    @GetMapping("/download/{subjectId}")
public ResponseEntity<Resource> downloadPDF(@PathVariable Long subjectId) throws IOException {
    // Retrieve the Subject object with the given ID
    Optional<Subject> subjectOptional = subjectRepository.findById(subjectId);
    if (!subjectOptional.isPresent()) {
        // Return a 404 Not Found response if the Subject does not exist
        return ResponseEntity.notFound().build();
    }
    Subject subject = subjectOptional.get();
    
    // Check that the Subject has a PDF file
    if (subject.getPdfFile() == null) {
        // Return a 404 Not Found response if the Subject does not have a PDF file
        return ResponseEntity.notFound().build();
    }
    
    // Load the PDF file as a resource
    Resource pdfResource = new ByteArrayResource(subject.getPdfFile().getBytes());
    
    // Set the response headers to indicate that the response should be treated as a file download
    HttpHeaders headers = new HttpHeaders();
    headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + subject.getName() + ".pdf\"");
    
    // Return the PDF file as a ResponseEntity
    return ResponseEntity.ok()
            .headers(headers)
            .contentType(MediaType.APPLICATION_PDF)
            .body(pdfResource);
}
    
}
