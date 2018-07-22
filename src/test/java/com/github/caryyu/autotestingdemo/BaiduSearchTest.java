package com.github.caryyu.autotestingdemo;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class BaiduSearchTest {
    private WebDriver webDriver;

    @BeforeTest
    public void testBefore(){
        webDriver = DriverHelper.open();
    }

    @Test
    public void testSearch(){
        webDriver.get("http://www.baidu.com");
        new WebDriverWait(webDriver,60l)
                .until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"kw\"]")))
                .sendKeys("陈冠希");

        new WebDriverWait(webDriver,60l)
                .until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"su\"]")))
                .click();

        String text = new WebDriverWait(webDriver,60l)
                .until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@id='content_left']//div[1]//a")))
                .getText();

        Assert.assertTrue(text.contains("百度百科"),"不满足预期值");
    }

    @AfterTest
    public void testAfter(){
        DriverHelper.quit(webDriver);
    }
}