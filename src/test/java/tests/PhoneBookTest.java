package tests;

import config.BaseTest;
import helpers.*;
import io.qameta.allure.Allure;
import io.qameta.allure.Description;
import model.Contact;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.RemoteWebElement;
import org.testng.Assert;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import pages.AddPage;
import pages.ContactsPage;
import pages.LoginPage;
import pages.MainPage;

import java.io.IOException;

public class PhoneBookTest extends BaseTest {
    @Test
    @Parameters("browser")
    public void registrationWithoutPassword(@Optional(value="firefox")String browser) throws InterruptedException {
        Allure.description("User already exist. Login and add contact.!");
        MainPage mainPage = new MainPage(getDriver());
        Allure.step("Click by login button");
        LoginPage loginPage = mainPage.openTopMenu(TopMenuItem.LOGIN.toString());
        Allure.step("Click by Reg button");
        String expectedString = "Wrong"; // берется из файла настроек
        Alert alert = loginPage.fillEmailField("myemail@mail.com").clickByRegistrationButton();
        boolean isAlertHandler = AlertHandler.handlerAlert(alert, expectedString);
        Assert.assertTrue(isAlertHandler);
       // loginPage.fillPasswordField("aaAA12$").
        Thread.sleep(5000);
    }


    @Test
    @Description("User already exist. Login and add contact.")
       public void loginOfAnExistingUserAddContact() throws InterruptedException {
        Allure.description("User already exist. Login and add contact.!"); // описание шага теста
        MainPage mainPage = new MainPage(getDriver()); // создает экземпляр главной странийы с вызовом Драйвера
        Allure.step("Step 1"); // создание Шага в тесте
        LoginPage lpage = mainPage.openTopMenu(TopMenuItem.LOGIN.toString());
        // сохраняет в переменную lpage строку названия пункта главного меню LOGIN
        Allure.step("Step 2"); // создание Шага в тесте
        lpage.fillEmailField(PropertiesReader.getProperty("existingUserEmail"))
                .fillPasswordField(PropertiesReader.getProperty("existingUserPassword"))
                .clickByLoginButton();
        // заполняет поля на странице lpage значениями из файла PropertiesReader
        // и нажимает на кн.Login
        Allure.step("Step 3"); // создание Шага в тесте
        MainPage.openTopMenu(TopMenuItem.ADD.toString()); //открытие страницы добавления контакта
        AddPage addPage = new AddPage(getDriver());
        //создает новый экземпляр AddPage, используя объект драйвера для взаимодействия
        // для использования методов класса AddPage.
        Contact newContact = new Contact(NameAndLastNameGenerator.generateName(),
                NameAndLastNameGenerator.generateLastName(),
                PhoneNumberGenerator.generatePhoneNumber(),
                EmailGenerator.generateEmail(10,5,3),
                AddressGenerator.generateAddress(),
                "new description");
        // создает новый контакт
        newContact.toString(); // выводит значения созданного контакта в виде строки
        addPage.fillFormAndSave(newContact); // сохраняет новый контакт
        ContactsPage contactsPage = new ContactsPage(getDriver());
        // переходит на страницу Контакты и вызывает дрйвер
        Assert.assertTrue(contactsPage.getDataFromContactList(newContact));
        // считывает информацию из вновь созданного контакта
        TakeScreen.takeScreenshot("screen"); // делает скриншит данной страницы
        Thread.sleep(3000); // пауза 3 сек

    }
    @Test
    @Description("Successful Registration")
    public void successfulRegistration(){
        Allure.description("Successful Registration test.");
        MainPage mainPage = new MainPage(getDriver());
        Allure.step("Open LOGIN menu");
        LoginPage lpage = mainPage.openTopMenu(TopMenuItem.LOGIN.toString());
        lpage.fillEmailField(EmailGenerator.generateEmail(5,5,3))
                .fillPasswordField(PasswordStringGenerator.generateString());
        Alert alert =  lpage.clickByRegistrationButton();
        if (alert==null){
            ContactsPage contactsPage = new ContactsPage(getDriver());
            Assert.assertTrue( contactsPage. isElementPersist(getDriver()
                    .findElement(By.xpath("//button[contains(text(),'Sign Out')]"))));
        }else {
            TakeScreen.takeScreenshot("Successful Registration");}
    }
    @Test
    public void deleteContact() throws InterruptedException {
        Allure.description("User already exist. Delete contact by phone number!");
        MainPage mainPage = new MainPage(getDriver());
        Allure.step("Step 1");
        LoginPage lpage = mainPage.openTopMenu(TopMenuItem.LOGIN.toString());
        Allure.step("Step 2");
        lpage.fillEmailField(PropertiesReader.getProperty("existingUserEmail"))
                .fillPasswordField(PropertiesReader.getProperty("existingUserPassword"))
                .clickByLoginButton();
        ContactsPage contactsPage = new ContactsPage(getDriver());
        Assert.assertNotEquals(contactsPage.deleteContactByPhoneNumberOrName("2101225254138"),
                contactsPage.getContactsListSize(),"Contact lists are different");

    }

