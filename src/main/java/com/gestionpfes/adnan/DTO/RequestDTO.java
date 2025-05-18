package com.gestionpfes.adnan.DTO;


import lombok.Data;

@Data
public class RequestDTO {

    Long senderID;
    Long requestID;
    String senderEmail;
    String subject;
    String status;
    Boolean seen;
    
}
