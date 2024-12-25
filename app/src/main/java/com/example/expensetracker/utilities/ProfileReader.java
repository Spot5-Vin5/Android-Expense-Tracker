package com.example.expensetracker.utilities;

import static com.example.expensetracker.utilities.HeadingConstants.BASE_PATH;
import static com.example.expensetracker.utilities.HeadingConstants.PROFILE_ACTIVITY;

import android.content.Context;

import com.example.expensetracker.NewUserActivity;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import java.util.ArrayList;
import java.util.Properties;

public class ProfileReader {
    public static String nameFromDB;
    public static String emailFromDB;

    // public static void profileReader(Context context) {
    public static ArrayList<String> profileReader(String sheetName,ArrayList<String> getScripts) {

        // Initialize variables in SharedVariables
        SingleTonSharedVariables sharedVariables = SingleTonSharedVariables.getInstance();

        /*// reading from application.properties file
        ApplicationPropertiesReader propertiesReader = new ApplicationPropertiesReader(context);
        Properties properties = propertiesReader.getProperties("application.properties");
        String basePath = properties.getProperty("file.basePath");*/

        // Retrieve data from your database
        //String basePath = "/Android/data/com.example.expensetracker/files/Expense Tracker App/";

       /* String fileName = NewUserActivity.fileName;
        String excelFilePath = BASE_PATH + fileName;*/
       // String sheetName = PROFILE_ACTIVITY;

        try (FileInputStream fileInputStream = new FileInputStream(new File(sharedVariables.getFilePath()));
             Workbook workbook = new XSSFWorkbook(fileInputStream)) {

            Sheet sheet = workbook.getSheet(sheetName);
            if (sheet == null) {
                System.out.println("Sheet not found: " + sheetName);
            } else {
                String nameValuePosition = "B2";
                String emailValuePosition = "B3";
                CellReference cellReference2 = new CellReference(nameValuePosition);
                CellReference cellReference3 = new CellReference(emailValuePosition);

                Row row2 = sheet.getRow(cellReference2.getRow());
                Cell cell2 = row2.getCell(cellReference2.getCol());

                Row row3 = sheet.getRow(cellReference3.getRow());
                Cell cell3 = row3.getCell(cellReference3.getCol());

                if ((cell2 != null) && (cell3 != null)) {
                    nameFromDB = cell2.getStringCellValue();
                    getScripts.add(nameFromDB);

                    emailFromDB = cell3.getStringCellValue();
                    getScripts.add(emailFromDB);
                }

              /*  for (int i = 0; i < sheet.getRows(); i++) {
                    Cell[] row = sheet.getRow(i);
                    titles.add(row[0].getContents());
                    descriptions.add(row[1].getContents());
                    imageUrl.add(row[2].getContents());
                }*/
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return getScripts;
    }
}

