package functional;

import com.sun.istack.internal.NotNull;
import server.*;
import org.junit.*;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import static junit.framework.Assert.*;

public class AuthTest {
    AccountService accountService;
    Thread gameThread;
    GameServer server;

    String testLogin = "testLogin";

    @Before
    public void setUp() throws Exception {
        server = new GameServer(8081, DatabaseService.DB.H2);
        gameThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    server.start();
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        gameThread.start();
    }

    @After
    public void tearDown() throws Exception {
        server.stop();
    }

    @Test
    public void testLoginOk() throws Exception {
        DatabaseService service = new DatabaseService(DatabaseService.DB.H2);
        accountService = new AccountService(service);
        accountService.tryRegister(testLogin, testLogin, testLogin);

        assertTrue(testLogin(testLogin));
    }

    @Test
    public void testLoginFailed() throws Exception {
        assertFalse(testLogin("trololo"));
    }

    public boolean testLogin(final String login) throws Exception {
        WebDriver driver = new HtmlUnitDriver(false);
        driver.get("http://127.0.0.1:8081/index");

        WebElement loginField = driver.findElement(By.name("login"));
        loginField.sendKeys(login);
        WebElement passwordField = driver.findElement(By.name("password"));
        passwordField.sendKeys(login);
        WebElement submitButton = driver.findElement(By.name("submit"));
        submitButton.submit();

        boolean result = false;

        try {
            result = (new WebDriverWait(driver, 10))
                    .until(new ExpectedCondition<Boolean>() {
                        @Override
                        @NotNull
                        public Boolean apply(@NotNull WebDriver d) {
                            WebElement el;
                            try {
                                el = d.findElement(By.id("login"));
                                return el.getText().contains(login);
                            } catch (NoSuchElementException e) {
                                return false;
                            }
                        }
                    });
        } catch (Exception e) {
            result = false;
        }

        driver.quit();
        return result;
    }
}
