package framework.core;

import framework.config.Settings;
import io.appium.java_client.MobileBy;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.touch.WaitOptions;
import io.appium.java_client.touch.offset.PointOption;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.interactions.MoveTargetOutOfBoundsException;
import org.openqa.selenium.json.JsonException;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.SkipException;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;
import java.util.HashMap;
import java.util.Objects;
import java.util.Set;

@Slf4j
public class WebElementHelper {

    static Duration defaultDuration = Duration.ofSeconds(30);

    public static void GoToUrl(String url) {
        log.info("GoToUrl");
        DriverContext.getRemoteWebDriver().get(url);
    }

    public static void QuitDriver() {
        if (null != DriverContext.getRemoteWebDriver()) {
            log.info("QUIT DRIVER");
            DriverContext.getRemoteWebDriver().quit();
        }
    }

    public static void CloseDriver() {
        if (null != DriverContext.getRemoteWebDriver()) {
            log.info("CLOSE DRIVER");
            DriverContext.getRemoteWebDriver().close();
        }
    }


    public static void MaximizeWindow() {
        if (Settings.DRIVER_TYPE.equals(DriverType.CHROME) || Settings.DRIVER_TYPE.equals(DriverType.FIREFOX)) {
            DriverContext.getRemoteWebDriver().manage().window().maximize();
        }
    }


    public static void WaitForPageToLoad() {
        var wait = new WebDriverWait(DriverContext.getRemoteWebDriver(), defaultDuration);
        var jsExecutor = DriverContext.getRemoteWebDriver();

        ExpectedCondition<Boolean> jsLoad = webDriver -> (DriverContext.getRemoteWebDriver())
                .executeScript("return document.readyState").toString().equals("complete");

        //Get JS Ready
        boolean jsReady = jsExecutor.executeScript("return document.readyState").toString().equals("complete");

        if (!jsReady)
            wait.until(jsLoad);
        else
            log.info("Page is ready !");

    }


    public static void WaitForElementVisible(final WebElement elementFindBy) {
//        for (int i = 0; i < 11; i++) {
//            try {
        var wait = new WebDriverWait(Objects.requireNonNull(DriverContext.getRemoteWebDriver()), defaultDuration);
        wait.until(ExpectedConditions.visibilityOf(elementFindBy));
//            } catch (NoSuchElementException | TimeoutException | StaleElementReferenceException | JsonException e) {
//                if (i == 10) {
//                    return;
//                }
//            }
//        }
    }

    public static void WaitForElementVisible(final WebElement elementFindBy, Duration duration) {
        WebDriverWait wait = new WebDriverWait(Objects.requireNonNull(DriverContext.getRemoteWebDriver()), duration);
        wait.until(ExpectedConditions.visibilityOf(elementFindBy));
    }

    public static void WaitForElementTextVisible(final WebElement elementFindBy, String text) {
        WebDriverWait wait = new WebDriverWait(DriverContext.getRemoteWebDriver(), defaultDuration);
        wait.until(ExpectedConditions.textToBePresentInElement(elementFindBy, text));
    }

    public static void WaitForElementTextVisible(final WebElement elementFindBy, String text, Duration duration) {
        WebDriverWait wait = new WebDriverWait(DriverContext.getRemoteWebDriver(), duration);
        wait.until(ExpectedConditions.textToBePresentInElement(elementFindBy, text));
    }

    public static void WaitUntilTextDisplayed(final By element, String text) {
        WebDriverWait wait = new WebDriverWait(DriverContext.getRemoteWebDriver(), defaultDuration);
        wait.until(TextDisplayed(element, text));
    }

    public static void WaitUntilTextDisplayed(final By element, String text, Duration duration) {
        WebDriverWait wait = new WebDriverWait(DriverContext.getRemoteWebDriver(), duration);
        wait.until(TextDisplayed(element, text));
    }

    private static ExpectedCondition<Boolean> TextDisplayed(final By elementFindBy, final String text) {
        return webDriver -> webDriver.findElement(elementFindBy).getText().contains(text);
    }

