package br.gov.rj.rio.iplanrio.aceitacao.prefrio.test;

import java.util.Map;


import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import br.gov.rj.rio.iplanrio.aceitacao.infra.BaseAceitacaoTest;
import br.gov.rj.rio.iplanrio.aceitacao.prefrio.page.HomePage;

@Test
public class HomeTest extends BaseAceitacaoTest{

	

	@Override
	protected void executaPassos(Map<String, String> dado) throws Exception {
		
	
		HomePage homePage = new HomePage(driver);
		homePage.chamarURL();
		 
		 
	 
	 
		 
     
	}

 

	@Override
	@BeforeClass
	protected void beforeClass() {
		umTestador.carregaUmaPlanilha("home");
		umTestador.defineSeDeveReportarNoJira(false);
		umTestador.defineSeDeveReportarNoTestlink(false);
 

	}
}
