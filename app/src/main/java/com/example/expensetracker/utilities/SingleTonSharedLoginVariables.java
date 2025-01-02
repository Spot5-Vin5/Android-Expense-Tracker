package com.example.expensetracker.utilities;

public class SingleTonSharedLoginVariables {
    private String basePath; //basePath = getExternalFilesDir(null).getAbsolutePath();
    private String fileName; //fileName = email+ "_expensesFile.xlsx";
    private String filePath; //filePath = basePath + "/Expense Tracker App/" + fileName;
    private String email;
    private String expensesFilesAppFolder; //expensesFilesAppFolder = basePath + "/Expense Tracker App/";

    // Singleton instance
    private static SingleTonSharedLoginVariables instance;

    // Private constructor to prevent instantiation
    private SingleTonSharedLoginVariables() {
    }

    // Method to get the single instance
    public static SingleTonSharedLoginVariables getInstance() {
        if (instance == null) {
            instance = new SingleTonSharedLoginVariables();
        }
        return instance;
    }

    // Getters and setters for the variables
    public String getBasePath() {
        return basePath;
    }

    public void setBasePath(String basePath) {
        this.basePath = basePath;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getExpensesFilesAppFolder() {
        return expensesFilesAppFolder;
    }

    public void setExpensesFilesAppFolder(String expensesFilesAppFolder) {
        this.expensesFilesAppFolder = expensesFilesAppFolder;
    }
}
