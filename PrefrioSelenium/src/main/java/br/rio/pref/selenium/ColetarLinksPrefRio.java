package br.rio.pref.selenium;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.time.Duration;
import java.util.*;

public class ColetarLinksPrefRio {

    public static void main(String[] args) throws Exception {
        // 1) AJUSTE AQUI: caminho completo do chromedriver.exe
        // Pelo que você disse, parece estar em C:\drivers\browser\chrome\142
        // Confirme se o arquivo é exatamente "chromedriver.exe" lá dentro.
        String chromeDriverPath = "C:\\drivers\\browser\\chrome\\142\\chromedriver.exe";

        // 2) Arquivo de saída
        Path output = Paths.get("C:\\projetos\\prefrio\\links.txt");
        Files.createDirectories(output.getParent());

        // 3) Configuração manual do driver
        System.setProperty("webdriver.chrome.driver", chromeDriverPath);

        ChromeOptions options = new ChromeOptions();
        // (opcional) Se quiser reduzir popups:
        options.addArguments("--disable-notifications");

        WebDriver driver = new ChromeDriver(options);

        try {
            String url = "https://pref.rio/";
            driver.get(url);

            // Espera simples pela página carregar
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
            wait.until(d -> ((JavascriptExecutor) d)
                    .executeScript("return document.readyState").equals("complete"));

            List<WebElement> anchors = driver.findElements(By.tagName("a"));

            URI base = URI.create(url);
            Set<String> linhas = new LinkedHashSet<>(); // mantém ordem e evita duplicados

            for (WebElement a : anchors) {
                String texto = safeTrim(a.getText());
                String href = safeTrim(a.getAttribute("href"));

                if (href.isEmpty()) continue;

                // Normaliza href relativo (se aparecer)
                String hrefAbsoluto = base.resolve(href).toString();

                // Você pode ajustar o formato como preferir
                linhas.add(texto + " | " + hrefAbsoluto);
            }

            Files.write(output, linhas, StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);

            System.out.println("OK! Total de links gravados: " + linhas.size());
            System.out.println("Arquivo: " + output);

        } finally {
            driver.quit();
        }
    }

    private static String safeTrim(String s) {
        return s == null ? "" : s.trim().replaceAll("\\s+", " ");
    }
}
