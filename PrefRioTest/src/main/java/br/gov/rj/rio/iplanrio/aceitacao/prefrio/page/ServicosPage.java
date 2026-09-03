package br.gov.rj.rio.iplanrio.aceitacao.prefrio.page;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import br.gov.rj.rio.iplanrio.aceitacao.infra.DSL;


public class ServicosPage {
	 
	
	 static  WebDriver driver;
	 static  DSL dsl;	
	 
	public ServicosPage(WebDriver driver) {
		
		ServicosPage.driver = driver;
		ServicosPage.dsl = new DSL(driver);
		
	}

	public void selecionaServicoCidade() {
		
		dsl.clicarBotao(By.xpath("//a[@href='/servicos/categoria/cidade']"));
		
		
		
	};
public void selecionaServicoTransporte() {
		
		
		
	};
public void selecionaServicoSaude() {
		
		
		
	};
public void selecionaServicoEducacao() {
		
		
		
	};
public void selecionaServicoCidadania() {
		
		
		
	};
public void selecionaServicoServidor() {
		
		
		
	};
public void selecionaServicoCultura() {
		
		
		
	};
public void selecionaServicoMeioAmbiente() {
		
		
		
	};
public void selecionaServicoIptu() {
		
		
		
	};
	
public void selecionaServicoCadRio() {
		
		
		
	};
public void selecionaServicoMultas() {
		
		
		
	};
public void selecionaServicoAlvara() {
		
		
		
	};

public void selecionaServicoLicencaSanitaria() {
		
		
		
	};
public void selecionaServicoCadUnico() {
		
		
		
	};
public void selecionaServicoDividaAtiva() {
		
		
		
	};

}