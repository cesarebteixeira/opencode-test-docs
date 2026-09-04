package br.gov.rj.rio.iplanrio.aceitacao.prefrio.page;

import org.openqa.selenium.WebDriver;

public class HomePage {
	
	   static WebDriver driver;
	
	public HomePage(WebDriver driver) {
		
		HomePage.driver = driver;
		
	}
 //	DSL dsl = new DSL(driver);
	public void chamarURL() {
		
 	 
		driver.get("https://pref.rio");
			      
	//	driver.manage().window().setSize(new Dimension (1200,765));
	//	driver.manage().window().maximize();
		 
		

	}
	/*
	public void selecionaFuncionalidadeHome(String funcionalidade) {
		
		System.out.println(funcionalidade);
		
		dsl.esperaPeloElemento(By.linkText(funcionalidade), 10);
	//	esperaPeloElemento(By.linkText(funcionalidade));
	
	//	driver.findElement(By.linkText(funcionalidade)).click();
		dsl.clicarLink(funcionalidade); 
		
		
	}

	public String obterNomePaginaUrl() {
		dsl.esperaPeloElemento("Id_MovimentacaoOrigem", 10);
	//	esperaPeloElemento(By.id("Id_MovimentacaoOrigem"));
	 	String nomeUrl = driver.getCurrentUrl();
	 	return 	 nomeUrl.substring(nomeUrl.lastIndexOf("br/")+2);
	 	
	}
	
*/
}
