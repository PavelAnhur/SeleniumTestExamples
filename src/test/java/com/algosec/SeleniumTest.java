package com.algosec;

import io.github.bonigarcia.wdm.WebDriverManager;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.Map;

@Slf4j
public class SeleniumTest {
    private WebDriver driver;
    private Actions actions;
    private WebDriverWait wait;

    @BeforeClass
    public void beforeSeleniumTest() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        actions = new Actions(driver);
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @Test(description = "computer creating test")
    public void seleniumTest1() {
        //implicit wait example
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));

        driver.get("https://computer-database.gatling.io/computers");

        driver.findElement(By.id("add")).click();
        driver.findElement(By.id("name")).sendKeys("Test Computer Name");
        driver.findElement(By.id("introduced")).sendKeys("2021-01-10");
        driver.findElement(By.id("discontinued")).sendKeys("2022-01-10");
        driver.findElement(By.id("company")).click();
        driver.findElement(By.xpath("//option[@value='36']")).click();
        driver.findElement(By.className("primary")).click();

        String getComputerName = driver.findElement(By.xpath("//*[@class='alert-message warning']")).getText();

        Assert.assertTrue(getComputerName.contains("Computer Test Computer Name has been created"),
                "Test Failed: Computer not created");
        log.info("Test Passes successfully: Computer created");
    }

    @Test(description = "drag and drop test")
    public void seleniumTest2() {
        driver.get("http://demo.guru99.com/test/drag_drop.html");

        Map<String, String> fromToMap = Map.of(
                "bank", "credit2",
                "amt7", "fourth",
                "loan", "credit1",
                "amt8", "fourth"
        );

        acceptCookies();

        fromToMap.forEach((to, from) -> dragAndDrop(from, to));

        Assert.assertTrue(driver.findElement(By.xpath("//*[@class='button button-green']")).isDisplayed(),
                "Drag and drop operation failed");
    }

    @Test(description = "upload file test")
    public void seleniumTest3() {
        driver.get("http://demo.guru99.com/test/upload/");

        acceptCookies();

        //explicit wait example
        wait.until(ExpectedConditions.elementToBeClickable(By.id("uploadfile_0")));
        WebElement uploadElement = driver.findElement(By.id("uploadfile_0"));

        uploadElement.sendKeys("C:\\Users\\pavel.anhur\\Documents\\New Text Document.txt");
        driver.findElement(By.id("terms")).click();
        driver.findElement(By.name("send")).click();

        By confirmUploadFileElementXpath = By.xpath("//h3//*[text()[contains(.,'has been successfully uploaded.')]]");
        wait.until(
                ExpectedConditions.textToBePresentInElementLocated(
                        confirmUploadFileElementXpath,
                        "has been successfully uploaded."));
        WebElement confirmUploadFileElement =
                driver.findElement(confirmUploadFileElementXpath);
        Assert.assertTrue(confirmUploadFileElement.isDisplayed(), "Upload file failed");
    }

    private void acceptCookies() {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//iframe[@id='gdpr-consent-notice']")));
            driver.switchTo().frame("gdpr-consent-notice");

            By acceptCookiesLocator = By.id("save");
            wait.until(ExpectedConditions.elementToBeClickable(acceptCookiesLocator));
            driver.findElement(acceptCookiesLocator).click();
        } catch (TimeoutException e) {
            log.error(e.getMessage());
        }
    }

    private void dragAndDrop(String elementFromLocatorPart, String elementToLocatorPart) {
        WebElement elementFrom = driver.findElement(By.xpath(String.format("//*[@id='%s']/a", elementFromLocatorPart)));
        WebElement elementTo = driver.findElement(By.xpath(String.format("//*[@id='%s']/li", elementToLocatorPart)));

        actions.dragAndDrop(elementFrom, elementTo)
                .build()
                .perform();
    }

    @AfterClass
    public void afterSeleniumTest() {
        driver.quit();
    }
}
