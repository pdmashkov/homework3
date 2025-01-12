package ru.netology;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RequestCardTest {
    private WebDriver driver;

    @BeforeAll
    static void setUpAll() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    void setUp() {
        ChromeOptions options = new ChromeOptions();

        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--no-sandbox");
        options.addArguments("--headless");

        driver = new ChromeDriver(options);
        driver.get("http://localhost:9999");
    }

    @AfterEach
    void tearDown() {
        driver.quit();
        driver = null;
    }

    @Test
    void shouldSendFormSuccess() {
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Иванов-Олег Алексеевич");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+70009181717");
        driver.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        driver.findElement(By.cssSelector("[type='button']")).click();

        WebElement resultElement = driver.findElement(By.cssSelector("[data-test-id='order-success']"));
        assertTrue(resultElement.isDisplayed());

        assertEquals("  Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время.", resultElement.getText());
    }

    @Test
    void shouldFailedWithoutName() {
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+70009181717");
        driver.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        driver.findElement(By.cssSelector("[type='button']")).click();

        WebElement errorElementName = driver.findElement(By.cssSelector("[data-test-id='name'].input_invalid .input__sub"));
        assertTrue(errorElementName.isDisplayed());

        assertEquals("Поле обязательно для заполнения", errorElementName.getText());
    }

    @Test
    void shouldFailedWithoutPhone() {
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Иванов-Олег Алексеевич");
        driver.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        driver.findElement(By.cssSelector("[type='button']")).click();

        WebElement errorElementPhone = driver.findElement(By.cssSelector("[data-test-id='phone'].input_invalid .input__sub"));
        assertTrue(errorElementPhone.isDisplayed());

        assertEquals("Поле обязательно для заполнения", errorElementPhone.getText());
    }

    @Test
    void shouldFailedWithBadName() {
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Ivanov Oleg");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+70009188765");
        driver.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        driver.findElement(By.cssSelector("[type='button']")).click();

        WebElement errorElementName = driver.findElement(By.cssSelector("[data-test-id='name'].input_invalid .input__sub"));
        assertTrue(errorElementName.isDisplayed());

        assertEquals("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.", errorElementName.getText());
    }

    @Test
    void shouldFailedWithBadPhone() {
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Олег");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+70009181717123");
        driver.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        driver.findElement(By.cssSelector("[type='button']")).click();

        WebElement errorElementPhone = driver.findElement(By.cssSelector("[data-test-id='phone'].input_invalid .input__sub"));
        assertTrue(errorElementPhone.isDisplayed());

        assertEquals("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.", errorElementPhone.getText());
    }

    @Test
    void shouldFailedWithoutAgreement() {
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Иванов-Олег Алексеевич");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+70009181717");
        driver.findElement(By.cssSelector("[type='button']")).click();

        WebElement errorElement = driver.findElement(By.cssSelector("[data-test-id='agreement'].input_invalid"));
        assertTrue(errorElement.isDisplayed());
    }
}
