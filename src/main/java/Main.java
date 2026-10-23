import dao.DatabaseConnection;
import ui.MainMenu;

/** App entry point. */
public class Main {
    public static void main(String[] args) {
        DatabaseConnection.initialize();   // creates studentpayment.db + users table
        new MainMenu().start();
    }
}
