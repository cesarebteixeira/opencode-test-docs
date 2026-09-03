package br.gov.rj.rio.iplanrio.aceitacao.infra;

import static org.testng.AssertJUnit.assertTrue;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import br.gov.rj.rio.iplanrio.aceitacao.infra.exception.AssertionFakeException;
import br.gov.rj.rio.iplanrio.aceitacao.infra.exception.InterruptFakeException;



public abstract class BaseAceitacaoTest {

	protected Testador umTestador;
	protected static WebDriver driver;
	public static   String   resultadoVariavel =null;
	protected DSL dsl;

	@Test
	public void ExecutaListaDeCasosDeUso() throws Exception {
		


		System.out.println("Estou dentro do ExecutaListaDeCasosDeUso");



		List<Map<String,String>> dadosDaPlanilha = umTestador.getDadosDaPlanilha();



		for (Map<String, String> dado : dadosDaPlanilha) {

			System.out.println("*** Executando caso de teste : " + dado.get("casoDeTeste"));


//ct em 18/12/2025 retiramos esse filtro para que o driver seja disponibilizado para todos
	//		if (dado.get("fazLogin").equals("sim") ){
				
				driver = umTestador.getDriver();
				
		//	}

		

			//		driver = umTestador.getDriver();

			try {

				this.executaPassos(dado);
				// 	umTestador.capturaMensagensDaTela();


				//	if(!dado.get("pulaEsseRegistro").equals("Sim")){
				
			
				this.verificaResultado(dado);	
		

				//	umTestador.reportaQuePassouNoTeste(dado, driver);
				//	}

			}catch (InterruptFakeException e){
				System.out.println("Uma interrup��o esperada ocorreu");

 			}catch (AssertionFakeException e){

				System.out.println("Vamos verificar se h� erro.");


				try {

					//	umTestador.capturaMensagensDaTela();
					//	driver.navigate().refresh();
					 
					this.verificaResultado(dado);		 
				} catch (AssertionError e2){ 
					System.out.println("*** reportaErroNaAplicacao");
					umTestador.reportaErroNaAplicacao(e2, dado, driver);
				}catch (Exception e2) {
					System.out.println("*** reportaErroNaEstruturaDoTeste");
					umTestador.reportaErroNaEstruturaDoTeste(e2, dado, driver);
				}

			}catch (AssertionError e){ 

				umTestador.reportaErroNaAplicacao(e, dado, driver);
			}catch (Exception e) {


				umTestador.reportaErroNaEstruturaDoTeste(e, dado, driver);
			}finally {

				 
				//ct	driver.quit();
				if (dado.get("quit").equals("sim")){ //visando não ficar fazendo login acada linha lida da planilha.
						umTestador.geraRelatorioDeEvidencias();
					driver.quit();
				
				}  
			}
		}

		umTestador.geraRelatorioDeEvidencias();




	}
	
	
	private void verificaResultado(Map<String, String> dado) throws InterruptedException {

		
		
		
		
		
		//		System.out.println("*** Vamos checar o resultado obtdo com o resultado esperado");
		
		try {
		
		
		
		if(!"*".equals(dado.get("resultadoEsperado"))){	
			
		// 	Thread.sleep(2000);
			String todoTextoDaPagina = driver.findElement(By.tagName("body")).getText();
		//	System.out.println("#source:" +driver.getPageSource());		 

			String message = "O valor '" + dado.get("resultadoEsperado") + "' n�o  foi encontrado na p�gina.";
		 
			assertTrue(message, StringUtils.contains(todoTextoDaPagina, dado.get("resultadoEsperado")));
		
			 
		}else {
			
	//		System.out.println("teste desabilitado para este caso de teste " + dado.get("casoDeTeste"));
		}
		
		umTestador.reportaQuePassouNoTeste(dado, driver);
		System.out.println(" *** " + dado.get("casoDeTeste")+" executado com sucesso");
	}catch (Exception e) {
		
	}

	}
	



	 

	protected abstract void executaPassos(Map<String, String> dado) throws Exception;

	@BeforeClass
	protected abstract void beforeClass();

	@BeforeTest
	public void beforeTest() {

		umTestador = new Testador();
		
		

	}

	@AfterTest
	public void afterTest() throws IOException {

	//ct	JiraInstance.getRestClient().close();

	}

	@AfterClass
	public void afterClass() {
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date();
		System.out.println("*** fim *** : " + dateFormat.format(date));

	}

}