    public static void WaitElementEnabled(final By elementFindBy) {
        WebDriverWait wait = new WebDriverWait(DriverContext.getRemoteWebDriver(), defaultDuration);
        wait.until(webDriver -> webDriver.findElement(elementFindBy).isEnabled());
    }

    public static void WaitForElementClickable(WebElement element) {
        WebDriverWait wait = new WebDriverWait(DriverContext.getRemoteWebDriver(), defaultDuration);
        wait.until(ExpectedConditions.elementToBeClickable(element));
    }

    public static void WaitForElementClickable(WebElement element, Duration duration) {
        WebDriverWait wait = new WebDriverWait(DriverContext.getRemoteWebDriver(), duration);
        wait.until(ExpectedConditions.elementToBeClickable(element));
    }

    public static boolean ElementIsVisible(final WebElement elementFindBy) {
        try {
            //    Thread.sleep(1000);
            WebDriverWait wait = new WebDriverWait(DriverContext.getRemoteWebDriver(), defaultDuration);
            wait.pollingEvery(Duration.ofMillis(100));
            wait.until(ExpectedConditions.visibilityOf(elementFindBy));
            return true;
        } catch (NoSuchElementException | TimeoutException | StaleElementReferenceException |
                 JsonException e) {
            return false;
        }
    }

    public static boolean ElementIsVisible(final WebElement elementFindBy, Duration duration) {
        try {
            WebDriverWait wait = new WebDriverWait(DriverContext.getRemoteWebDriver(), duration);
            wait.pollingEvery(Duration.ofMillis(100));
            wait.until(ExpectedConditions.visibilityOf(elementFindBy));
            return true;
        } catch (NoSuchElementException | TimeoutException | StaleElementReferenceException | JsonException e) {
            return false;
        }
    }

    public static void SwitchToFrameById(int i) {
        DriverContext.getRemoteWebDriver().switchTo().frame(i);
    }

    public static void SwitchToFrameByWebElement(final WebElement elementFindBy) {
        DriverContext.getRemoteWebDriver().switchTo().frame(elementFindBy);
    }

    public static void SwitchToDefaultFrame() {
        DriverContext.getRemoteWebDriver().switchTo().defaultContent();
    }

    public static void ShutDownBrowser() {
        try {
            if ((Settings.DRIVER_TYPE.equals(DriverType.CHROME) || Settings.DRIVER_TYPE.equals(DriverType.FIREFOX))) {
                DriverContext.getRemoteWebDriver().quit();
            } else if ((Settings.DRIVER_TYPE.equals(DriverType.ANDROID_APP) || Settings.DRIVER_TYPE.equals(DriverType.IOS_APP))) {
                DriverContext.getRemoteWebDriver().close();
                DriverContext.getRemoteWebDriver().quit();
            }
        } catch (NoSuchSessionException | NullPointerException e) {
            log.info("Methode shutDownBrowser not run");
        }
    }

    public static String GetCurrentUrl() {
        return DriverContext.getRemoteWebDriver().getCurrentUrl();
    }

    @SneakyThrows
    public static void WaitByUrl(String urlContains) {
        boolean contains = false;
        String url;
        int attempts = 0;
        do {
            attempts++;
            url = GetCurrentUrl();
            Thread.sleep(10);

            if (url.contains(urlContains)) {
                contains = true;
            } else if (attempts > 50) {
                break;
            }
        } while (!contains);
    }

    @SneakyThrows
    public static boolean urlContains(String urlContains) {
        boolean contains = false;
        String url;
        int attempts = 0;
        do {
            attempts++;
            url = GetCurrentUrl();
            Thread.sleep(10);

            if (url.contains(urlContains)) {
                contains = true;
            } else if (attempts > 50) {
                break;
            }
        } while (!contains);

        return contains;
    }

