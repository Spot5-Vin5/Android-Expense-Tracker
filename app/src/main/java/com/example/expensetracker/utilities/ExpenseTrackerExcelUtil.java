package com.example.expensetracker.utilities;

import static com.example.expensetracker.utilities.HeadingConstants.AMOUNT;
import static com.example.expensetracker.utilities.HeadingConstants.BASE_PATH;
import static com.example.expensetracker.utilities.HeadingConstants.CATEGORY;
import static com.example.expensetracker.utilities.HeadingConstants.DATE;
import static com.example.expensetracker.utilities.HeadingConstants.NOTE;
import static com.example.expensetracker.utilities.HeadingConstants.PAYMENT;
import static com.example.expensetracker.utilities.HeadingConstants.PAYMENT_SUBTYPE;
import static com.example.expensetracker.utilities.HeadingConstants.SUBCATEGORY;

import com.example.expensetracker.NewUserActivity;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ExpenseTrackerExcelUtil {

    public static String fileName = NewUserActivity.fileName;
    public static final String excelFilePath = BASE_PATH + fileName;
    //public static final String sheetName = CATEGORIES;

    public static ArrayList<String> readTypesFromExcelUtil(String sheetName, ArrayList<String> typeList, HashMap<String, ArrayList<String>> typesToSubtypesMap) {
        System.out.println("inside ExpenseTrackerExcelUtil class, inside readTypesFromExcelUtil () 1 of 2, ==Started==");
        /*   // reading from application.properties file
        ApplicationPropertiesReader propertiesReader = new ApplicationPropertiesReader(context);
        Properties properties = propertiesReader.getProperties("application.properties");
        String basePath = properties.getProperty("file.basePath");
*/
        // Retrieve data from your database defined as global variables
     /*   String fileName = NewUserActivity.fileName;
        String excelFilePath = BASE_PATH + fileName;
        String sheetName = CATEGORIES;*/

        try (FileInputStream fileInputStream = new FileInputStream(new File(excelFilePath)); Workbook workbook = new XSSFWorkbook(fileInputStream)) {
            //    Workbook workbook =NewUserActivity.workbook; // for testing
            Sheet sheet = workbook.getSheet(sheetName);
            if (sheet == null) {
                System.out.println("Sheet not found: " + sheetName);
            } else {
                int columnIndex = 0;//catType column
                for (Row row : sheet) {
                    Cell cell = row.getCell(columnIndex, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
                    if (cell != null && (cell.getCellType() == CellType.STRING || cell.getCellType() != CellType.BLANK)) {
                        String typeFromSheet = cell.getStringCellValue();
                        if (!typeFromSheet.contains(sheetName) && !typeFromSheet.contains("Type")) {
                            typeList.add(typeFromSheet); // Add category to the list
                            // initialize value with null here and in subcategory class assign the subCatArraylist as value.
                            readSubTypesFromSheet(row, typeFromSheet, typesToSubtypesMap);
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("inside ExpenseTrackerExcelUtil class, inside readTypesFromExcelUtil () 2 of 2, === Ended== typeList :"+typeList);
        return typeList;
    }

    public static void readSubTypesFromSheet(Row row, String typeFromSheet, HashMap<String, ArrayList<String>> typesToSubtypesMap) {

        ArrayList<String> subCatList = new ArrayList<String>();
        int subCatColumnIndex = 1;//Subcat starts at index1
        Cell subCell;
        do {
            subCell = row.getCell(subCatColumnIndex, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
            //(cell != null && (cell.getCellType() == CellType.STRING || cell.getCellType() != CellType.BLANK))
            if (subCell == null || subCell.getCellType() == CellType.BLANK) {
                break;
            } else {
                if (subCell.getCellType() == CellType.STRING) {
                    subCatList.add(subCell.getStringCellValue());
                    subCatColumnIndex++;
                }
            }
        } while (subCell.getCellType() != CellType.BLANK);
        typesToSubtypesMap.put(typeFromSheet, subCatList);
       /* if (!subCatList.isEmpty()) {
            categoryToSubcategoriesMap.put(categoryFromSheet, subCatList);
        } else {
            categoryToSubcategoriesMap.put(categoryFromSheet, null);
        }*/
    }

    public static void writeTypesToExcelUtil(String sheetName, String type, ArrayList<String> typeList, HashMap<String, ArrayList<String>> typesToSubtypesMap) {

        try (FileInputStream fileInputStream = new FileInputStream(new File(excelFilePath)); Workbook workbook = new XSSFWorkbook(fileInputStream)) {

            //   Sheet sheet = workbook.getSheet(CATEGORIES);
            Sheet sheet = workbook.getSheet(sheetName);

            if (!(typeList.contains(type))) {
                int highestRowIndex = sheet.getPhysicalNumberOfRows(); // to get how many rows have data in sheet.
                sheet.createRow(highestRowIndex).createCell(0).setCellValue(type); // to add new category in the new row.
                typeList.add(type);
                typesToSubtypesMap.put(type, new ArrayList<String>());
            }
            // Write changes to the file
            try (FileOutputStream fileOut = new FileOutputStream(new File(excelFilePath))) {
                workbook.write(fileOut);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void writeSubTypesToExcelUtil(String sheetName, String typeName, String subType, ArrayList<String> readSubTypesListFromSheet, HashMap<String, ArrayList<String>> typesToSubtypesMap) {

        try (FileInputStream fileInputStream = new FileInputStream(new File(excelFilePath)); Workbook workbook = new XSSFWorkbook(fileInputStream)) {
            Sheet sheet = workbook.getSheet(sheetName);
            if (!(readSubTypesListFromSheet.contains(subType))) {

                for (Row row : sheet) {
                    Cell cell = row.getCell(0, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
                    if (cell != null && (cell.getCellType() == CellType.STRING || cell.getCellType() != CellType.BLANK)) {
                        String categoryFromSheet = cell.getStringCellValue();
                        if (categoryFromSheet.equalsIgnoreCase(typeName)) {
                            int rowIndex = row.getRowNum();
                            int columnIndex = 1;
                            Cell rowcellcheck;
                            do {
                                rowcellcheck = sheet.getRow(rowIndex).getCell(columnIndex, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
                                if (rowcellcheck == null || rowcellcheck.getCellType() == CellType.BLANK) {
                                    sheet.getRow(rowIndex).createCell(columnIndex).setCellValue(subType);
                                    readSubTypesListFromSheet.add(subType); // Add subcategory/subpayment to the list
                                    break;
                                } else {
                                    columnIndex++;
                                }
                            } while (rowcellcheck.getCellType() != CellType.BLANK);
                        }
                    }
                }
                typesToSubtypesMap.put(typeName, readSubTypesListFromSheet);
            }
            // Write changes to the file
            try (FileOutputStream fileOut = new FileOutputStream(new File(excelFilePath))) {
                workbook.write(fileOut);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static ArrayList<String> readProfileFromExcel(String sheetName, ArrayList<String> getScripts) {

        String fileName = NewUserActivity.fileName;
        String excelFilePath = BASE_PATH + fileName;

        try (FileInputStream fileInputStream = new FileInputStream(new File(excelFilePath)); Workbook workbook = new XSSFWorkbook(fileInputStream)) {

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
                    String nameFromDB = cell2.getStringCellValue();
                    getScripts.add(nameFromDB);

                    String emailFromDB = cell3.getStringCellValue();
                    getScripts.add(emailFromDB);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return getScripts;
    }

    public static String writeAddExpenseToSheet(String sheetName, HashMap<String, String> addExpenseDataMap) {

        try (FileInputStream fileInputStream = new FileInputStream(new File(excelFilePath)); Workbook workbook = new XSSFWorkbook(fileInputStream)) {

            //  Set set=addExpenseDataMap.keySet();
            Sheet sheet = workbook.getSheet(sheetName);

            int highestRowIndex = sheet.getPhysicalNumberOfRows(); // to get how many rows have data in sheet.
            sheet.createRow(highestRowIndex).createCell(0).setCellValue(addExpenseDataMap.get(DATE)); // to add new category in the new row.
            sheet.getRow(highestRowIndex).createCell(1).setCellValue(addExpenseDataMap.get(AMOUNT));
            sheet.getRow(highestRowIndex).createCell(2).setCellValue(addExpenseDataMap.get(CATEGORY));
            sheet.getRow(highestRowIndex).createCell(3).setCellValue(addExpenseDataMap.get(SUBCATEGORY));
            sheet.getRow(highestRowIndex).createCell(4).setCellValue(addExpenseDataMap.get(PAYMENT));
            sheet.getRow(highestRowIndex).createCell(5).setCellValue(addExpenseDataMap.get(PAYMENT_SUBTYPE));
            sheet.getRow(highestRowIndex).createCell(6).setCellValue(addExpenseDataMap.get(NOTE));

            //  typeList.add(type);
            try (FileOutputStream fileOut = new FileOutputStream(new File(excelFilePath))) {
                workbook.write(fileOut);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return "Expense Added Successfully";
    }

/*    public static ArrayList<HashMap<String, String>> readExpenseTransactionsFromExcelUtil(String sheetName){
        public static ArrayList<HashMap<String, String>> expenseDataRowMapList = new ArrayList<HashMap<String, String>>();


        return typeList;
    }*/

    //readExpenseDataRowMapListFromExcel = ExpenseTrackerExcelUtil.readExpenseTransactionsFromExcelUtil(EXPENSE, expenseColumnIndices, new ArrayList<HashMap<String, String>>());
    public static ArrayList<HashMap<String, String>> readExpenseTransactionsFromExcelUtil(String sheetName, HashMap<String, Integer> expenseColumnIndices, ArrayList<HashMap<String, String>> expenseDataRowMapList) {

        System.out.println("inside ExpenseTrackerExcelUtil class, inside readExpenseTransactionsFromExcelUtil () , ==Started==");
        try (FileInputStream fileInputStream = new FileInputStream(new File(excelFilePath)); Workbook workbook = new XSSFWorkbook(fileInputStream)) {

            Sheet sheet = workbook.getSheet(sheetName);
            System.out.println("inside ExpenseTrackerExcelUtil class, inside readExpenseTransactionsFromExcelUtil () ,"+sheet);

            if (sheet == null) {
                System.out.println("Sheet not found: " + sheetName);
            } else {
                System.out.println("inside ExpenseTrackerExcelUtil class, inside readExpenseTransactionsFromExcelUtil () , --else block-- ==start==");
                //int highestRowIndex = sheet.getPhysicalNumberOfRows(); // to get how many rows have data in sheet.
                //int columnIndex = 0;//catType column
                for (Row row : sheet) {
                    System.out.println("inside ExpenseTrackerExcelUtil class, inside readExpenseTransactionsFromExcelUtil () , --else block--"+row);
                    Cell dateCell = row.getCell(expenseColumnIndices.get(DATE), Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
                    Cell amountCell = row.getCell(expenseColumnIndices.get(AMOUNT), Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
                    Cell categoryCell = row.getCell(expenseColumnIndices.get(CATEGORY), Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
                    Cell subcategoryCell = row.getCell(expenseColumnIndices.get(SUBCATEGORY), Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
                    Cell paymentCell = row.getCell(expenseColumnIndices.get(PAYMENT), Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
                    Cell paymentSubtypeCell = row.getCell(expenseColumnIndices.get(PAYMENT_SUBTYPE), Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
                    Cell noteCell = row.getCell(expenseColumnIndices.get(NOTE), Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);

                    System.out.println("inside ExpenseTrackerExcelUtil class, inside readExpenseTransactionsFromExcelUtil () , --else block-- inside for loop: "+dateCell + ","+ amountCell +","+categoryCell+"," +subcategoryCell+"," +paymentCell+"," +paymentSubtypeCell +","+noteCell);

                    if ((dateCell != null && (dateCell.getCellType() == CellType.STRING || dateCell.getCellType() != CellType.BLANK))
                            && (categoryCell != null && (categoryCell.getCellType() == CellType.STRING || categoryCell.getCellType() != CellType.BLANK))
                            && (paymentCell != null && (paymentCell.getCellType() == CellType.STRING || paymentCell.getCellType() != CellType.BLANK))
                    ) {
                        System.out.println("inside ExpenseTrackerExcelUtil class, inside readExpenseTransactionsFromExcelUtil () , --else block-- inside for loop **1st if block");
                        String rowDate = dateCell.getStringCellValue();// to skip 1st 2 rows which are not required
                        if (!rowDate.contains(sheetName) && !rowDate.contains(DATE) && !rowDate.contains(AMOUNT) && !rowDate.contains(CATEGORY)
                                && !rowDate.contains(SUBCATEGORY) && !rowDate.contains(PAYMENT) && !rowDate.contains(PAYMENT_SUBTYPE) && !rowDate.contains(NOTE)) {
                            System.out.println("inside ExpenseTrackerExcelUtil class, inside readExpenseTransactionsFromExcelUtil () , --else block-- inside for loop **2nd if block");

                            HashMap<String, String> rowExpenseDataMap = new HashMap<String, String>(); // new map for each row

                            rowExpenseDataMap.put(DATE, dateCell.getStringCellValue());
                            rowExpenseDataMap.put(CATEGORY, categoryCell.getStringCellValue());
                            rowExpenseDataMap.put(PAYMENT, paymentCell.getStringCellValue());

                            if ((amountCell != null && (amountCell.getCellType() == CellType.STRING || amountCell.getCellType() != CellType.BLANK))) {
                                rowExpenseDataMap.put(AMOUNT, amountCell.getStringCellValue());
                                System.out.println("inside ExpenseTrackerExcelUtil class, inside readExpenseTransactionsFromExcelUtil () , --else block-- inside for loop **3rd if block");
                            } else if ((amountCell != null && (amountCell.getCellType() == CellType.NUMERIC || amountCell.getCellType() != CellType.BLANK))) {
                                rowExpenseDataMap.put(AMOUNT, Integer.toString((int) amountCell.getNumericCellValue()));
                                System.out.println("inside ExpenseTrackerExcelUtil class, inside readExpenseTransactionsFromExcelUtil () , --else block-- inside for loop **3rd if-else block");
                            }

                            if ((subcategoryCell != null && (subcategoryCell.getCellType() == CellType.STRING || subcategoryCell.getCellType() != CellType.BLANK)) ){
                                rowExpenseDataMap.put(SUBCATEGORY, subcategoryCell.getStringCellValue());
                                System.out.println("inside ExpenseTrackerExcelUtil class, inside readExpenseTransactionsFromExcelUtil () , --else block-- inside for loop **4th if block");
                            }else{
                                rowExpenseDataMap.put(SUBCATEGORY, "");
                                System.out.println("inside ExpenseTrackerExcelUtil class, inside readExpenseTransactionsFromExcelUtil () , --else block-- inside for loop **4th if-else block");
                            }
                            if ((paymentSubtypeCell != null && (paymentSubtypeCell.getCellType() == CellType.STRING || paymentSubtypeCell.getCellType() != CellType.BLANK)) ){
                                rowExpenseDataMap.put(PAYMENT_SUBTYPE, paymentSubtypeCell.getStringCellValue());
                                System.out.println("inside ExpenseTrackerExcelUtil class, inside readExpenseTransactionsFromExcelUtil () , --else block-- inside for loop **5th if block");
                            }else{
                                rowExpenseDataMap.put(PAYMENT_SUBTYPE, "");
                                System.out.println("inside ExpenseTrackerExcelUtil class, inside readExpenseTransactionsFromExcelUtil () , --else block-- inside for loop **5th if-else block");
                            }
                            if ((noteCell != null && (noteCell.getCellType() == CellType.STRING || noteCell.getCellType() != CellType.BLANK))) {
                                rowExpenseDataMap.put(NOTE, noteCell.getStringCellValue());
                                System.out.println("inside ExpenseTrackerExcelUtil class, inside readExpenseTransactionsFromExcelUtil () , --else block-- inside for loop **6th if block");
                            } else {
                                rowExpenseDataMap.put(NOTE, "");//set to empty string
                                System.out.println("inside ExpenseTrackerExcelUtil class, inside readExpenseTransactionsFromExcelUtil () , --else block-- inside for loop **6th if-else block");
                            }

                            expenseDataRowMapList.add(rowExpenseDataMap); //Add Map to the list
                            System.out.println("inside ExpenseTrackerExcelUtil class, inside readExpenseTransactionsFromExcelUtil () , --else block-- inside for loop, rowExpenseDataMap : "+rowExpenseDataMap);
                            System.out.println("inside ExpenseTrackerExcelUtil class, inside readExpenseTransactionsFromExcelUtil () , --else block-- inside for loop, expenseDataRowMapList : "+expenseDataRowMapList);
                        }
                    }
                }
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("inside ExpenseTrackerExcelUtil class, inside readExpenseTransactionsFromExcelUtil () , ==ended==");
        return expenseDataRowMapList;
    }

    public static ArrayList<String> readAllSubPaymentsFromExcel(HashMap<String, ArrayList<String>> paymentToSubPaymentMap) {
        ArrayList<String> readAllSubPaymentsFromExcel = new ArrayList<>();
        for (Map.Entry<String, ArrayList<String>> entry : paymentToSubPaymentMap.entrySet()) {
            readAllSubPaymentsFromExcel.addAll(entry.getValue());
        }
        return readAllSubPaymentsFromExcel;
    }

}


