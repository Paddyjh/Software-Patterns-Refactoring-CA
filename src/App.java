import Controller.EmployeeController;
import View.EmployeeDetails;

public class App {

    public static void main(String args[]) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                EmployeeDetails employeeDetailsView = new EmployeeDetails();
                EmployeeController controller = new EmployeeController(employeeDetailsView);
                employeeDetailsView.setController(controller);
            }
        });
    }//
}