    @SneakyThrows
    public static boolean UrlContains(String urlContains) {
        boolean contains = false;
        String url;
        int attempts = 0;
        do {
            attempts++;
            url = GetCurrentUrl();
            Thread.sleep(10);

            if (url.contains(urlContains)) {
                contains = true;
            } else if (attempts > 50) {
                break;
            }
        } while (!contains);
        return contains;
    }

    public static void WaitByUrlNotContains(String urlContains) throws InterruptedException {
        boolean contains = false;
        String url;
        int attempts = 0;
        do {
            attempts++;
            url = GetCurrentUrl();
            Thread.sleep(10);

            if (!url.contains(urlContains)) {
                contains = true;
            } else if (attempts > 50) {
                break;
            }
        } while (!contains);
    }

    public static boolean UrlNotContains(String urlContains) {
        boolean contains = false;
        String url;
        int attempts = 0;
        do {
            attempts++;
            url = GetCurrentUrl();
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            if (!url.contains(urlContains)) {
                contains = true;
            } else if (attempts > 50) {
                break;
            }
        } while (!contains);

        return contains;
    }

    public static void RefreshPage() {
        Objects.requireNonNull(DriverContext.getRemoteWebDriver()).navigate().refresh();
    }

    public static void RefreshPageByJs() {
        JavascriptExecutor jsExecutor = (JavascriptExecutor) Objects.requireNonNull(DriverContext.getRemoteWebDriver());
        jsExecutor.executeScript("location.reload();");
    }

    public static void StopLoading() {
        JavascriptExecutor jsExecutor = (JavascriptExecutor) Objects.requireNonNull(DriverContext.getRemoteWebDriver());
        jsExecutor.executeScript("window.stop();");
    }

    public static void MoveMouseToElement(final WebElement element) throws InterruptedException {
        boolean isElementVisible = false;
        Actions actions = new Actions(Objects.requireNonNull(DriverContext.getRemoteWebDriver()));

        while (!isElementVisible) {
            try {
                WaitForElementVisible(element);
                actions.moveToElement(element).perform();
                isElementVisible = true;
            } catch (StaleElementReferenceException | MoveTargetOutOfBoundsException e) {
                Thread.sleep(3000);
            }
        }
    }

    public static void MoveMouseToElementAndClick(final WebElement element) throws InterruptedException {
        boolean isElementVisible = false;
        Actions actions = new Actions(DriverContext.getRemoteWebDriver());

        while (!isElementVisible) {
            try {
                WaitForElementVisible(element);
                actions.moveToElement(element).perform();
                actions.moveToElement(element).click().perform();
                isElementVisible = true;
            } catch (StaleElementReferenceException | MoveTargetOutOfBoundsException e) {
                Thread.sleep(3000);
            }
        }
    }

    public static void RetryingClick(final WebElement element) {
        int attemps = 0;
        while (attemps < 10) {
            try {
                element.click();
                break;
            } catch (StaleElementReferenceException | ElementClickInterceptedException | NoSuchElementException e) {
                log.info("Error on RetryingClick methode");
                attemps++;
            }
        }
    }

    public static void MarkScenarioAsSkipped(String text) {
        throw new SkipException(text);
    }

    public static void MarkScenarioAsFail(String text) {
        Assert.fail(text);
    }

    public static void OpenNewTab() {
        DriverContext.getRemoteWebDriver().executeScript("window.open()");
    }

    public static void SwitchToNewTab() {
        String currentTab = DriverContext.getRemoteWebDriver().getWindowHandle();
        Set<String> windows = DriverContext.getRemoteWebDriver().getWindowHandles();

        for (String tab : windows) {
            if (!tab.equals(currentTab)) {
                DriverContext.getRemoteWebDriver().switchTo().window(tab);
            }
        }
    }

    public static boolean ScrollToElement(WebElement webElement) {
        try {
            JavascriptExecutor javascriptExecutor = DriverContext.getRemoteWebDriver();
            assert javascriptExecutor != null;
            javascriptExecutor.executeScript("arguments[0].scrollIntoView(true);", webElement);
            return true;
        } catch (StaleElementReferenceException e) {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
            RefreshPage();
            WaitForPageToLoad();
            return false;
        }
    }

