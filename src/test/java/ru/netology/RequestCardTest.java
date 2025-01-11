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
    }

    @AfterEach
    void tearDown() {
        driver.quit();
        driver = null;
    }

    @Test
    void shouldSendForm() {
        driver.get("http://localhost:9999");

        driver.findElement(By.cssSelector("[data-test-id='name']")).findElement(By.cssSelector("input")).sendKeys("Иванов Олег");
        driver.findElement(By.cssSelector("[data-test-id='phone']")).findElement(By.cssSelector("input")).sendKeys("+70009181717");
        driver.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        driver.findElement(By.cssSelector("[type='button']")).click();

        WebElement resultElement = driver.findElement(By.cssSelector("[data-test-id='order-success']"));
        assertTrue(resultElement.isDisplayed());

        assertEquals("  Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время.", resultElement.getText());
    }

    @Test
    void shouldFailedWithoutName() {
        driver.get("http://localhost:9999");

        driver.findElement(By.cssSelector("[data-test-id='phone']")).findElement(By.cssSelector("input")).sendKeys("+70009181717");
        driver.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        driver.findElement(By.cssSelector("[type='button']")).click();

        WebElement errorElement = driver.findElement(By.cssSelector("[data-test-id='name'")).findElement(By.cssSelector("[class='input__sub']"));
        assertTrue(errorElement.isDisplayed());

        assertEquals("Поле обязательно для заполнения", errorElement.getText());
    }

    @Test
    void shouldFailedWithBadPhone() {
        driver.get("http://localhost:9999");

        driver.findElement(By.cssSelector("[data-test-id='name']")).findElement(By.cssSelector("input")).sendKeys("Иванов Олег");
        driver.findElement(By.cssSelector("[data-test-id='phone']")).findElement(By.cssSelector("input")).sendKeys("+70009181717123");
        driver.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        driver.findElement(By.cssSelector("[type='button']")).click();

        WebElement errorElement = driver.findElement(By.cssSelector("[data-test-id='phone'")).findElement(By.cssSelector("[class='input__sub']"));
        assertTrue(errorElement.isDisplayed());

        assertEquals("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.", errorElement.getText());
    }
}
