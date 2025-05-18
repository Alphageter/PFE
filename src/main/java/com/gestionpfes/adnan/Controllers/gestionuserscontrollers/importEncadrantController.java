package com.gestionpfes.adnan.Controllers.gestionuserscontrollers;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.EncryptedDocumentException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import com.gestionpfes.adnan.models.Encadrant;
import com.gestionpfes.adnan.models.User;
import com.gestionpfes.adnan.services.EncadrantService;
import com.gestionpfes.adnan.services.UserService;

@Controller
public class importEncadrantController {

    @Autowired
    private UserService userService;

    @Autowired
    private EncadrantService encadrantService;
    
    @PostMapping("/Admin/importlistEncadrant")
    public String importListEncadrant(@RequestParam("file") MultipartFile file,
                                     @RequestParam("filier") String filier,
                                     RedirectAttributes redirectAttributes) {
    
        List<Integer> errorRows = new ArrayList<>();
    
        // Validate file extension
        if (!file.getOriginalFilename().endsWith(".xlsx") && !file.getOriginalFilename().endsWith(".xls")) {
            redirectAttributes.addFlashAttribute("messagfail", "Format de fichier invalide. Veuillez télécharger un fichier Excel.");
            return "redirect:/Admin/newEncadrant";
        }
    
        try {
            // Load Excel file into a workbook
            Workbook workbook = WorkbookFactory.create(file.getInputStream());
    
            // Get the first sheet from the workbook
            Sheet sheet = workbook.getSheetAt(0);
    
            // Define column headers
            String[] requiredColumns = {"nom", "prenom", "email"};
    
            // Validate column headers
            Row headerRow = sheet.getRow(0);
            for (String column : requiredColumns) {
                if (!cellValueToString(headerRow.getCell(getCellIndex(headerRow, column))).equalsIgnoreCase(column)) {
                    redirectAttributes.addFlashAttribute("messagfail", "En-têtes de colonne non valides. Colonnes obligatoires : nom, prenom, email");
                    return "redirect:/Admin/newEncadrant";
                }
            }
    
            // Iterate over rows (excluding the header row)
            for (int rowIndex = 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
                Row dataRow = sheet.getRow(rowIndex);
    
                // Get values from each cell
                String nom = cellValueToString(dataRow.getCell(0));
                String prenom = cellValueToString(dataRow.getCell(1));
                String email = cellValueToString(dataRow.getCell(2));
                // String napogeValue = cellValueToString(dataRow.getCell(getCellIndex(dataRow, "N_apogee")));
    
                // Validate required fields
                if (nom.isEmpty() || prenom.isEmpty() || email.isEmpty()) {
                    errorRows.add(rowIndex + 1); // Add the row number with an issue to the list
                    continue; // Skip to the next row
                }
                User user = userService.getUserByEmail(email);
                if(user!=null){
                    errorRows.add(rowIndex + 1); 
                    continue; // Skip to the next row
                }
    
                // Parse N_apogee value
                // Long napoge;
                // try {
                //     napoge = Long.parseLong(napogeValue);
                // } catch (NumberFormatException e) {
                //     errorRows.add(rowIndex + 1); // Add the row number with an issue to the list
                //     continue; // Skip to the next row
                // }
    
                // Create and save the Encadrant entity
                Encadrant encadrant = new Encadrant();
                encadrant.setName(nom);
                encadrant.setLastname(prenom);
                encadrant.setEmail(email);
                
                encadrant.setPassword(prenom);
                encadrant.setValidation(false);
               
                encadrant.setFilier(filier);
               
                encadrantService.save(encadrant);
            }
    
            // Check if there were any rows with issues
            if (!errorRows.isEmpty()) {
                
                redirectAttributes.addFlashAttribute("messagfail", "Importation terminée avec certaines lignes contenant des erreurs : " + errorRows);
            } else {
                redirectAttributes.addFlashAttribute("messagesucces", "Importation terminée avec succès. ");
            }
    
            workbook.close();
            return "redirect:/Admin/listEncadrant"; 
        } catch (IOException  e) {
            redirectAttributes.addFlashAttribute("messagfail", "Une erreur s'est produite lors du traitement du fichier.");
        }catch (EncryptedDocumentException e) {
            // Handle encrypted document exception
            e.printStackTrace();
        }
    
        return "redirect:/Admin/newEncadrant";
    }

    private String cellValueToString(Cell cell) {
        if (cell == null) {
            return "";
        }
    
        CellType cellType = cell.getCellType();
    
        if (cellType == CellType.STRING) {
          
            return cell.getStringCellValue();
        } else if (cellType == CellType.NUMERIC) {
            if (DateUtil.isCellDateFormatted(cell)) {
               
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                return dateFormat.format(cell.getDateCellValue());
            } else {
                double numericValue = cell.getNumericCellValue();
              
                return String.valueOf(numericValue);
            }
        } else if (cellType == CellType.BOOLEAN) {
           
            return String.valueOf(cell.getBooleanCellValue());
        } else {
            return "";
        }
    }
    
    
    private int getCellIndex(Row row, String columnName) {
        for (Cell cell : row) {
            CellType cellType = cell.getCellType();
            if (cellType == CellType.STRING && cell.getStringCellValue().equalsIgnoreCase(columnName)) {
               
                return cell.getColumnIndex();
            }
        }
        
        return -1; // Column not found
    }
    
}
