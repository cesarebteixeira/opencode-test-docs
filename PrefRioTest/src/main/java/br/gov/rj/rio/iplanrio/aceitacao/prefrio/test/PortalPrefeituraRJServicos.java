package br.gov.rj.rio.iplanrio.aceitacao.prefrio.test;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.*;

public class PortalPrefeituraRJServicos {

    private WebDriver driver;
    private WebDriverWait wait;

    // ====== AJUSTE ESTES SELECTORS COM O HTML REAL ======
    // URL inicial do portal
    private static final String BASE_URL = "https://pref.rio/"; // exemplo

    // Categoria "Cidade" na página principal
    private By categoriaCidade = By.xpath("//a[@href='/servicos/categoria/cidade']");

    // Lista de serviços na página 2
    // Ex.: cada serviço pode ser um <a> dentro de um <li>, ou um card com <a>
    private By linksServicos = By.cssSelector("ul li a"); // AJUSTAR para algo mais específico

    // Botão para ir para a próxima página
    private By botaoProximo = By.xpath("//button[contains(.,'Próximo') or contains(.,'>')]");

    // Título da página do serviço (página 3)
    private By tituloServico = By.cssSelector("h1"); // ajustar, se necessário
    // ====================================================

    public void setUp() {
        System.setProperty("webdriver.chrome.driver", "C:/drivers/browser/chrome/142/chromedriver.exe");
        driver = new ChromeDriver();
        driver.manage().window().maximize();
      //  wait = new WebDriverWait(driver, Duration.ofSeconds(10));
      
    }

    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    public void testarTodosServicosDaCategoriaCidade() throws InterruptedException {
        driver.get(BASE_URL);

        // 1) Clicar na categoria "Cidade" na página principal
     //   wait.until(ExpectedConditions.elementToBeClickable(categoriaCidade)).click();

        // 2) Estrutura para guardar todos os serviços encontrados (sem duplicar)
        Set<String> nomesServicos = new LinkedHashSet<>();
        Map<String, String> mapaServicoParaUrl = new LinkedHashMap<>();

        boolean temProximaPagina = true;

        while (temProximaPagina) {

            // 2.1) Espera a lista de serviços carregar
        //    wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(linksServicos));

            // 2.2) Coleta todos os serviços da página atual
            List<WebElement> servicosPagina = driver.findElements(linksServicos);
            System.out.println("Qtd serviços nesta página: " + servicosPagina.size());

            // IMPORTANTE: trabalharemos por índice para evitar StaleElementReference
            for (int i = 0; i < servicosPagina.size(); i++) {
                // Reencontra a lista (porque a página pode ter sido redesenhada em algum momento)
                servicosPagina = driver.findElements(linksServicos);

                WebElement servico = servicosPagina.get(i);

                String nome = servico.getText().trim();
                String url = servico.getAttribute("href");

                if (nome.isEmpty()) {
                    nome = "(sem texto) - index " + i;
                }

                nomesServicos.add(nome);
                mapaServicoParaUrl.put(nome, url);

                System.out.println("Serviço encontrado: " + nome + " -> " + url);

                // 2.3) (Opcional) Clicar em cada serviço e validar
                testarPaginaDoServico(servico, nome);

                // Volta para a lista
                driver.navigate().back();
                // Espera a lista carregar de novo
                wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(linksServicos));
            }

            // 2.4) Tenta ir para a próxima página de serviços
            List<WebElement> botoesProximo = driver.findElements(botaoProximo);

            if (botoesProximo.isEmpty() || !botoesProximo.get(0).isDisplayed() || !botoesProximo.get(0).isEnabled()) {
                temProximaPagina = false;
                System.out.println("Não há mais páginas de serviços.");
            } else {
                System.out.println("Indo para a próxima página de serviços...");
                botoesProximo.get(0).click();
                Thread.sleep(1000); // pode trocar por wait mais inteligente, se quiser
            }
        }

        // 3) Relatório final
        System.out.println("======================================");
        System.out.println("TOTAL DE SERVIÇOS ÚNICOS ENCONTRADOS: " + nomesServicos.size());
        for (String nome : nomesServicos) {
            System.out.println("- " + nome + " -> " + mapaServicoParaUrl.get(nome));
        }
    }

    private void testarPaginaDoServico(WebElement linkServico, String nomeServico) {
        // Clica no serviço (já estamos na página 2)
        linkServico.click();

        // Espera o título da página do serviço aparecer
        wait.until(ExpectedConditions.presenceOfElementLocated(tituloServico));

        WebElement titulo = driver.findElement(tituloServico);
        String tituloTexto = titulo.getText().trim();

        System.out.println("Abrindo serviço: " + nomeServico + " | Título da página: " + tituloTexto);

        // Aqui você pode colocar validações reais (JUnit/TestNG)
        // Exemplo simples: verificar se a página contém o texto do nome do serviço
        if (!tituloTexto.isEmpty() && tituloTexto.toLowerCase().contains(nomeServico.toLowerCase().substring(0, Math.min(10, nomeServico.length())))) {
            System.out.println("OK - Página parece coerente com o serviço: " + nomeServico);
        } else {
            System.out.println("ALERTA - Página pode não corresponder ao serviço: " + nomeServico);
        }
    }

    public static void main(String[] args) {
        PortalPrefeituraRJServicos teste = new PortalPrefeituraRJServicos();
        try {
            teste.setUp();
            teste.testarTodosServicosDaCategoriaCidade();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            teste.tearDown();
        }
    }
}
