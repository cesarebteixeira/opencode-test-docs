package br.gov.rj.rio.iplanrio.aceitacao.prefrio.test;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import br.gov.rj.rio.iplanrio.aceitacao.infra.BaseAceitacaoTest;
import br.gov.rj.rio.iplanrio.aceitacao.prefrio.page.HomePage;

@Test
public class PortalCrawler3NiveisTest extends BaseAceitacaoTest {

    private static final int NIVEL_MAXIMO = 3;

    private OpenAIService openAIService;
    private WebDriverWait wait;

    @Override
    protected void executaPassos(Map<String, String> dado) throws Exception {

        openAIService = new OpenAIService();
        wait = new WebDriverWait(driver, 10);

        HomePage homePage = new HomePage(driver);
        homePage.chamarURL();

     //   wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("body")));
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//a[@href='/servicos/categoria/cidade']")));

        // ================================
        // NÍVEL 1 = CATEGORIAS (Home + página 2)
        // ================================
        Set<String> categorias = coletarLinksNivel1DeCategorias();
        System.out.println("NÍVEL 1 - Total de categorias coletadas: " + categorias.size());

        Set<String> visitados = new HashSet<>();

        for (String hrefCategoria : categorias) {
            System.out.println("\n=== NÍVEL 1: Categoria: " + hrefCategoria + " ===");

            abrirUrl(hrefCategoria);

            String topicoCategoria = extrairTopicoDaCategoria(hrefCategoria);
            if (topicoCategoria == null || topicoCategoria.isEmpty()) {
                topicoCategoria = "CATEGORIA";
            }

            try {
                // nível 1: página da categoria (ex.: SERVIDOR)
                validarPaginaEFilhos("Home -> " + hrefCategoria, 1, topicoCategoria, visitados);
            } finally {
                driver.navigate().back();
                wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("body")));
            }
        }
    }

    /**
     * Coleta as categorias de serviços (Nível 1) na Home e na página 2.
     * Aqui filtramos apenas URLs que contenham "/servicos/categoria/".
     */
    private Set<String> coletarLinksNivel1DeCategorias() throws InterruptedException {

        Set<String> hrefs = new HashSet<>();

        // -------------------------
        // (1) HOME
        // -------------------------
        System.out.println("Coletando categorias na HOME...");

        List<WebElement> linksHome = driver.findElements(By.tagName("a"));
        for (WebElement link : linksHome) {
            String href = link.getAttribute("href");
            if (href != null && href.contains("/servicos/categoria/")) {
                hrefs.add(href);
            }
        }

        // -------------------------
        // (2) IR PARA PÁGINA 2
        // -------------------------
        System.out.println("Indo para a página 2 da Home...");

        List<WebElement> botoes = driver.findElements(
            By.xpath("(//button[@aria-label='Próximo slide'])[2]")
        );

        if (!botoes.isEmpty()) {
            WebElement botaoPagina2 = botoes.get(0);
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", botaoPagina2);
            Thread.sleep(1500);
            wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("body")));

            // -------------------------
            // (3) PÁGINA 2
            // -------------------------
            System.out.println("Coletando categorias na página 2...");

            List<WebElement> linksPagina2 = driver.findElements(By.tagName("a"));
            for (WebElement link : linksPagina2) {
                String href = link.getAttribute("href");
                if (href != null && href.contains("/servicos/categoria/")) {
                    hrefs.add(href);
                }
            }
        } else {
            System.out.println("⚠ Botão da página 2 não encontrado. Usando apenas a HOME.");
        }

        return hrefs;
    }

    private void abrirUrl(String href) {
        driver.get(href);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("body")));
    }

    /**
     * Valida a página atual com base no "topicoPai" (que representa
     * o título do link que levou até esta página), e navega de forma recursiva
     * nos links filhos até o nível máximo.
     *
     * topicoPai:
     *  - Nível 1: nome da categoria (ex.: SERVIDOR)
     *  - Nível 2: título do serviço (ex.: Informações sobre pecúlio)
     *  - Nível 3: título do subserviço, etc.
     */
    private void validarPaginaEFilhos(String contextoAtual,
                                      int nivelAtual,
                                      String topicoPai,
                                      Set<String> visitados) throws IOException {

        if (nivelAtual > NIVEL_MAXIMO) {
            return;
        }

        // ================================
        // 1) Validar CONTEÚDO da página atual em relação ao tópicoPai (exceto no nível 1, se você preferir)
        // ================================
        WebElement body = driver.findElement(By.tagName("body"));
        String conteudoPagina = body.getText();

        if (topicoPai != null && !topicoPai.isEmpty() && nivelAtual >= 2) {
            // A partir do nível 2, a página deve corresponder ao título do serviço que a abriu
      //aqui chama openai..inibir para teste      String respConteudo = openAIService.validarConteudo(topicoPai, conteudoPagina);
      //      System.out.println("IA CONTEÚDO [" + contextoAtual + " | tópico: " + topicoPai + "] => " + respConteudo);
        }

        if (nivelAtual == NIVEL_MAXIMO) {
            return;
        }

        // ================================
        // 2) Coletar links filhos (nível seguinte)
        // ================================
        List<WebElement> anchors = driver.findElements(By.tagName("a"));

        for (int i = 0; i < anchors.size(); i++) {

            WebElement link = anchors.get(i);
            String href = link.getAttribute("href");
            if (href == null || href.trim().isEmpty()) {
                continue;
            }

            // Evita reprocessar mesmo href várias vezes
            if (!visitados.add(href)) {
                continue;
            }

            // Extrair o título "real" do serviço:
            // 1º tenta pegar span.text-card-foreground
            // se não encontrar, usa link.getText()
            String tituloServico = extrairTituloDoLink(link).trim();
            if (tituloServico.isEmpty()) {
                // se ainda assim estiver vazio, não vale a pena validar
                continue;
            }

            String contextoFilho = contextoAtual + " -> " + tituloServico;

            // ---------------------------
            // 2a) NÍVEL DE TÍTULO (antes de clicar)
            // Validar se o título é coerente com o tópicoPai atual
            // Ex: tópicoPai=SERVIDOR / título=Informações sobre pecúlio
            // ---------------------------
            if (topicoPai != null && !topicoPai.isEmpty()) {
                try {
        //inibido pra teste            String respTitulo = openAIService.validarTitulo(topicoPai, tituloServico);
        //            System.out.println("IA TÍTULO [" + contextoAtual + " -> " + tituloServico +
         //                              " | tópicoPai: " + topicoPai + "] => " + respTitulo);
                } catch (Exception e) {
                    System.out.println("Erro ao validar título com IA para: " + contextoFilho);
                    e.printStackTrace();
                }
            }

            // ---------------------------
            // 2b) Clicar no link e descer um nível
            // ---------------------------
            System.out.println("\n[NÍVEL " + (nivelAtual + 1) + "] Visitando: " + contextoFilho);

            try {
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", link);
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", link);

                try {
                    wait.until(ExpectedConditions.stalenessOf(link));
                } catch (TimeoutException e) {
                    // pode ser SPA ou mesmo conteúdo parcial; seguimos
                }

                wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("body")));

                // Chamada recursiva:
                // Agora, o "tópicoPai" dos próximos links é o título deste serviço.
                try {
                    validarPaginaEFilhos(contextoFilho, nivelAtual + 1, tituloServico, visitados);
                } finally {
                    // Volta para a página anterior
                    driver.navigate().back();
                    wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("body")));
                    anchors = driver.findElements(By.tagName("a")); // re-coleta depois do back
                }

            } catch (Exception e) {
                System.out.println("Erro ao visitar link: " + contextoFilho);
                e.printStackTrace();
            }
        }
    }

    /**
     * Tenta extrair o título principal associado ao link:
     * 1) Se existir <span class="text-card-foreground">...</span> dentro da âncora, usa esse texto.
     * 2) Caso contrário, usa link.getText().
     */
    private String extrairTituloDoLink(WebElement link) {
        try {
            WebElement span = link.findElement(By.cssSelector("span.text-card-foreground"));
            String txt = span.getText();
            if (txt != null && !txt.trim().isEmpty()) {
                return txt.trim();
            }
        } catch (NoSuchElementException e) {
            // ignora e tenta getText()
        }

        String fallback = link.getText();
        return fallback == null ? "" : fallback.trim();
    }

    /**
     * A partir da URL de categoria, tenta extrair um "nome amigável" do tópico.
     * Exemplo: https://pref.rio/servicos/categoria/servidor -> "SERVIDOR".
     */
    private String extrairTopicoDaCategoria(String hrefCategoria) {
        if (hrefCategoria == null) return null;

        String slug = hrefCategoria;
        int idx = hrefCategoria.indexOf("/servicos/categoria/");
        if (idx >= 0) {
            slug = hrefCategoria.substring(idx + "/servicos/categoria/".length());
        }

        // corta depois de uma próxima barra, se existir (caso tenha id UUID etc.)
        int barra = slug.indexOf('/');
        if (barra > 0) {
            slug = slug.substring(0, barra);
        }

        // slug: "servidor" -> "SERVIDOR"
        slug = slug.replace('-', ' ').toUpperCase();
        return slug;
    }

    @Override
    @BeforeClass
    protected void beforeClass() {
        umTestador.carregaUmaPlanilha("home");
        umTestador.defineSeDeveReportarNoJira(false);
        umTestador.defineSeDeveReportarNoTestlink(false);
    }
}