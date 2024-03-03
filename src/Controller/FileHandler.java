package Controller;

import Model.RandomFile;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Random;

public class FileHandler {
    EmployeeController controller;
    String generatedFileName;
    File file;
    RandomFile application = RandomFile.getInstance();
    boolean changesMade = false;
    FileNameExtensionFilter datfilter = new FileNameExtensionFilter("dat files (*.dat)", "dat");

    public void setController(EmployeeController controller) {
        this.controller = controller;
    }

    public File getFile() {
        return this.file;
    }

    public RandomFile getRandomFile() {
        return this.application;
    }

    public boolean getChangesMade() {
        return this.changesMade;
    }

    public void setChangesMade(boolean changesMade) {
        this.changesMade = changesMade;
    }

    void createRandomFile() {
        generatedFileName = getFileName() + ".dat";
        // assign generated file name to file
        file = new File(generatedFileName);
        // create file
        application.createFile(file.getName());
    }// end createRandomFile

    private String getFileName() {
        String fileNameChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890_-";
        StringBuilder fileName = new StringBuilder();
        Random rnd = new Random();
        // loop until 20 character long file name is generated
        while (fileName.length() < 20) {
            int index = (int) (rnd.nextFloat() * fileNameChars.length());
            fileName.append(fileNameChars.charAt(index));
        }
        String generatedfileName = fileName.toString();
        return generatedfileName;
    }// end getFileName