    public static void AndroidScrollToElementByText(String text) {
        try {
            DriverContext.getRemoteWebDriver()
                    .findElement(MobileBy.AndroidUIAutomator("new UiScrollable(new UiSelector().scrollable(true).instance(0))." +
                            "scrollIntoView(new UiSelector().textContains(\"" + text + "\").instance(0))"));
        } catch (Exception e) {
            throw new java.util.NoSuchElementException("Element tot find " + e);
        }
    }


    public static void swipeBottomToElement(WebElement element) {
        for (int i = 0; i < 10; i++) {
            if (ElementIsVisible(element)) {
                break;
            } else {
                Dimension dim = DriverContext.getRemoteWebDriver().manage().window().getSize();
                if (dim != null) {
                    int height = dim.getHeight();
                    int width = dim.getWidth();
                    int x = width / 2;
                    int topY = (int) (height * 0.50);
                    int bottomY = (int) (height * 0.20);
                    TouchAction ts = null;
                    if (Settings.DRIVER_TYPE.equals(DriverType.ANDROID_APP)) {
                        ts = new TouchAction((AndroidDriver) DriverContext.getRemoteWebDriver());
                    } else if (Settings.DRIVER_TYPE.equals(DriverType.IOS_APP)) {
                        ts = new TouchAction((IOSDriver) DriverContext.getRemoteWebDriver());
                    }
                    if (ts != null) {
                        ts.press(PointOption.point(x, topY)).moveTo(PointOption.point(x, bottomY))
                                .release()
                                .perform();
                    }
                }
            }
        }
    }

    public static void swipeToBottom() {
        for (int i = 0; i < 10; i++) {
            Dimension dim = DriverContext.getRemoteWebDriver().manage().window().getSize();
            if (dim != null) {
                int height = dim.getHeight();
                int width = dim.getWidth();
                int x = width / 2;
                int topY = (int) (height * 0.50);
                int bottomY = (int) (height * 0.20);
                TouchAction ts = null;
                if (Settings.DRIVER_TYPE.equals(DriverType.ANDROID_APP)) {
                    ts = new TouchAction((AndroidDriver) DriverContext.getRemoteWebDriver());
                } else if (Settings.DRIVER_TYPE.equals(DriverType.IOS_APP)) {
                    ts = new TouchAction((IOSDriver) DriverContext.getRemoteWebDriver());
                }
                if (ts != null) {
                    ts.press(PointOption.point(x, topY)).moveTo(PointOption.point(x, bottomY))
                            .release()
                            .perform();
                }
            }
        }
    }

    public static void swipeFromUpToBottom() {
        JavascriptExecutor javascriptExecutor = DriverContext.getRemoteWebDriver();
        HashMap<String, String> scrollObject = new HashMap<>();
        scrollObject.put("direction", "up");
        try {
            javascriptExecutor.executeScript("mobile: scroll", scrollObject);
            log.info("Swipe UP");
        } catch (Exception e) {
            log.info("Error on Swipe UP " + e);
        }
    }

    public static void swipeFromBottomToUp() {
        JavascriptExecutor javascriptExecutor = DriverContext.getRemoteWebDriver();
        HashMap<String, String> scrollObject = new HashMap<>();
        scrollObject.put("direction", "down");
        try {
            javascriptExecutor.executeScript("mobile: scroll", scrollObject);
            log.info("Swipe DOWN");
        } catch (Exception e) {
            log.info("Error on Swipe DOWN " + e);
        }
    }

    public static void ScrollToBeginningByActions() {
        Actions actions = new Actions(DriverContext.getRemoteWebDriver());
        actions.sendKeys(Keys.HOME).build().perform();
    }

    public static void ScrollToENDByActions() {
        Actions actions = new Actions(DriverContext.getRemoteWebDriver());
        actions.sendKeys(Keys.END).build().perform();
    }

