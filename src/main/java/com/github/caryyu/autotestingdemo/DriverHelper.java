package com.github.caryyu.autotestingdemo;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

public class DriverHelper {
    public static void quit(WebDriver webDriver){
        if(webDriver != null) {
            webDriver.manage().deleteAllCookies();
            webDriver.quit();
        }
    }

    public static WebDriver open() {
        WebDriver webDriver = null;
        String driverPath = System.getProperty("driver.path");
        String driverMode = System.getProperty("driver.mode");
        String driverAddress = System.getProperty("driver.address");
        String driverArguments = System.getProperty("driver.arguments");

        if (StringUtils.equalsIgnoreCase(driverMode, "local")) {
            System.setProperty("webdriver.chrome.driver", driverPath);
            webDriver = new ChromeDriver(getChromeOptionsByString(driverArguments));
        } else if (StringUtils.equalsIgnoreCase(driverMode, "remote")) {
            URL url = null;
            try {
                url = new URL(driverAddress);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            webDriver = new RemoteWebDriver(url, getCapabilitiesByString(driverArguments));
        } else {
            throw new UnsupportedOperationException("Driver mode value is range of [local,remote]");
        }

        webDriver.manage().timeouts().pageLoadTimeout(120, TimeUnit.SECONDS); // 超时等待时间20s

        return webDriver;
    }

    private static ChromeOptions getChromeOptionsByString(String argumentsString) {
        ChromeOptions chromeOptions = new ChromeOptions();

        if(StringUtils.isNotEmpty(argumentsString)){
            String[] chromeArguments = argumentsString.split(" ");
            chromeOptions.addArguments(chromeArguments);
        }

        return chromeOptions;
    }

    private static DesiredCapabilities getCapabilitiesByString(String argumentsString) {
        DesiredCapabilities capabilities = DesiredCapabilities.chrome();
        capabilities.setCapability("chromeOptions", getChromeOptionsByString(argumentsString));
        return capabilities;
    }

}