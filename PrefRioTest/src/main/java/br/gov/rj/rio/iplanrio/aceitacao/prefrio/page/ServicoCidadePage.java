package br.gov.rj.rio.iplanrio.aceitacao.prefrio.page;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * Page Object da categoria "Cidade" (pref.rio/servicos/categoria/cidade).
 *
 * Estratégia (Opção 1): abre direto pela URL da categoria (mais estável para iniciar).
 */
public class ServicoCidadePage extends CategoriaBasePage {

    // ===== Rotas =====
    private static final String ROTA_CIDADE = "/servicos/categoria/cidade";

    // ===== Locators (refinamos conforme você for validando a tela) =====
    private final By tituloPagina = By.cssSelector("h1");

    /**
     * Links de cards de serviços dentro da categoria.
     * - começa com /servicos/
     * - evita /categoria/
     */
    private final By cardsDeServico = By.cssSelector("a[href^='/servicos/']:not([href*='/categoria/'])");

    public ServicoCidadePage(WebDriver driver) {
        super(driver);
    }

    /** Abre diretamente a categoria Cidade */
    public void abrir() {
        abrirRota(ROTA_CIDADE);
        esperarTelaCidadeCarregar();
    }

    /** Valida carregamento básico (URL + H1 não vazio) */
    public void esperarTelaCidadeCarregar() {
        if (!driver.getCurrentUrl().contains(ROTA_CIDADE)) {
            throw new AssertionError("URL não parece ser da categoria Cidade: " + driver.getCurrentUrl());
        }

        dsl.esperaPeloElemento(tituloPagina, 15);
        String h1 = driver.findElement(tituloPagina).getText();
        if (h1 == null || h1.trim().isEmpty()) {
            throw new AssertionError("H1 da página está vazio na categoria Cidade.");
        }
    }

    /** Coleta hrefs absolutos de todos os cards de serviço listados na categoria */
    public List<String> coletarLinksDosCards() {
        dsl.esperaPeloElemento(cardsDeServico, 15);

        List<WebElement> links = driver.findElements(cardsDeServico);
        List<String> hrefs = new ArrayList<>();

        for (WebElement a : links) {
            String href = a.getAttribute("href");
            if (href != null && !href.trim().isEmpty()) {
                hrefs.add(href.trim());
            }
        }
        return hrefs;
    }

    /**
     * Valida qualidade básica:
     * - existe ao menos 1 card
     * - texto dos cards não vazio
     * - href não vazio
     * - sem duplicados
     */
    public void validarCardsElinksBasicos() {
        dsl.esperaPeloElemento(cardsDeServico, 15);

        List<WebElement> links = driver.findElements(cardsDeServico);
        if (links == null || links.isEmpty()) {
            throw new AssertionError("Nenhum card/link de serviço foi encontrado na categoria Cidade.");
        }

        Set<String> hrefUnicos = new HashSet<>();

        for (WebElement a : links) {
            String texto = a.getText() != null ? a.getText().trim() : "";
            String href = a.getAttribute("href");

            if (texto.isEmpty()) {
                throw new AssertionError("Encontrado card com texto vazio (provável problema de seletor/DOM).");
            }
            if (href == null || href.trim().isEmpty()) {
                throw new AssertionError("Card '" + texto + "' está sem href.");
            }
            if (!hrefUnicos.add(href.trim())) {
                throw new AssertionError("Link duplicado encontrado: " + href);
            }
        }
    }

    /**
     * Valida o status HTTP dos links dos cards (sem clicar).
     * Aceita HTTP 200 e redirects (301/302).
     *
     * Observação: se houver WAF/rate-limit, você pode reduzir o volume (ex: validar só uma amostra).
     */
    public void validarStatusHttpDosLinks() throws Exception {
        List<String> hrefs = coletarLinksDosCards();
        if (hrefs == null || hrefs.isEmpty()) {
            throw new AssertionError("Não foi possível coletar hrefs dos cards (lista vazia).");
        }

        List<String> quebrados = new ArrayList<>();

        for (String href : hrefs) {
            int code = obterStatusHttp(href);
            if (!(code == 200 || code == 301 || code == 302)) {
                quebrados.add(href + " -> HTTP " + code);
            }
        }

        if (!quebrados.isEmpty()) {
            throw new AssertionError("Links com possível problema:\n" + String.join("\n", quebrados));
        }
    }

    private int obterStatusHttp(String url) throws Exception {
        HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection();
        con.setInstanceFollowRedirects(false);
        con.setRequestMethod("GET");
        con.setConnectTimeout(8000);
        con.setReadTimeout(8000);
        con.connect();
        return con.getResponseCode();
    }
}