    public static void ClickByJavaScriptExecutor(WebElement element) {
        JavascriptExecutor javascriptExecutor = DriverContext.getRemoteWebDriver();
        javascriptExecutor.executeScript("var elem=argument[0]; setTimeout(function() {elem.click();} 100", element);
    }

    public static WebElement ReturnParentWebElementByJS(WebElement element) {
        JavascriptExecutor javascriptExecutor = DriverContext.getRemoteWebDriver();
        return (WebElement) javascriptExecutor.executeScript("return arguments[0].parentNode;", element);
    }

    public static void ClickByAction(WebElement element) {
        Actions actions = new Actions(DriverContext.getRemoteWebDriver());
        actions.moveToElement(element).click().build().perform();
    }

    public static void WaitForElementToByInvisibility(WebElement element) {
        if (ElementIsVisible(element)) {
            WebDriverWait wait = new WebDriverWait(DriverContext.getRemoteWebDriver(), defaultDuration);
            wait.pollingEvery(Duration.ofMillis(50));
            try {
                wait.until(ExpectedConditions.invisibilityOf(element));
            } catch (NoSuchElementException | TimeoutException e) {
                log.info("Error on invisibilityOf element" + e);
            }
        }
    }

    public static WebElement GetFromShadowRootByClassName(WebElement webElement, String myClassName) {
        JavascriptExecutor javascriptExecutor = DriverContext.getRemoteWebDriver();
        WebElement shadowRootElement = (WebElement) javascriptExecutor.executeScript("return arguments[0].shadowRoot", webElement);
        return shadowRootElement.findElement(By.className(myClassName));
    }

    public static WebElement GetFromShadowRootByCSS(WebElement webElement, String mycss) {
        JavascriptExecutor javascriptExecutor = DriverContext.getRemoteWebDriver();
        WebElement shadowRootElement = (WebElement) javascriptExecutor.executeScript("return arguments[0].shadowRoot", webElement);
        return shadowRootElement.findElement(By.cssSelector(mycss));
    }

    public static WebElement GetFromShadowRootById(WebElement webElement, String myId) {
        JavascriptExecutor javascriptExecutor = DriverContext.getRemoteWebDriver();
        WebElement shadowRootElement = (WebElement) javascriptExecutor.executeScript("return arguments[0].shadowRoot", webElement);
        return shadowRootElement.findElement(By.id(myId));
    }

    public static WebElement GetFromShadowRootByName(WebElement webElement, String name) {
        JavascriptExecutor javascriptExecutor = DriverContext.getRemoteWebDriver();
        WebElement shadowRootElement = (WebElement) javascriptExecutor.executeScript("return arguments[0].shadowRoot", webElement);
        return shadowRootElement.findElement(By.name(name));
    }

    public static WebElement GetFromShadowRootByXpath(WebElement webElement, String myXpath) {
        JavascriptExecutor javascriptExecutor = DriverContext.getRemoteWebDriver();
        WebElement shadowRootElement = (WebElement) javascriptExecutor.executeScript("return arguments[0].shadowRoot", webElement);
        return shadowRootElement.findElement(By.xpath(myXpath));
    }


    public static void SwipeScreen(Direction dir) {
        int ANIMATION_TIME = 200; //ms
        int PRES_TIME = 200;
        int edgeBorder = 10;
        PointOption pointOptionStart, pointOptionEnd;

        Dimension dim = DriverContext.getRemoteWebDriver().manage().window().getSize();

        pointOptionStart = PointOption.point(dim.width / 2, dim.height / 2);
        switch (dir) {
            case DOWN:
                pointOptionEnd = PointOption.point(dim.width / 2, dim.height - edgeBorder);
                break;
            case UP:
                pointOptionEnd = PointOption.point(dim.width / 2, edgeBorder);
                break;
            case LEFT:
                pointOptionEnd = PointOption.point(edgeBorder, dim.height / 2);
                break;
            case RIGHT:
                pointOptionEnd = PointOption.point(dim.width / 2 - edgeBorder, dim.height / 2);
                break;
            default:
                throw new IllegalArgumentException(dir + "Not supported");
        }

        try {
            new TouchAction((AndroidDriver) DriverContext.getRemoteWebDriver())
                    .press(pointOptionStart)
                    .waitAction(WaitOptions.waitOptions(Duration.ofMillis(PRES_TIME)))
                    .moveTo(pointOptionEnd)
                    .release().perform();
        } catch (Exception e) {
            log.info("SwipeScreen methode Failed " + e.getMessage());
            return;
        }
    }

