package br.gov.rj.rio.iplanrio.aceitacao.prefrio.page;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import br.gov.rj.rio.iplanrio.aceitacao.infra.DSL;

/**
 * Base para páginas de categoria/serviço do pref.rio.
 * Centraliza navegação via URL (Opção 1) + ações comuns (ex: cookies).
 */
public abstract class CategoriaBasePage {

    protected final WebDriver driver;
    protected final DSL dsl;

    protected static final String BASE_URL = "https://pref.rio";

    protected CategoriaBasePage(WebDriver driver) {
        this.driver = driver;
        this.dsl = new DSL(driver);
    }

    /** Abre uma rota relativa no pref.rio. Ex: "/servicos/categoria/cidade" */
    protected void abrirRota(String relativePath) {
        driver.get(BASE_URL + relativePath);
        aceitarCookiesSeAparecer();
        esperarUrlConter(relativePath);
    }

    protected void esperarUrlConter(String expected) {
        long deadline = System.currentTimeMillis() + Duration.ofSeconds(15).toMillis();
        while (System.currentTimeMillis() < deadline) {
            String url = driver.getCurrentUrl();
            if (url != null && url.contains(expected)) return;
            try { Thread.sleep(100); } catch (InterruptedException e) { /* ignore */ }
        }
        throw new AssertionError("URL não corresponde ao esperado. Esperado conter: " + expected
                + " | Atual: " + driver.getCurrentUrl());
    }

    /**
     * Fecha/aceita o banner de cookies se aparecer.
     * Observação: o texto do botão pode variar; por isso tentamos algumas opções comuns.
     */
    protected void aceitarCookiesSeAparecer() {
        try {
            By[] candidatos = new By[] {
                By.xpath("//*[self::button or self::a][normalize-space()='Aceitar todos']"),
                By.xpath("//*[self::button or self::a][normalize-space()='Aceitar']"),
                By.xpath("//*[self::button or self::a][contains(normalize-space(),'Aceitar')]")
            };

            for (By by : candidatos) {
                List<WebElement> els = driver.findElements(by);
                if (els == null || els.isEmpty()) continue;

                for (WebElement el : els) {
                    try {
                        if (el.isDisplayed() && el.isEnabled()) {
                            el.click();
                            // pequeno respiro para sumir overlay
                            try { Thread.sleep(250); } catch (InterruptedException e) { /* ignore */ }
                            return;
                        }
                    } catch (Exception ignore) { }
                }
            }
        } catch (Exception ignore) {
            // se não apareceu, segue o fluxo
        }
    }
}
