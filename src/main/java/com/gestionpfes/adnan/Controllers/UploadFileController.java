package com.gestionpfes.adnan.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.gestionpfes.adnan.services.UploadFileService;

@RestController
@RequestMapping("/file")
public class UploadFileController {

    @Autowired
   private UploadFileService uploadFileService;


    @PostMapping("/upload")
    public ResponseEntity<Object> uploadFile(@RequestParam Long id,@RequestParam String pathType 
    ,@RequestParam MultipartFile file){

      String fileName =  uploadFileService.storeFile(uploadFileService.convertMultiPartFile(file), id, pathType);

     return ResponseEntity.ok(fileName);
    }


    
}
