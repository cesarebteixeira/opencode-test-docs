package br.gov.rj.rio.iplanrio.aceitacao.prefrio.test;

import java.util.Map;

import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import br.gov.rj.rio.iplanrio.aceitacao.infra.BaseAceitacaoTest;
import br.gov.rj.rio.iplanrio.aceitacao.prefrio.page.HomePage;
import br.gov.rj.rio.iplanrio.aceitacao.prefrio.page.LoginPage;

@Test
public class ServicoCidadeTest extends BaseAceitacaoTest {
	 
	 
	
	@Override
	protected void executaPassos(Map<String, String> dado) throws Exception {
		
		
			HomePage homePage = new HomePage(driver);
  		
			homePage.chamarURL();
		 
			LoginPage loginPage = new LoginPage(driver);
			
			if (dado.get("fazLogin").equals("sim")){
				loginPage.clicarSigIn();
				loginPage.escrevaSeuLogin(dado.get("meuLogin"));
				Assert.assertEquals(dado.get("meuLogin"), loginPage.obterValorDigitadoNoCampoLogin());
	 		
				loginPage.escrevaSuaSenha(dado.get("minhaSenha"));
				Assert.assertEquals(dado.get("minhaSenha"), loginPage.obterValorDigitadoNoCampoSenha());
				loginPage.cliqueNoBotaoParaLogar();
				
			}
 		
			driver.findElement(By.xpath("//a[@href='/servicos/categoria/cidade']")).click();
			
			
					 
			
 		
 		
 	 
 		
 	/*	
 		//checar se � preciso fazer um ASSERT agora
 		if(dado.get("exit").equals("verifiqueResultadoEsperadoAgora")){
 			throw new AssertionFakeException("Verificando se a aplica��o emitiu mensagem de erro");
 		}
 			*/
 		 
	 
	}

 



	@Override
	@BeforeClass
	protected void beforeClass() {
		umTestador.carregaUmaPlanilha("Login");
	  	umTestador.defineSeDeveReportarNoJira(false);
	  	umTestador.defineSeDeveReportarNoTestlink(false);
		 
	  	
	}
	
 

}
