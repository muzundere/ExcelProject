package page;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class OrderPage {
    public  OrderPage(WebDriver driver){
        PageFactory.initElements(driver,this);
    }

    @FindBy(linkText = "Order")
    public  WebElement orderLink;

    @FindBy(id="ctl00_MainContent_fmwOrder_ddlProduct")
    public WebElement dropDown;

    @FindBy(id = "ctl00_MainContent_fmwOrder_txtQuantity")
    public WebElement quantity;

    @FindBy(id="ctl00_MainContent_fmwOrder_txtUnitPrice")
    public  WebElement pricePerUnit;

    @FindBy(id="ctl00_MainContent_fmwOrder_txtDiscount")
    public  WebElement discount;

    @FindBy (id = "ctl00_MainContent_fmwOrder_txtTotal")
    public  WebElement total;

    @FindBy(xpath = "//input[@value='Calculate']")
    public WebElement calculateButton;

    @FindBy(id = "ctl00_MainContent_fmwOrder_txtName")
    public  WebElement customerName;

    @FindBy(id = "ctl00_MainContent_fmwOrder_TextBox2")
    public  WebElement street;

    @FindBy(id = "ctl00_MainContent_fmwOrder_TextBox3")
    public  WebElement city;

    @FindBy(id = "ctl00_MainContent_fmwOrder_TextBox4")
    public  WebElement state;

    @FindBy(id = "ctl00_MainContent_fmwOrder_TextBox5")
    public  WebElement zip;

    @FindBy(id = "ctl00_MainContent_fmwOrder_TextBox6")
    public  WebElement cardNr;

    @FindBy(id = "ctl00_MainContent_fmwOrder_TextBox1")
    public  WebElement expireDate;

    @FindBy (id="ctl00_MainContent_fmwOrder_InsertButton")
    public  WebElement processButton;

    @FindBy (xpath = "//input[@value='Reset']")
    public  WebElement resetButton;
}