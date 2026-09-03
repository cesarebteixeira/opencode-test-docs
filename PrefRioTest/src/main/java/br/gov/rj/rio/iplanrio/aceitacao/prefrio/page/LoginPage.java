package br.gov.rj.rio.iplanrio.aceitacao.prefrio.page;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import br.gov.rj.rio.iplanrio.aceitacao.infra.DSL;


public class LoginPage {
	 
	
	 static  WebDriver driver;
 	// static  DSL dsl;	
	 
	public LoginPage(WebDriver driver) {
		
		LoginPage.driver = driver;
	//    LoginPage.dsl = new DSL(driver);
		
	}

	
	
	public void escrevaSeuLogin(String meuLogin) { 
	
		
 	// driver.findElement(By.id("j_username")).sendKeys(meuLogin);
		
//	 dsl.escrever("email", meuLogin); 
		
		 
	}

	
	public void escrevaSuaSenha(String minhaSenha) {	  
		 
   //	 	dsl.escrever("passwd", minhaSenha);	

	}



	public void esperaTelaLoginSerDisponibilizada() {
	  
//	 dsl.esperaPeloElemento("email", 30); 

	}
	

	//######################################################################################################
	//################  O B T E R   V A L O R   P A R A  A S S E R T   #####################################

	public String obterValorDigitadoNoCampoLogin() {
 
		DSL dsl = new DSL(driver); 
		return dsl.obterGetAttribute("email", "value");
			
		 
	}
	public String obterValorDigitadoNoCampoSenha() {
		DSL dsl = new DSL(driver); 
		 
		return dsl.obterGetAttribute("passwd", "value");
		
	 
}




	public void cliqueNoBotaoParaLogar() {
	 	DSL dsl = new DSL(driver); 
	//	dsl.aguardaTemporizador();
		dsl.clicarBotao("SubmitLogin");
		
	}



	public void clicarSigIn() {
		
		DSL dsl = new DSL(driver); 
		dsl.clicarBotao(By.xpath("//a[.//span[text()='Faça seu login']]"));
		
	}




}
