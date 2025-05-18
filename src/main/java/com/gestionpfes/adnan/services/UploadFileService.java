package com.gestionpfes.adnan.services;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;


import com.gestionpfes.adnan.handleErrores.FileStorageException;
import com.gestionpfes.adnan.models.Admin;



@Service
public class UploadFileService {

    Logger log = LoggerFactory.getLogger(UploadFileService.class);

    private Path fileStorageLocation;

    // @Value("${file.upload.base.path}")
    private  final String basePath = "C :: \\ Users \\ Dell\\ Documents \\ me \\ VScodeProjects \\ adnan \\"
    +" src \\ main \\ resources \\ static \\ images \\ " ;

    @Autowired
    private AdminService adminService ;

    public String storeFile(File file ,long id , String pathType){

        //create Upload Path
        this.fileStorageLocation = Paths.get(basePath+pathType).toAbsolutePath().normalize();
        try{
           Files.createDirectories(this.fileStorageLocation);
            
        }catch(Exception ex){
            throw new FileStorageException("Could not Create the directory where the upload file will be stored");
        }

        //Normalize file name
        //long rondomNumber = new Rondom().nextLong();
        String fileName = StringUtils.cleanPath(id + "-" + file.getName());

        try{
            //Check if file's name contains invalid charcters
            if(fileName.contains("..")){
                throw new FileStorageException("Sorry ! file name contain invalid path sequence " + fileName);
            }

            //Copy file to the target location 
            Path targetLocation = this.fileStorageLocation.resolve(fileName);
            InputStream targetStream = new FileInputStream(file);
            Files.copy(targetStream, targetLocation, StandardCopyOption.REPLACE_EXISTING);
            updateImagePath(id ,  pathType ,pathType + "/" +fileName);

            return fileName;
        }catch(IOException ex){
            throw new FileStorageException("Could not store file "+fileName+". please try again !");
        }

    }

    private void updateImagePath(Long adminid , String pathType ,String imagePath){

        Admin admin = adminService.getAdminById(adminid);
        admin.setImage(imagePath);
        adminService.updateAdminImage(adminid, imagePath);


    }

public File convertMultiPartFile(final MultipartFile multipartFile){
 final File file = new File(multipartFile.getOriginalFilename());
 try(final FileOutputStream outputStream = new FileOutputStream(file)){
    outputStream.write(multipartFile.getBytes());
 }catch(final IOException ex){
    log.error("Error converting the multi_part file to file",ex.getMessage());
 }
    return file;
}

    
}
