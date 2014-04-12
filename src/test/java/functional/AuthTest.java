package functional;

import messaging.AddressService;
import messaging.MessageSystem;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import server.AccountService;
import server.DatabaseService;
import server.GameServer;
import server.H2DatabaseService;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AuthTest {
    AccountService accountService;
    Thread gameThread;
    GameServer server;

    String testLogin = "testLogin";

    @Before
    public void setUp() throws Exception {
        DatabaseService service = new H2DatabaseService();

        MessageSystem ms = mock(MessageSystem.class);
        AddressService as = mock(AddressService.class);
        when(ms.getAddressService()).thenReturn(as);

        accountService = new AccountService(ms, service);
        accountService.tryRegister(testLogin, testLogin, testLogin);

        server = new GameServer(8081, service);
        gameThread = new Thread(() -> {
                try {
                    server.start();
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        );

        gameThread.start();
    }

    @After
    public void tearDown() throws Exception {
        server.stop();
    }

    @Test
    public void testLoginOk() throws Exception {
        assertTrue(testLogin(testLogin));
    }

    @Test
    public void testLoginFailed() throws Exception {
        assertFalse(testLogin("trololo"));
    }

    public boolean testLogin(final String login) throws Exception {
        WebDriver driver = new FirefoxDriver();
        driver.get("http://127.0.0.1:8081/index");

        WebElement loginField = driver.findElement(By.name("login"));
        loginField.sendKeys(login);
        WebElement passwordField = driver.findElement(By.name("password"));
        passwordField.sendKeys(login);
        WebElement submitButton = driver.findElement(By.name("submit"));
        submitButton.submit();

        boolean result = false;

        try {
            (new WebDriverWait(driver, 10))
                    .until((WebDriver d) -> {
                        WebElement el;
                        try {
                            el = d.findElement(By.id("login"));
                            return el.getText().contains(login);
                        } catch (NoSuchElementException e) {
                            return false;
                        }
                    });
            result = true;
        } catch (Exception e) {
            result = false;
        }

        driver.quit();
        return result;
    }
}
