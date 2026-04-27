package ui.pages;

import ui.helpers.WaitHelper;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

public abstract class BasePage {

    protected WebDriver driver;
    protected WaitHelper waitHelper;

    public BasePage(WebDriver driver) {
        this.driver = driver;
        this.waitHelper = new WaitHelper(driver);
        PageFactory.initElements(driver, this);
    }
}
