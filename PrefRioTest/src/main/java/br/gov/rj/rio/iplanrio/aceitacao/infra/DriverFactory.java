package br.gov.rj.rio.iplanrio.aceitacao.infra;

import java.io.File;
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

import br.gov.rj.rio.iplanrio.aceitacao.util.SeleniumUtils;
import br.gov.rj.rio.iplanrio.aceitacao.util.Utils;

public class DriverFactory {
	//preciso de uma instancia do webdriver, pois erá ele que ira gerencciar pra mim
	// só que esta instância precisa ser estatica
	//vamos crira tbm um construtor para essa class e deixar ela private para ninguém acessar
	//assim todos que quiserem acesssar esta class, deve vir através de um método do tipo static
	//e deve retornar um webdriver
	//quand algém pedir um driver, eu já vou inicializar e dimensionar a tela e depois retorno um driver
	private static  WebDriver driver;
	
	private DriverFactory() {
		// TODO Auto-generated constructor stub
	}
	
	//o que este metodo deve fazer? identificar o brwoser e passar adiate o driver desse browser
	public static WebDriver getDriver() {
		WebDriver browser;
		
		 
	browser =	SeleniumUtils.getDriver(SeleniumUtils
				.getSeleniumProperties("selenium.browser"));


		String proxyAddr = Utils.getProperties("proxy");

		org.openqa.selenium.Proxy proxy = new org.openqa.selenium.Proxy();

		if (!"".equalsIgnoreCase(proxyAddr)) {
			proxy.setHttpProxy(proxyAddr).setFtpProxy(proxyAddr).setSslProxy(proxyAddr)
					.setNoProxy("jdev.rio.rj.gov.br jdev jeap.rio.rj.gov.br jeap jhom.rio.rj.gov.br jhom localhost");
		}
//*************** identificar o browser e inicializar
		if (driver ==null) {
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

			DesiredCapabilities dc = new DesiredCapabilities();

			dc.setCapability(CapabilityType.UNEXPECTED_ALERT_BEHAVIOUR, UnexpectedAlertBehaviour.IGNORE);
			System.setProperty("webdriver.chrome.driver","C:/drivers/browser/chrome/88/chromedriver.exe");
			ChromeOptions options = new ChromeOptions();
			options.setUnhandledPromptBehaviour(UnexpectedAlertBehaviour.IGNORE); // 16/11/18
		

			driver = new ChromeDriver(options);

		}

		// se o browser for Internet Explorer configura a nao exibicao de warnings do IE
		if (browser.equals("ie")) {
		//ct	File file = new File(getSeleniumProperties("selenium.iedriver"));
		//	System.setProperty("webdriver.ie.driver", file.getAbsolutePath());
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
		}
		return driver;
	}

		public static void KillDriver() {
			if (driver != null) {
				driver.quit();
				driver = null;
			}
		}
	

}
