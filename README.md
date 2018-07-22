# 说明
随着项目后期的业务趋于稳定之后，传统的手工方式进行回归测试消耗太多的人力资源，那就要思考如何利用计算机帮助我们达到自动化测试的目的了；编写此篇文档的主要目的是为了针对项目组的新人员加入，如何让他们能够更快的熟悉什么是自动化测试，怎样来进行实现自动化测试，我这里主要采用标题所提到的框架与技术。
> 注意：如果你的系统业务还在持续改进还没有稳定下来请不要使用自动化测试，切记！切记！否则所消耗的人力成本会更大，一定要趋于稳定之后使用自动化测试。
# 开始
我们这里主要采用Java作为编写自动化测试代码的语言，利用标准化的`maven`环境进行运行测试，具体如何利用`maven`创建项目和管理项目相关的知识我这里不赘述，主要环绕`TestNG`与`Selenium`的使用知识点进行展开。
# 简述`TestNG`&`Selenium`
我想刚开始的时候总会有些同学对`TestNG`和`Selenium`的概念模糊不清，以下我会简单的把两者的大致功能与作用描述一下。
## TestNG
这个玩意主要是一个测试框架工具（与`JUnit`相同，当然你也可以选择JUnit亦可），对定义了测试的概念及理论并进行实现提供了许多方便的工具，如`Assert`工具之类的；从而如何有效快速化达到我们的目的，针对其核心主要涉及`断言`对`实际值`与`预期值`进行处理，主要围绕结果二个主要状态(Failure、Skipped)进行判断通过率。
> 注意：`TestNG`与`JUnit`我所说的相同只是宏观上的，可能具体还会存在某些理论上的冲突，而在`JUnit`中存在结果为`Error`的状态，在`TestNG`是没有的。
## Selenium 介绍
此款框架就比较复杂了，当然归根结底其核心功能主要围绕模拟终端用户的行为进行操作，如：浏览器用户行为、手机端用户行为；其所涉及到的内容较多，对很多语言都有相关的SDK支持；当然在这里我们只用到了Java的SDK包与Chrome驱动的使用方式，更多方式可以参考其开源库：[点击这里](https://github.com/SeleniumHQ/selenium/)。
# 环境准备
## TestNG 依赖包
```
<dependency>
    <groupId>org.testng</groupId>
    <artifactId>testng</artifactId>
    <version>6.9.12</version>
</dependency>
```
## Selenium 依赖包
由于我只使用了`Chrome`驱动，所以我过滤了其他的驱动包，可以根据需求进行导入就可以了。
```
<dependency>
    <groupId>org.seleniumhq.selenium</groupId>
    <artifactId>selenium-java</artifactId>
    <version>3.5.3</version>
    <exclusions>
        <exclusion>
            <groupId>org.seleniumhq.selenium</groupId>
            <artifactId>selenium-edge-driver</artifactId>
        </exclusion>
        <exclusion>
            <groupId>org.seleniumhq.selenium</groupId>
            <artifactId>selenium-ie-driver</artifactId>
        </exclusion>
        <exclusion>
            <groupId>org.seleniumhq.selenium</groupId>
            <artifactId>selenium-opera-driver</artifactId>
        </exclusion>
        <exclusion>
            <groupId>org.seleniumhq.selenium</groupId>
            <artifactId>selenium-safari-driver</artifactId>
        </exclusion>
        <exclusion>
            <groupId>org.seleniumhq.selenium</groupId>
            <artifactId>htmlunit-driver</artifactId>
        </exclusion>
        <exclusion>
            <groupId>net.sourceforge.htmlunit</groupId>
            <artifactId>htmlunit</artifactId>
        </exclusion>
        <exclusion>
            <groupId>net.sourceforge.htmlunit</groupId>
            <artifactId>htmlunit-core-js</artifactId>
        </exclusion>
        <exclusion>
            <groupId>net.sourceforge.htmlunit</groupId>
            <artifactId>neko-htmlunit</artifactId>
        </exclusion>
        <exclusion>
            <groupId>org.eclipse.jetty.websocket</groupId>
            <artifactId>websocket-api</artifactId>
        </exclusion>
        <exclusion>
            <groupId>org.eclipse.jetty.websocket</groupId>
            <artifactId>websocket-client</artifactId>
        </exclusion>
        <exclusion>
            <groupId>org.eclipse.jetty.websocket</groupId>
            <artifactId>websocket-common</artifactId>
        </exclusion>
        <exclusion>
            <groupId>org.eclipse.jetty</groupId>
            <artifactId>jetty-io</artifactId>
        </exclusion>
        <exclusion>
            <groupId>org.eclipse.jetty</groupId>
            <artifactId>jetty-util</artifactId>
        </exclusion>
    </exclusions>
</dependency>
```
## Maven 运行插件
当我们在`maven`的`mvn test`命令测试的时候我们主要用到一个运行插件`maven-surefire-plugin`，此插件支持与`TestNG`的集成，帮助我们做了很多简便的事情，具体可以[点击这里查看更多](http://maven.apache.org/surefire/maven-surefire-plugin/)。
```
<build>
    <plugins>
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-compiler-plugin</artifactId>
            <version>2.5.1</version>
            <configuration>
                <source>${java.version}</source>
                <target>${java.version}</target>
                <encoding>${project.build.sourceEncoding}</encoding>
            </configuration>
        </plugin>
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-surefire-plugin</artifactId>
            <version>2.20</version>
            <configuration>
                <suiteXmlFiles>
                    <suiteXmlFile>src/test/resources/testng.xml</suiteXmlFile>
                </suiteXmlFiles>
                <systemPropertyVariables>
                    <driver.path>${driver.path}</driver.path>
                    <driver.mode>${driver.mode}</driver.mode>
                    <driver.address>${driver.address}</driver.address>
                    <driver.arguments>${driver.arguments}</driver.arguments>
                    <buildDirectory>${project.build.directory}</buildDirectory>
                </systemPropertyVariables>
                <properties>
                    <property>
                        <name>usedefaultlisteners</name>
                        <value>false</value>
                    </property>
                    <property>
                        <name>reporter</name>
                        <value>org.testng.reporters.XMLReporter</value>
                    </property>
                    <property>
                        <name>testnames</name>
                        <value>${testng.testnames}</value>
                    </property>
                </properties>
            </configuration>
        </plugin>
    </plugins>
</build>
```
关于此插件有几处重要的属性需要注意，根据以下的描述进行了解这些属性的作用性：

* suiteXmlFiles - 指定`TestNG`的用例文件，用例文件中描述了`TestNG`核心处理的配置；
* systemPropertyVariables - 利用此属性可以自定义系统变量，方便我们在`maven`的`profile`层面做环境控制处理；
* properties - 这里是此插件与`TestNG`集成用来控制`TestNG`配置的内容，比如最后输出的报告控制或者是指定具体的测试名称。

> 注意: 示例中`${xxx}`主要取值于`maven`的最外层即全局的`properties`属性值，这样利于我们之后的统一管理。
## 全局属性值
```
<properties>
    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    <java.version>1.8</java.version>
    <reports.html.enabled>false</reports.html.enabled>

    <driver.path>/Users/cary/Desktop/chromedriver</driver.path>
    <driver.mode>local</driver.mode>
    <driver.address>http://172.16.1.12:4444/wd/hub</driver.address>
    <driver.arguments>--window-size=1280,800</driver.arguments>
    <testng.testnames>testname</testng.testnames>
</properties>
```
## 下载 Chrome Driver
由于我们采用`chrome driver`进行测试，所以需要预先把相应的执行文件下载到本地，可以参看`selenium`的文档进行下载：[点击打开](https://github.com/SeleniumHQ/selenium/wiki/ChromeDriver)。
> 注意：`chrome driver`不能单独运行，需要依赖`chrome`浏览器，所以浏览器也要安装。
## 工具类
由于我自定义封装了一些全局的属性值，方便我们更好的进行管理，那就会有相应的工具类进行支持处理。
```
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
```
# 示例演示
这里我们主要编写一个自动查询百度我们想要的关键字，然后通过断言查看第一条结果是否是百度百科并且是否满足我们的预期值。
## 核心代码
```
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

```
## TestNG XML 配置
这里的XML路径位置一般位于项目的`src/test/resources/testng.xml`下。
```
<?xml version="1.0" encoding="UTF-8"?><!DOCTYPE suite SYSTEM "http://testng.org/testng-1.0.dtd" >
<suite name="testsuite" parallel="methods" thread-count="1">
    <test name="testname">
        <classes>
            <class name="com.nakedhub.autotester.nhos.BaiduSearchTest">
                <methods>
                    <include name="testSearch"/>
                </methods>
            </class>
        </classes>
    </test>
</suite>
```
## 运行命令
主要利用MAVEN的命令进行执行，如果用到了`profile`的话，可以使用`-P`参数进行指定具体的`profile`环境控制，如：`mvn test -Pcary`。
```
mvn test
```
# 更多查看
* TestNG 文档 - http://testng.org/doc/documentation-main.html
* Selenium Wiki - https://github.com/SeleniumHQ/selenium/wiki
* 代码下载 - https://github.com/caryyu/auto-testing-demo
