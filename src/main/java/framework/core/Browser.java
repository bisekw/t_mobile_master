package framework.core;

import org.openqa.selenium.WebDriver;

public class Browser {


    private WebDriver _driver;

    public Browser(WebDriver driver) {
        _driver = driver;
    }

    public DriverType type;



    public void maximize()
    {
        _driver.manage().window().maximize();
    }
}
