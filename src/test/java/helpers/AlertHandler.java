package helpers;

import org.openqa.selenium.Alert;
import org.openqa.selenium.WebDriver;

import java.sql.SQLOutput;

public class AlertHandler {
    WebDriver driver;
    public AlertHandler(WebDriver driver){
        this.driver = driver;
    }

    public static boolean handlerAlert(Alert alert, String expectedText){
        if(alert !=null) {
            String alertText = alert.getText();
            System.out.println("ALERT_TEXT " + alertText);
            alert.accept(); // закрывает окно
            return alertText.contains(expectedText);
        }
            return false;
    }

}
