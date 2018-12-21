package stepdefs;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import page.LoginPage;
import page.OrderPage;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Properties;

public class OrderTests {

    private WebDriver driver;
    private final String path = "config.properties";
    private final String excelFile = "positiveLoginData.xlsx";
    private Properties prop;
    LoginPage loginPage;
    OrderPage orderPage;
    InputStream excel;
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss");
    FileOutputStream out;
    SoftAssert soft;

    @BeforeClass
    public static void setupClass() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeMethod
    public void setupTest() throws IOException {
        driver = new ChromeDriver();
        loginPage = new LoginPage(driver);
        FileInputStream file = new FileInputStream(path);
        prop = new Properties();
        prop.load(file);
        orderPage = new OrderPage(driver);
        soft = new SoftAssert();

    }

    @AfterMethod
    public void teardown() {
        if (driver != null) {
           // driver.quit();
        }
    }

    @Test
    public void test() throws IOException {
        excel = new FileInputStream(excelFile);
        XSSFWorkbook wb = new XSSFWorkbook(excel);
        XSSFSheet sheet = wb.getSheet("Sheet2");
        driver.get(prop.getProperty("url"));
        loginPage.username.sendKeys(prop.getProperty("userName"));
        loginPage.password.sendKeys(prop.getProperty("password") + Keys.ENTER);
        orderPage.orderLink.click();
        Select category = new Select(orderPage.dropDown);
//        List<WebElement> products = product.getOptions();

        int rowNumber = sheet.getLastRowNum();
        for (int i = 1; i <= rowNumber; i++) {
            Row row = sheet.getRow(i);
            String quantity = row.getCell(CellReference.convertColStringToIndex("A")).toString();
            String pricePerUnit = row.getCell(CellReference.convertColStringToIndex("B")).toString();
            String expectedDiscount = row.getCell(CellReference.convertColStringToIndex("C")).toString();
            String expectedTotal = row.getCell(CellReference.convertColStringToIndex("D")).toString();
            String customerName = row.getCell(CellReference.convertColStringToIndex("E")).toString();
            String street = row.getCell(CellReference.convertColStringToIndex("F")).toString();
            String city = row.getCell(CellReference.convertColStringToIndex("G")).toString();
            String state = row.getCell(CellReference.convertColStringToIndex("H")).toString();
            String zip = row.getCell(CellReference.convertColStringToIndex("I")).toString();
            String card = row.getCell(CellReference.convertColStringToIndex("J")).toString();
            String cardNo = row.getCell(CellReference.convertColStringToIndex("K")).toString();
            String expireDate = row.getCell(CellReference.convertColStringToIndex("L")).toString();
            String product = row.getCell(CellReference.convertColStringToIndex("O")).toString();
            category.selectByVisibleText(product);
            orderPage.quantity.clear();
            orderPage.quantity.sendKeys(quantity);
            orderPage.customerName.sendKeys(customerName);
            orderPage.street.sendKeys(street);
            orderPage.city.sendKeys(city);
            orderPage.state.sendKeys(state);
            orderPage.zip.sendKeys(zip);
            driver.findElement(By.xpath("//input[@value='" + card + "']")).click();
            orderPage.cardNr.sendKeys(cardNo);
            orderPage.expireDate.sendKeys(expireDate);
            String actualTotal = orderPage.total.getAttribute("value");
            String actualDiscount = orderPage.discount.getAttribute("value");
            orderPage.processButton.click();
            Row row1 = sheet.getRow(i);
            if(row1 == null) row1 = sheet.createRow(i);
            Cell status = row1.getCell(CellReference.convertColStringToIndex("M"));
            if(status == null) status = row1.createCell(CellReference.convertColStringToIndex("M"));
            String str = "";
            if(actualDiscount.equals(expectedDiscount) && actualTotal.equals(expectedTotal)){
                str = "PASS";
            }else{
                str = "FAIL";
            }
            soft.assertEquals(actualDiscount,expectedDiscount);
            soft.assertEquals(actualTotal,expectedTotal);
            status.setCellValue(str);
            Cell timestamp = row1.getCell(CellReference.convertColStringToIndex("N"));
            if(timestamp == null) timestamp = row1.createCell(CellReference.convertColStringToIndex("N"));
            Date date = new Date();
            String times = sdf.format(date);
            timestamp.setCellValue(times);
            Cell discount = row1.getCell(CellReference.convertColStringToIndex("Q"));
            if(discount == null) discount = row1.createCell(CellReference.convertColStringToIndex("Q"));
            discount.setCellValue(actualDiscount);
            Cell total = row1.getCell(CellReference.convertColStringToIndex("P"));
            if(total == null) total = row1.createCell(CellReference.convertColStringToIndex("P"));
            total.setCellValue(actualTotal);


       }

        try{
            out = new FileOutputStream(new File(excelFile));
            wb.write(out);
            wb.close();
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        soft.assertAll();


    }


}
