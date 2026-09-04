package br.gov.rj.rio.iplanrio.aceitacao.prefrio.test;




import java.util.Map;

import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import br.gov.rj.rio.iplanrio.aceitacao.infra.BaseAceitacaoTest;
import br.gov.rj.rio.iplanrio.aceitacao.prefrio.page.HomePage;
import br.gov.rj.rio.iplanrio.aceitacao.prefrio.page.LoginPage;
import br.gov.rj.rio.iplanrio.aceitacao.prefrio.page.ServicosPage;
import java.io.IOException;

@Test
public class ServicosTest extends BaseAceitacaoTest {
	  private final OpenAIService aiService = new OpenAIService(); 
	  private static  String TOPICO_ESPERADO = null;
	  
	  private boolean validarConteudoPorIA(String topicoReferencia, String conteudoPagina) {
  	    try {
  	        // Chama o método da classe OpenAIService
  	        String iaResponse = aiService.getValidationResponse(topicoReferencia, conteudoPagina);
  	        
  	        // Normaliza e verifica a resposta (espera-se "SIM" ou "NÃO")
  	        String normalizedResponse = iaResponse.toUpperCase().trim();
  	        
  	        // Retorna TRUE se a IA confirmar o conteúdo
  	        return normalizedResponse.equals("SIM");
  	        
  	    } catch (IOException e) {
  	        // É importante registrar (logar) falhas de comunicação com a API
  	        System.err.println("ERRO na comunicação com a API OpenAI: " + e.getMessage());
              e.printStackTrace();
  	        
              // Decisão: Falhar o teste se a API não puder ser contatada?
              // Geralmente, sim, para não dar um falso positivo.
  	        return false; 
  	    }
  	}
		
	 
	
	@Override
	protected void executaPassos(Map<String, String> dado) throws Exception {
		
		
		// 1. Instância do serviço de IA (lê a chave de API do sistema)
	  
		TOPICO_ESPERADO = dado.get("topico"); 
		
			HomePage homePage = new HomePage(driver);
  		
			homePage.chamarURL();
			
			ServicosPage servicoPage = new ServicosPage(driver);

			servicoPage. selecionaServicoCidade();
			
			//aqui vamos ler o tópico para análise do juiz
			
			  
			
// --- INÍCIO DA VALIDAÇÃO COM IA ---
			
            // 3. Captura o conteúdo principal da página após o clique.
            // É crucial escolher um elemento que contenha o texto central (ex: body ou uma div principal).
            String conteudoPagina = driver.findElement(By.tagName("body")).getText();
            
            // 4. Validação da página via API OpenAI
            // O método validarConteudoPorIA retornará TRUE se o conteúdo for relevante.
            boolean isContentValid = validarConteudoPorIA(TOPICO_ESPERADO, conteudoPagina);

            // 5. Usa Assert do TestNG para verificar o resultado da IA
            Assert.assertTrue(
                isContentValid, 
                "Falha na validação semântica da IA: O conteúdo da página não se refere ao tópico esperado: " + TOPICO_ESPERADO
            );
            
            

	}


	@Override
	@BeforeClass
	protected void beforeClass() {
		umTestador.carregaUmaPlanilha("Login");
	  	umTestador.defineSeDeveReportarNoJira(false);
	  	umTestador.defineSeDeveReportarNoTestlink(false);
		 
	  	
	}
	
 

}