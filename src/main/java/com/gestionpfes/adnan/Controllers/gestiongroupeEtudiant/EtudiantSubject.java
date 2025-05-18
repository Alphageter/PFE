package com.gestionpfes.adnan.Controllers.gestiongroupeEtudiant;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.gestionpfes.adnan.models.Subject;
import com.gestionpfes.adnan.services.SubjectService;

import jakarta.servlet.http.HttpServletResponse;


@Controller
public class EtudiantSubject {
    

@Autowired
private SubjectService subjectService;



    @GetMapping("/Etudiant/download/subject/{subjectId}")
 public void viewPdf(@PathVariable Long subjectId, HttpServletResponse response) throws IOException {
        Subject subject = subjectService.getSubjectById(subjectId);
        File pdfFile = new File(subject.getPdfFile()); 
          // Assuming that the pdfFile property of Subject entity contains the file path of the PDF
          response.setContentType("application/pdf");
          response.setHeader("Content-disposition", "inline; filename=" + pdfFile.getName());
            FileInputStream inputStream = new FileInputStream(pdfFile);
           IOUtils.copy(inputStream, response.getOutputStream());
         response.flushBuffer();
             }

}
