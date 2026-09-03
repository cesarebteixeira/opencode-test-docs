
package br.gov.rj.rio.iplanrio.aceitacao.prefrio.test;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import br.gov.rj.rio.iplanrio.aceitacao.infra.BaseAceitacaoTest;
import br.gov.rj.rio.iplanrio.aceitacao.prefrio.page.HomePage;


@Test
public class CapturaLinksTest extends BaseAceitacaoTest{


	

	@Override
	protected void executaPassos(Map<String, String> dado) throws Exception {
		
	
		HomePage homePage = new HomePage(driver);
		homePage.chamarURL();
		
	//	driver.findElement(By.xpath("//img[@alt='IPTU']")).click();
		
		Thread.sleep(1000);
		
		Set<String> todosServicos = new HashSet<>();

		while (true) {
		    // Conta quantos serviços únicos temos ANTES da coleta desta página
		    int sizeAntesDaColeta = todosServicos.size();

		    // 1. COLETAR SERVIÇOS (Seu trecho de código)
		    List<WebElement> servicos = driver.findElements(By.cssSelector("a"));
		    
		    
		    // ... (restante do seu for loop) ...
		    
		    
		    for (WebElement s : servicos) {
		        String serviceText = s.getText().trim();
		        if (!serviceText.isEmpty()) {
		            todosServicos.add(serviceText);
		        }
		//ct        System.out.println("serviço : " + s.getText());
		    }
		    // O print de status deve ficar fora do for loop para não poluir
	//ct	    System.out.println("\nServiços únicos coletados até agora: " + todosServicos.size());

		    // 2. PAGINAÇÃO: Encontra o botão (usando o JS Executor, se necessário)
		    List<WebElement> nextButtons = driver.findElements(By.xpath("//button[@aria-label='Próximo slide' and not(@disabled)]"));

		    // --- CONDIÇÃO DE SAÍDA 1: O botão habilitado não existe ---
		    if (nextButtons.isEmpty()) {
		  //ct      System.out.println("\n--- Fim da Paginação: Botão 'Próximo slide' habilitado não encontrado. ---");
		        break;
		    }

		    // Clica no botão (use o executor JS para evitar o erro de interceptação)
		    WebElement nextButton = nextButtons.get(0);
		    JavascriptExecutor executor = (JavascriptExecutor) driver;
		    executor.executeScript("arguments[0].click();", nextButton);

		    // Espera (usar WebDriverWait é melhor, mas mantemos o Thread.sleep por enquanto)
		    try {
		        Thread.sleep(1500); // Aumentei a espera para 1.5s
		    } catch (InterruptedException e) {
		        Thread.currentThread().interrupt();
		        break;
		    }

		    // --- CONDIÇÃO DE SAÍDA 2: Verifica se algo novo foi coletado ---
		    // Fazemos uma nova coleta (ou o loop irá fazê-lo). 
		    // Para simplificar, vamos verificar na PRÓXIMA iteração, mas podemos forçar a verificação aqui:
		    
		    // **Recomendação:** Deixe esta verificação ocorrer no início da próxima iteração.
		    // Se o loop coletar o mesmo número de serviços na próxima vez (sizeAntesDaColeta == todosServicos.size()), 
		    // significa que o clique não funcionou ou o conteúdo acabou.

		    int sizeAposAvanco = todosServicos.size();

		    // Vamos forçar a coleta para ver se houve mudança (melhor seria coletar na próxima iteração)
		    // Para garantir a detecção de loop, vamos checar no final:
		    // **Se o tamanho não mudou, o clique falhou em avançar o conteúdo.**
		    if (sizeAntesDaColeta == sizeAposAvanco) {
		         System.out.println("🚨 Alerta de Loop: Conteúdo não mudou após o clique. Abortando.");
		         break;
		    }
		}
		
		int contador = 1;
		for (String servico : todosServicos) {
		    System.out.println(contador + ". " + servico);
		    contador++;
		}
     
	}

 

	@Override
	@BeforeClass
	protected void beforeClass() {
		umTestador.carregaUmaPlanilha("home");
		umTestador.defineSeDeveReportarNoJira(false);
		umTestador.defineSeDeveReportarNoTestlink(false);
 

	}
}