    @Test
    public void deleteContactApproachTwo() throws IOException {
        String filename = "contactDataFile.ser";
        MainPage mainPage = new MainPage(getDriver());
        LoginPage lpage = mainPage.openTopMenu(TopMenuItem.LOGIN.toString());
        lpage.fillEmailField(PropertiesReader.getProperty("existingUserEmail"))
                .fillPasswordField(PropertiesReader.getProperty("existingUserPassword"))
                .clickByLoginButton();
        MainPage.openTopMenu(TopMenuItem.ADD.toString());
        AddPage addPage = new AddPage(getDriver());
        Contact newContact = new Contact(NameAndLastNameGenerator.generateName(),NameAndLastNameGenerator.generateLastName(),
                PhoneNumberGenerator.generatePhoneNumber(),
                EmailGenerator.generateEmail(10,5,3),
                AddressGenerator.generateAddress(), "Test description");
        addPage.fillFormAndSave(newContact);
        Contact.serializeContact(newContact, filename);
        ContactsPage contactsPage = new ContactsPage(getDriver());
        Contact deserContact = Contact.desiarializeContact(filename);
        Assert.assertNotEquals(contactsPage.deleteContactByPhoneNumberOrName(deserContact.getPhone()),
                contactsPage.getContactsListSize());

    }

    // проверить что Пользователь с уже существующими данными пытается зарегистрироваться
// зарегистрировались
//вышли
//еще раз пытаемся зарегистрироваться

    //Пользователь зарегистрирован, не может пройти регистрацию повторно с теми же данными
    // Шаг 1: Пользователь переходит на страницу LOGIN и регистрируется с валидными данными
    // Шаг 2: Пользователь выходит из системы
    // ContactsPage -> find Sing out button-> clickSignOutButton
    // Шаг 3: Пользователь регистрируется с теми же данными
    // Ожидаемый результат: система выдает сообщение: "User already exist"

     @Test
   public static void registrationOfAnAlreadyRegisteredUser() throws InterruptedException {
        Allure.description("User already exists test.");
        Allure.step("Step 1");
         WebDriver driver = new FirefoxDriver();
         MainPage mainPage = new MainPage(driver);
         LoginPage lpage = mainPage.openTopMenu(TopMenuItem.LOGIN.toString());
             lpage.fillEmailField(PropertiesReader.getProperty("testUserEmail"))
             .fillPasswordField(PropertiesReader.getProperty("testUserPassword"))
             .clickByRegistrationButton();
         Allure.step("Step 2");
         ContactsPage contactsPage = new ContactsPage(driver);
         contactsPage.clickSignOutButton();
         Allure.step("Step 3");
          mainPage.openTopMenu(TopMenuItem.LOGIN.toString());
          lpage.fillEmailField(PropertiesReader.getProperty("testUserEmail"))
                 .fillPasswordField(PropertiesReader.getProperty("testUserPassword"))
                 .clickByRegistrationButton();
         String expectedString = "User already exist";
         Alert alert = driver.switchTo().alert();
         String actualAlertText = alert.getText();
         boolean isAlertHandler = actualAlertText.contains(expectedString);
         Assert.assertTrue(isAlertHandler);
         TakeScreen.takeScreenshot("Already registered User can not be registered");
         Thread.sleep(3000);
         driver.quit();

   }
}


