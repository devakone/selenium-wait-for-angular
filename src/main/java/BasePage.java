import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;


public class BasePage {
    private static String angularScript;
    private static String resetScript;
    protected static WebDriver driver;

    private static Long SELENIUM_WAIT_TIMEOUT = new Long(120);
    private static Long SELENIUM_WAIT_STEP = new Long(200);

/*    @BeforeMethod
    public void setup() throws InterruptedException {
        System.setProperty("webdriver.chrome.driver",
                "/usr/local/bin/chromedriver");
        // initializing driver variable using ChromeDriver
        String url = "https://localhost:4204";
        driver=new ChromeDriver();
        driver.get(url);
        // maximized the browser window
        driver.manage().window().maximize();
        // drv.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
    }*/


    public static void main(String[] args){
        System.setProperty("webdriver.chrome.driver",
                "/usr/local/bin/chromedriver");
        ChromeOptions options = new ChromeOptions();
        options.addArguments("test-type");
        options.addArguments("ignore-certificate-errors");
        options.setAcceptInsecureCerts(true);
        String url = "https://localhost:4204";
        System.out.println("To load page: " + url);
        driver=new ChromeDriver(options);
        driver.get(url);
        waitForPageToLoad();
        // driver.quit();

    }

    static {
        try {
            ClassLoader classLoader = BasePage.class.getClassLoader();
            // Getting resource(File) from class loader
            File angularFile=new File(classLoader.getResource("js/check.js").getFile());
            FileInputStream fileStream = new FileInputStream(angularFile);
            StringBuilder sb = new StringBuilder();
            int ch;
            while ((ch = fileStream.read()) != -1) {
                sb.append((char) ch);
            }
            angularScript = sb.toString();

            File resetFile=new File(classLoader.getResource("js/reset.js").getFile());
            fileStream = new FileInputStream(resetFile);
            sb = new StringBuilder();
            while ((ch = fileStream.read()) != -1) {
                sb.append((char) ch);
            }
            resetScript = sb.toString();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected static void waitForPageToLoad() {

        Date d1 = new Date();
        WebDriverWait wait = new WebDriverWait(driver, SELENIUM_WAIT_TIMEOUT, SELENIUM_WAIT_STEP);
        ((JavascriptExecutor) driver).executeScript(resetScript);

        System.out.println("Waiting for page to load.");
        wait.until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver driver) {
                Object ret = ((JavascriptExecutor) driver).executeScript(angularScript);
                if (ret == null) {
                    return true;
                }

                System.out.println("Page not loaded yet. (cause: " + ret + ").");
                return false;
            }
        });
        URL url = null;
        try {
            url = new URL(driver.getCurrentUrl());
        } catch (MalformedURLException e) {
            System.out.println("Malformed URL Exception detection");
        }

        Date d2 = new Date();
        System.out.println(String.format("Page loaded in %s millis for %s.", d2.getTime() - d1.getTime(), url.getPath()));
    }

/*    @AfterMethod
    public void teardown() {
        // closes all the browser windows opened by web driver
        drv.quit();
    }*/

}