    public static void SetAttribute(String attribute, WebElement element, String attributeValue) {
        JavascriptExecutor jsExecutor = (JavascriptExecutor) DriverContext.getRemoteWebDriver();
        jsExecutor.executeScript("arguments[0].setAttribute('" + attribute + "', '" + attributeValue + "');", element);
    }

    public static void SwipeOnElementToDirection(WebElement element, Direction dir) {
        TouchAction touchAction = new TouchAction((AndroidDriver) DriverContext.getRemoteWebDriver());
        int centerX = element.getLocation().getX() + (element.getSize().getWidth() / 2);
        int centerY = element.getLocation().getY() + (element.getSize().getHeight() / 2);

        switch (dir) {
            case DOWN:
                touchAction.press(PointOption.point(centerX, centerY))
                        .waitAction(WaitOptions.waitOptions(Duration.ofMillis(200)))
                        .moveTo(PointOption.point(centerX, centerY - 200))
                        .release()
                        .perform();
                break;
            case UP:
                touchAction.press(PointOption.point(centerX, centerY))
                        .waitAction(WaitOptions.waitOptions(Duration.ofMillis(200)))
                        .moveTo(PointOption.point(centerX, centerY + 200))
                        .release()
                        .perform();
                break;
            case LEFT:
                touchAction.press(PointOption.point(centerX, centerY))
                        .waitAction(WaitOptions.waitOptions(Duration.ofMillis(200)))
                        .moveTo(PointOption.point(centerX - 200, centerY))
                        .release()
                        .perform();
                break;
            case RIGHT:
                touchAction.press(PointOption.point(centerX, centerY))
                        .waitAction(WaitOptions.waitOptions(Duration.ofMillis(200)))
                        .moveTo(PointOption.point(centerX + 200, centerY))
                        .release()
                        .perform();
                break;
        }
    }


    public enum Direction {
        UP,
        DOWN,
        LEFT,
        RIGHT
    }


    public static void SavePageSource() {
        String pageSource = DriverContext.getRemoteWebDriver().getPageSource();
        String currentPath = System.getProperty("user.dir");
        String filePath = currentPath + "/target/pageSource.xml";
        System.out.println("Aktualna ścieżka: " + filePath);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write(pageSource);
            log.info("Zapisano do pliku.");
        } catch (IOException e) {
            log.info("Błąd podczas zapisywania do pliku: " + e.getMessage());
        }
    }

    public static void PrintPageSource() {
        String pageSource = Objects.requireNonNull(DriverContext.getRemoteWebDriver()).getPageSource();
        log.info("PAGE SOURCE: \n" + pageSource);
    }

    public static void androidScrollToAnElementByText(String text) {
        DriverContext.getRemoteWebDriver().findElement(MobileBy.AndroidUIAutomator(
                "new UiScrollable(new UiSelector().scrollable(true).instance(0))"
                        + ".scrollIntoView(new UiSelector().text(\"" + text + "\").instance(0))"));
    }

    public static void androidScrollToAnElementByTextContains(String text) {
        try {
            DriverContext.getRemoteWebDriver().findElement(
                    MobileBy.AndroidUIAutomator("new UiScrollable(" +
                            "new UiSelector().scrollable(true).instance(0)).scrollIntoView(new UiSelector().textContains(\""
                            + text + "\").instance(0))"));
        } catch (Exception e) {
            throw new java.util.NoSuchElementException("No element" + e);
        }
    }
}


