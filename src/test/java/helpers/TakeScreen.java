package helpers;

import io.qameta.allure.Attachment;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import static config.BaseTest.getDriver;

public class TakeScreen {
    @Attachment(value = "Fallure screenshot", type = "image/png")
    public static byte[] takeScreenshot(String testName) {

        try {
            String screenshotName = testName + "_" + System.currentTimeMillis() + ".png";
            // создание уникального имени скриншота
            File screenshotFile = ((TakesScreenshot) getDriver()).getScreenshotAs(OutputType.FILE);
            // получаем скриншот состояния страницы и сохраняется в виду страницы
            FileUtils.copyFile(screenshotFile, new File("screenshots\\" + screenshotName));
            // копирует файл скриншота в директорию проекта
            return Files.readAllBytes(Paths.get("screenshots\\" + screenshotName));
            // возвращается содержимое скриншота в виде масива байтов
        } catch (IOException e) { // если произошел сбой при сохранении файла
            return null;
        }
    }
}
