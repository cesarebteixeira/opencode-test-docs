package br.gov.rj.rio.iplanrio.aceitacao.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.UnexpectedAlertBehaviour;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriver.Options;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;


/**
 * Classe Utils para o Seleium Ela esta implementando a classe
 * 'SeleniumInterface' para utilizar o nome dos browsersO sistema não pode
 * encontrar o caminho especificado
 * 
 * @author Elias Nogueira <elias.nogueira@gmail.com>
 *
 */
public class SeleniumUtils {

	
	private static WebDriver driver = null;
	static String headless = "n�o";

	@SuppressWarnings("deprecation")
	public static WebDriver getDriver(String browser) {

		// if (driver == null) {

		String proxyAddr = Utils.getProperties("proxy");

		org.openqa.selenium.Proxy proxy = new org.openqa.selenium.Proxy();

		if (!"".equalsIgnoreCase(proxyAddr)) {
			proxy.setHttpProxy(proxyAddr).setFtpProxy(proxyAddr).setSslProxy(proxyAddr)
					.setNoProxy("jdev.rio.rj.gov.br jdev jeap.rio.rj.gov.br jeap jhom.rio.rj.gov.br jhom localhost");
		}

		// se o browser for firefox instancia o driver do Firefox
		if (browser.equals("firefox")) {

			FirefoxProfile fxProfile = new FirefoxProfile();
			Options options;

			fxProfile.setPreference("browser.download.folderList", 2);
			fxProfile.setPreference("browser.download.manager.showWhenStarting", false);
			fxProfile.setPreference("browser.download.dir", "C:\\Users\\26211010\\Downloads");
			fxProfile.setPreference("browser.helperApps.neverAsk.saveToDisk", "text/csv");
			System.setProperty("webdriver.gecko.driver", "C:\\tools\\webdriver\\geckodriver.exe");
			FirefoxOptions optionsFirefox = new FirefoxOptions();
			optionsFirefox.addArguments("window-size=1400,600");
			optionsFirefox.addArguments("headless");
			DesiredCapabilities dc = DesiredCapabilities.firefox();

			dc.setCapability("marionette", true);
			driver = new FirefoxDriver(dc);

		}

		// se o browser for Google Chrome seta algumas configuracoes para execucao
		if (browser.equals("chrome")) {

			/*
			 * 
			 * DesiredCapabilities capabilities = DesiredCapabilities.chrome();
			 * capabilities.setJavascriptEnabled(true);
			 * capabilities.setCapability("chrome.binary",
			 * getSeleniumProperties("chrome.binary"));
			 * System.setProperty("webdriver.chrome.driver",
			 * getSeleniumProperties("webdriver.chrome.driver"));
			 * 
			 * driver = new ChromeDriver(capabilities);
			 */

			// ChromeOptions options = new ChromeOptions();
			// Proxy proxy = new Proxy();
			// proxy.setHttpProxy("myhttpproxy:3337");
			// options.setCapability("proxy", proxy);
			// options.addArguments("--headless");
			// options.addArguments("--disable-gpu");
			// options.setAcceptInsecureCerts(true);
			// options.addArguments("--allow-insecure-localhost");
			// options.addArguments("--lang=fr-CA");
			// options.addArguments("--start-maximized");
			// options.setCapability("chrome.binary",
			// getSeleniumProperties("chrome.binary"));
			DesiredCapabilities dc = new DesiredCapabilities();

			dc.setCapability(CapabilityType.UNEXPECTED_ALERT_BEHAVIOUR, UnexpectedAlertBehaviour.IGNORE);
			System.setProperty("webdriver.chrome.driver", getSeleniumProperties("webdriver.chrome.driver"));
			ChromeOptions options = new ChromeOptions();
			options.setUnhandledPromptBehaviour(UnexpectedAlertBehaviour.IGNORE); // 16/11/18
			if (headless.equals("sim")) {
				options.addArguments("window-size=1400,600");
				options.addArguments("headless");
			}

			// options.addExtensions(new File("/path/to/extension.crx")); // ct para pegar
			// impressão em PDF no chrome
			// options.setBinary( "C:/tools/webdriver/chromedriver.exe");

			// options.addArguments("start-maximized");

			driver = new ChromeDriver(options);

		}

		// se o browser for Internet Explorer configura a nao exibicao de warnings do IE
		if (browser.equals("ie")) {
			File file = new File(getSeleniumProperties("selenium.iedriver"));
			System.setProperty("webdriver.ie.driver", file.getAbsolutePath());
			// System.setProperty("webdriver.ie.driver", "C:\\drivers\\IEDriverServer.exe"
			// );
			DesiredCapabilities capabilities = DesiredCapabilities.internetExplorer();

			capabilities.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);

			capabilities.setCapability(CapabilityType.PROXY, proxy);

			driver = new InternetExplorerDriver(capabilities);
		}

		// se o browser for Opera instancia o driver do Opera
		if (browser.equals("opera")) {
			// driver = new OperaDriver();
		}

		// }

		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

		return driver;
	}

	/**
	 * Metodo para pegar o valor de alguma propriedade no arquivo de configuracao do
	 * Selenium O caminho e o nome do arquivo pode ser trocados
	 */
	public static String getSeleniumProperties(String name) {
		Properties properties = new Properties();
		String value = null;

		try {
			properties.load(new FileInputStream("selenium.properties")); // se necessitar altere o caminho e/ou o nome
																			// do arquivo
			value = properties.getProperty(name);

		} catch (IOException e) {
			e.printStackTrace();
		}

		return value;
	}

}
