package functional;

import messaging.AddressService;
import messaging.MessageSystem;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import resourcing.ResourceSystem;
import server.AccountService;
import database.DatabaseService;
import server.GameServer;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AuthTest {
    AccountService accountService;
    Thread gameThread;
    GameServer server;

    String testLoginStr = "testLoginStr";
    String testRegisterStr = "testRegisterStr";
    String port = "8081";

    @Before
    public void setUp() throws Exception {
        ResourceSystem rs = ResourceSystem.getInstance();
        DatabaseService service = new DatabaseService(rs.getConfig("h2"));

        MessageSystem ms = mock(MessageSystem.class);
        AddressService as = mock(AddressService.class);
        when(ms.getAddressService()).thenReturn(as);

        accountService = new AccountService(ms, service);
        accountService.tryRegister(testLoginStr, testLoginStr, testLoginStr);

        port = rs.getConfig("server").get("port");
        server = new GameServer(Integer.parseInt(port), service);
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
        assertTrue(testLogin(testLoginStr));
    }

    @Test
    public void testLoginFailed() throws Exception {
        assertFalse(testLogin("trololo"));
    }

    @Test
    public void testRegisterOk() throws Exception {
        assertTrue(testRegister(testRegisterStr));
    }

    @Test
    public void testRegisterFailed() throws Exception {
        testRegister(testRegisterStr);
        assertFalse(testRegister(testRegisterStr));
    }

    public boolean testLogin(final String login) throws Exception {
        WebDriver driver = new FirefoxDriver();
        driver.get("http://127.0.0.1:" + port + "/index");

        WebElement loginField = driver.findElement(By.name("login"));
        loginField.sendKeys(login);
        WebElement passwordField = driver.findElement(By.name("password"));
        passwordField.sendKeys(login);
        WebElement submitButton = driver.findElement(By.name("submit"));
        submitButton.submit();

        return testAuth(driver, login);
    }

    public boolean testRegister(final String login) throws Exception {
        WebDriver driver = new FirefoxDriver();
        driver.get("http://127.0.0.1:" + port + "/registration");

        WebElement loginField = driver.findElement(By.name("login"));
        loginField.sendKeys(login);
        WebElement passwordField = driver.findElement(By.name("password"));
        passwordField.sendKeys(login);
        WebElement emailField = driver.findElement(By.name("email"));
        emailField.sendKeys(login);
        WebElement submitButton = driver.findElement(By.name("submit"));
        submitButton.submit();

        return testAuth(driver, login);
    }

    public boolean testAuth(WebDriver driver, String login) {
        boolean result;

        try {
            (new WebDriverWait(driver, 10))
                    .until((WebDriver d) -> {
                        try {
                            return testElementValue(d, "login", login);
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

    private boolean testElementValue(WebDriver d, String element, String value) {
        while (true) {
            try {
                WebElement el = d.findElement(By.id(element));
                return el.getText().contains(value);
            }
            catch(StaleElementReferenceException ignore) { }
        }
    }
}