    public void openFile() {
        final JFileChooser fc = new JFileChooser();
        fc.setDialogTitle("Open");
        // display files in File Chooser only with extension .dat
        fc.setFileFilter(datfilter);
        File newFile; // holds opened file name and path
        // if old file is not empty or changes has been made, offer user to save
        // old file
        if (file.length() != 0 || controller.change) {
            int returnVal = JOptionPane.showOptionDialog(controller.employeeDetailsView, "Do you want to save changes?", "Save", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, null, null, null);
            // if user wants to save file, save it
            if (returnVal == JOptionPane.YES_OPTION) {
                saveFile();// save file
            } // end if
        } // end if

        int returnVal = fc.showOpenDialog(controller.employeeDetailsView);
        // if file been chosen, open it
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            newFile = fc.getSelectedFile();
            // if old file wasn't saved and its name is generated file name,
            // delete this file
            if (file.getName().equals(generatedFileName)) file.delete();// delete file
            file = newFile;// assign opened file to file
            // open file for reading
            application.openReadFile(file.getAbsolutePath());
            controller.firstRecord();// look for first record
            controller.employeeDetailsView.displayRecords(controller.currentEmployee);
            application.closeReadFile();// close file for reading
        } // end if
    }// end openFile

    public void saveFile() {
        // if file name is generated file name, save file as 'save as' else save
        // changes to file
        if (file.getName().equals(generatedFileName)) saveFileAs();// save file as 'save as'
        else {
            // if changes has been made to text field offer user to save these
            // changes
            if (controller.change) {
                int returnVal = JOptionPane.showOptionDialog(controller.employeeDetailsView, "Do you want to save changes?", "Save", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, null, null, null);
                // save changes if user choose this option
                if (returnVal == JOptionPane.YES_OPTION) {
                    // save changes if ID field is not empty
                    if (!controller.employeeDetailsView.getIdField().getText().equals("")) {
                        // open file for writing
                        application.openWriteFile(file.getAbsolutePath());
                        // get changes for current Model.Employee
                        controller.currentEmployee = controller.employeeDetailsView.getChangedDetails(); //May need to modify this method
                        // write changes to file for corresponding Model.Employee
                        // record
                        application.changeRecords(controller.currentEmployee, controller.currentByteStart);
                        application.closeWriteFile();// close file for writing
                    } // end if
                } // end if
            } // end if

            controller.employeeDetailsView.displayRecords(controller.currentEmployee);
            controller.employeeDetailsView.setEnabled(false);
        } // end else
    }// end saveFile

    // save changes to current Employee
    public void saveChanges() {
        int returnVal = JOptionPane.showOptionDialog(controller.employeeDetailsView, "Do you want to save changes to current Employee?", "Save", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, null, null, null);
        // if user choose to save changes, save changes
        if (returnVal == JOptionPane.YES_OPTION) {
            // open file for writing
            application.openWriteFile(file.getAbsolutePath());
            // get changes for current Model.Employee
            controller.currentEmployee = controller.employeeDetailsView.getChangedDetails(); //Again may need to modify this method
            // write changes to file for corresponding Model.Employee record
            application.changeRecords(controller.currentEmployee, controller.currentByteStart);
            application.closeWriteFile();// close file for writing
            changesMade = false;// state that all changes has bee saved
        } // end if
        controller.employeeDetailsView.displayRecords(controller.currentEmployee);
        controller.employeeDetailsView.setEnabled(false);
    }// end saveChanges

    // save file as 'save as'
    //Potentially move elsewhere or adapt
    public void saveFileAs() {
        final JFileChooser fc = new JFileChooser();
        File newFile;
        String defaultFileName = "new_Employee.dat";
        fc.setDialogTitle("Save As");
        // display files only with .dat extension
        fc.setFileFilter(datfilter);
        fc.setApproveButtonText("Save");
        fc.setSelectedFile(new File(defaultFileName));

        int returnVal = fc.showSaveDialog(controller.employeeDetailsView);
        // if file has chosen or written, save old file in new file
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            newFile = fc.getSelectedFile();
            // check for file name
            if (!checkFileName(newFile)) {
                // add .dat extension if it was not there
                newFile = new File(newFile.getAbsolutePath() + ".dat");
                // create new file
                application.createFile(newFile.getAbsolutePath());
            } // end id
            else
                // create new file
                application.createFile(newFile.getAbsolutePath());

            try {// try to copy old file to new file
                Files.copy(file.toPath(), newFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                // if old file name was generated file name, delete it
                if (file.getName().equals(generatedFileName)) file.delete();// delete file
                file = newFile;// assign new file to file
            } // end try
            catch (IOException e) {
            } // end catch
        } // end if
        changesMade = false;
    }// end saveFileAs


    private boolean checkFileName(File fileName) {
        boolean checkFile = false;
        int length = fileName.toString().length();
        // check if last characters in file name is .dat
        if (fileName.toString().charAt(length - 4) == '.' && fileName.toString().charAt(length - 3) == 'd' && fileName.toString().charAt(length - 2) == 'a' && fileName.toString().charAt(length - 1) == 't')
            checkFile = true;
        return checkFile;
    }// end checkFileName

    public void exitApp() {
        // if file is not empty allow to save changes
        if (file.length() != 0) {
            if (changesMade) {
                int returnVal = JOptionPane.showOptionDialog(controller.employeeDetailsView, "Do you want to save changes?", "Save", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null, null, null);
                // if user chooses to save file, save file
                if (returnVal == JOptionPane.YES_OPTION) {
                    saveFile();// save file
                    // delete generated file if user saved details to other file
                    if (file.getName().equals(generatedFileName)) file.delete();// delete file
                    System.exit(0);// exit application
                } // end if
                // else exit application
                else if (returnVal == JOptionPane.NO_OPTION) {
                    // delete generated file if user chooses not to save file
                    if (file.getName().equals(generatedFileName)) file.delete();// delete file
                    System.exit(0);// exit application
                } // end else if
            } // end if
            else {
                // delete generated file if user chooses not to save file
                if (file.getName().equals(generatedFileName)) file.delete();// delete file
                System.exit(0);// exit application
            } // end else
            // else exit application
        } else {
            // delete generated file if user chooses not to save file
            if (file.getName().equals(generatedFileName)) file.delete();// delete file
            System.exit(0);// exit application
        } // end else
    }// end exitApp

}
