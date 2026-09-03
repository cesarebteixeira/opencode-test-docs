package br.gov.rj.rio.iplanrio.aceitacao.infra;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;

import com.google.common.base.Function;

import br.gov.rj.rio.iplanrio.aceitacao.prefrio.page.LoginPage;


public   class DSL   {
 

	static   WebDriver driver;		

	public DSL(WebDriver driver) {
		
		DSL.driver = driver;
/*		
		 static  WebDriver driver;
		 static  DSL dsl;	
		 
		public LoginPage(WebDriver driver) {
			
			LoginPage.driver = driver;
			LoginPage.dsl = new DSL(driver);
			*/
		
	}


	//** TextField e  Text Area
	
	public void escrever(String id_campo, String texto) {
	
		driver.findElement(By.id(id_campo)).clear();//para evitar de pegar "lixo"

		driver.findElement(By.id(id_campo)).sendKeys(texto);

	}
	
	public void escrever(By by, String texto) {
		driver.findElement(by).sendKeys(texto);
	}	
	
	
	public String obterValorCampo(String id_campo) {
		
		return	driver.findElement(By.id(id_campo)).getAttribute("value");
	}
	//******************* FIM TextField e  Text Area
	
	//Radio e check
	public void clicarRadio(String id) {
		
		driver.findElement(By.id(id)).click();
	}
	
	public void clicarRadio(By by) {
		
		driver.findElement(by).click();
	}
	
	public boolean isRadioMarcado(String id) {
		
		return driver.findElement(By.id(id)).isSelected();
	}
	
public boolean isRadioMarcado(By by) {
		
		return driver.findElement(by).isSelected();
	}
	
	
	public void clicarCheckBox(String id) {
		driver.findElement(By.id(id)).click();
	}
	public void clicarCheckBox(By by) {
		driver.findElement(by).click();
	}
	
public boolean isCheckMarcado(String id) {
		
		return driver.findElement(By.id(id)).isSelected();
	}
public boolean isCheckMarcado(By by) {
	
	return driver.findElement(by).isSelected();
}


//***** FIM Radio e Chck Box

//************Combo
public void selecionarCombo(String id, String valor ) {
	

	WebElement findElement = driver.findElement(By.id(id));
	Select combo = new Select(findElement);
	// combo.selectByIndex(3);
	combo.selectByVisibleText(valor);
	}

public void selecionarCombo(By by, String valor ) {
	

	WebElement findElement = driver.findElement(by);
	Select combo = new Select(findElement);
	// combo.selectByIndex(3);
	combo.selectByVisibleText(valor);
	}

public void deselecionarCombo(String id, String valor ) {
	

	WebElement findElement = driver.findElement(By.id(id));
	Select combo = new Select(findElement);
	// combo.selectByIndex(3);
	combo.deselectByVisibleText(valor);
	}
public void deselecionarCombo(By by, String valor ) {
	

	WebElement findElement = driver.findElement(by);
	Select combo = new Select(findElement);
	// combo.selectByIndex(3);
	combo.deselectByVisibleText(valor);
	}

public String obterValorCombo(String id) {
	

	WebElement findElement = driver.findElement(By.id(id));
	Select combo = new Select(findElement);
	// combo.selectByIndex(3);
	return combo.getFirstSelectedOption().getText();
	}

public String obterValorCombo(By by) {
	

	WebElement findElement = driver.findElement(by);
	Select combo = new Select(findElement);
	// combo.selectByIndex(3);
	return combo.getFirstSelectedOption().getText();
	}

 public List<String> obterValoresCombo(String id ) {

		WebElement findElement = driver.findElement(By.id(id));
		Select combo = new Select(findElement);
		List<WebElement> allSelectOptions = combo.getAllSelectedOptions(); //pega todas as op��es selecionadas, 
		//por�m quando ele vai retornar ele  est� retornando uma lista de webelement
		//ent�o eu estou convertendo tudo para uma lista de string
		List<String> valores = new  ArrayList<String>();
		for (WebElement opcao : allSelectOptions) {
			valores.add(opcao.getText());
		}
		return valores; //note que pegamos o webelement e coloco em uma lista para retornar
	 
 }
 

 
 public List<String> obterValoresCombo(By by ) {

		WebElement findElement = driver.findElement(By.id("elementosForm:esportes"));
		Select combo = new Select(findElement);
		List<WebElement> allSelectOptions = combo.getAllSelectedOptions(); //pega todas as op��es selecionadas, 
		//por�m quando ele vai retornar ele  est� retornando uma lista de webelement
		//ent�o eu estou convertendo tudo para uma lista de string
		List<String> valores = new  ArrayList<String>();
		for (WebElement opcao : allSelectOptions) {
			valores.add(opcao.getText());
		}
		return valores; //note que pegamos o webelement e coloco em uma lista para retornar
	 
}
 
 public int obterQuantidadeDeOpcoesCombo(String id) {
	 
	 WebElement findElement = driver.findElement(By.id(id));
	 Select combo = new Select(findElement);
	 List<WebElement> options = combo.getOptions();
	 return options.size();
	 
 }
 
 public int obterQuantidadeDeOpcoesCombo(By by) {
	 
	 WebElement findElement = driver.findElement(by);
	 Select combo = new Select(findElement);
	 List<WebElement> options = combo.getOptions();
	 return options.size();
	 
 }

 public boolean verificarOcaoCombo(String id, String opcao) {
	 
	 WebElement findElement = driver.findElement(By.id(id));
	
	 Select combo = new Select(findElement);
	 //criar uma lista com todas as op��es que foram marcadas
	 
	 List<WebElement> options = combo.getOptions();
	 
	 for(WebElement option : options) {
		 
		 if(option.getText().equals(opcao)) {
			 return true;
		 }
	 }
	 return false;
 }
 
 public boolean verificarOcaoCombo(By by, String opcao) {
	 
	 WebElement findElement = driver.findElement(by);
	
	 Select combo = new Select(findElement);
	 //criar uma lista com todas as op��es que foram marcadas
	 
	 List<WebElement> options = combo.getOptions();
	 
	 for(WebElement option : options) {
		 
		 if(option.getText().equals(opcao)) {
			 return true;
		 }
	 }
	 return false;
 }
 
 //######################### Bot�o
 
 public void clicarBotao(String id) {
	 driver.findElement(By.id(id)).click();
 }
 
 public void clicarBotao(By by) {
	 driver.findElement(by).click();
 }
 
 public String obterValueElemento(String id) {
	 
	 return (driver.findElement(By.id(id)).getAttribute("value"));
 }
 
 public String obterValueElemento(By by) {
	 
	 return (driver.findElement(by).getAttribute("value"));
 }
 
 //############################ LINK
 
 public void clicarLink(String linkText) {
	 
	 driver.findElement(By.linkText(linkText)).click();
 
 }
 

 
 //#################### TEXTOS
 
 public String obterGetText(By by) {
	 
	 return driver.findElement(by).getText();
 }
 
 public String obterGetText(String id) {
	 
	 return driver.findElement(By.id(id)).getText();
 }
 
 public String obterGetAttribute(By by, String value) {
	 
	 return driver.findElement(by).getAttribute(value);
 }
 
 public String obterGetAttribute(String id, String value) {
	 
	 return driver.findElement(By.id(id)).getAttribute(value);
 }
 
 //##############ALERT
 
 public String alertaObterTexto() {
	 Alert alert = driver.switchTo().alert();
	 return alert.getText();
 }
 
 public String alertaObterTextoEAceita() {
	 
	 Alert alert = driver.switchTo().alert();
	String valor =  alert.getText();
	alert.accept();
	return valor;
 }
 
 public String alertaObterTextoENega() {
	 
	 Alert alert = driver.switchTo().alert();
	String valor =  alert.getText();
	alert.dismiss();
	return valor;
 }
 
 public void alertaEscrever(String valor) {
	 
	 Alert alert = driver.switchTo().alert();
	 alert.sendKeys(valor);
	 alert.accept();//para fechar o alerta
 }
 
 //########### FRAMES E JANELAS
 
 public void entrarFrame(String index) {
	 
	 driver.switchTo().frame(index);	 
	 
 }
 public void sairFrame() {
	 
	 driver.switchTo().defaultContent();//volta para janela principal
 }
 
 public void trocarJanela(String nameOrHandle) {
	 
	 driver.switchTo().window(nameOrHandle);
	 
 }
 ///////////// ************* FIM *************** dsl do curso
public boolean verificarItemSelecionadoNaComboBox(String id , String textoItemSelecionado) {
	WebElement findElement = driver.findElement(By.id(id));
	// encontro o combo e transformo numa inst�nci do select
	Select combo = new Select(findElement);
	List<WebElement> options = combo.getOptions(); // como.getOptions(); >aqui vc clica ctrl+1 e o comando e
													// preenchido automaticament
	Assert.assertEquals(8, options.size());
	// Para verificar se um elemento faz parte da combo
	boolean encontrou = false;

	for (WebElement option : options) {

		if (option.getText().equals(textoItemSelecionado)) {
			encontrou = true;
			break;
		}

	}
	return (encontrou);
}

public boolean verificarItemSelecionadoNaComboBox(By by , String textoItemSelecionado) {
	WebElement findElement = driver.findElement(by);
	// encontro o combo e transformo numa inst�nci do select
	Select combo = new Select(findElement);
	List<WebElement> options = combo.getOptions(); // como.getOptions(); >aqui vc clica ctrl+1 e o comando e
													// preenchido automaticament
	Assert.assertEquals(8, options.size());
	// Para verificar se um elemento faz parte da combo
	boolean encontrou = false;

	for (WebElement option : options) {

		if (option.getText().equals(textoItemSelecionado)) {
			encontrou = true;
			break;
		}

	}
	return (encontrou);
}
 public void selecionarComboEsporte(String id, String tipoEsporte) {
	 
	 WebElement findElement = driver.findElement(By.id(id));
		// encontro o combo e transformo numa inst�nci do select
		Select combo = new Select(findElement);
		//basta selecionar os elementos desejados, individualmente
		
		combo.selectByVisibleText(tipoEsporte);
		combo.selectByVisibleText(tipoEsporte);
		combo.selectByVisibleText(tipoEsporte);
		
		List<WebElement> options = combo.getOptions();
		
		int  encontrouElementoSelecionado = 0;;
		
		for (WebElement option : options) {
			
			if (option.getText().equals(tipoEsporte) &&
					option.isSelected()){
				encontrouElementoSelecionado ++;
			}
			if (option.getText().equals("Karate") &&
					option.isSelected()){
				encontrouElementoSelecionado ++;
			}
			if (option.getText().equals("Corrida") &&
					option.isSelected()){
				encontrouElementoSelecionado ++;
			}
		}
 }
 


public String validarTextoBotaoClicado(String id) {
	WebElement findElement = driver.findElement(By.id(id));
	return (findElement.getAttribute("value")); 
}

public String validarTextoBotaoClicado(By by) {
	WebElement findElement = driver.findElement(by);
	return (findElement.getAttribute("value")); 
}

//tem que verificar como usar os casos de multiplos valores
public List<String> obterValoresComboMultiplos(String string) {
	
	WebElement findElement = driver.findElement(By.id("elementosForm:esportes"));
	// encontro o combo e transformo numa inst�nci do select
	Select combo = new Select(findElement);
	//basta selecionar os elementos desejados, individualmente
	
	combo.selectByVisibleText("Natacao");
	combo.selectByVisibleText("Corrida");
	combo.selectByVisibleText("Karate");
	
	List<WebElement> options = combo.getOptions();
	
	int  encontrouElementoSelecionado = 0;
	
	for (WebElement option : options) {
		
		if (option.getText().equals("Natacao") &&
				option.isSelected()){
			encontrouElementoSelecionado ++;
		}
		if (option.getText().equals("Karate") &&
				option.isSelected()){
			encontrouElementoSelecionado ++;
		}
		if (option.getText().equals("Corrida") &&
				option.isSelected()){
			encontrouElementoSelecionado ++;
		}
	}
	return null;
}
public void deselectCombo(String id, String tipo) {
	
}

public String  obterTextoLink(String textoLink) {
	return driver.findElement(By.linkText(textoLink)).getText();
}

//************ JavaScript
public Object executarJS(String cmd, Object... param) {
 JavascriptExecutor js = (JavascriptExecutor) driver;
 return js.executeScript(cmd, param);
	}

public void clicarBotaoTabela(String colunaBusca, String valor, String colunaBotao, String idTaabela) {//o que esse m�todo precisa fazer ? encontar  coluna em que vou buscar o registro
	
		//1.procurar o elemento que � a nossa tabela
		
		WebElement tabela = driver.findElement(By.id("elementosForm:tableUsuarios"));
		//tudo que eu for buscar, ser� sobre essa tabela
		
		//OBSERVA��O> .. leva para o diret�rio superior�. o ponto(.) � quando quero referenciar o diret�rio corrente
		// Quando eu coloco .// eu estou dizendo para procurar no escopo onde estou, nesse caso na tabela 
		
		/********************* esse c�digo foi extra�do para o metodo obterIndiceColuna
		 */
		int idColuna = obterIndiceColuna(colunaBusca, tabela); //foi derivado da extra��o do c�digo abaixo
		/*///////////////////////////////////////////////////
		List<WebElement> colunas = tabela.findElements(By.xpath(".//(th"));
		//agora estamos com todas as colunas. Basta iteragir com ela procurado pelo titulo passado no colunaBusca
		
		for (int i =0; i<colunas.size(); i++ ) {
			
			int idColuna = -1;
			if (colunas.get(i).getText().equals(colunaBusca)){
				idColuna = i +1 ;//pq i+ 1? A lista � indexada por zero mas o xpath come�a com 1 . Ent�o devemos somar 1 ao idex da lista
				break;
				
			}
		}

		*/
			
	 
		//varrer a coluna para encontrar a linha do registro desejado
		
		List<WebElement> linhas = tabela.findElements(By.xpath(".//tr/td["+idColuna+"]"));
		
	
		
		/*
		 int idLinha = -1;
		for (int i =0; i<linhas.size(); i++ ) {
			
		
			if (linhas.get(i).getText().equals(valor )){
				idLinha = i +1 ;//pq i+ 1? A lista � indexada por zero mas o xpath come�a com 1 . Ent�o devemos somar 1 ao idex da lista
				break;
				
			}
		}
		 */
		
		int idLinha = obterIndiceLinha(valor, tabela, idColuna);
		
		
		//procurar a coluna do bot�o 
		
		int idColunaBotao = obterIndiceColuna(colunaBotao, tabela);
		
		//com isso eu tenho a linha e coluna ou seja a celula onde est� o bot�o que eu quero clicar	
		//basta clicar na celula
		
		WebElement celula = tabela.findElement(By.xpath(".//tr["+idLinha+"]/td["+idColunaBotao+"]"));
		celula.findElement(By.xpath(".//input")).click();
		
	}

protected int obterIndiceLinha(String valor, WebElement tabela, int idColuna) {
	
	List<WebElement> linhas = tabela.findElements(By.xpath("./tbody/tr/td["+idColuna+"]"));
	
	int idLinha = -1;
	for (int i =0; i<linhas.size(); i++ ) {
		
	
		if (linhas.get(i).getText().equals(valor )){
			idLinha = i +1 ;//pq i+ 1? A lista � indexada por zero mas o xpath come�a com 1 . Ent�o devemos somar 1 ao idex da lista
			break;
			
		}
	}
	return idLinha;
}

protected int obterIndiceColuna	(String coluna , WebElement tabela) {
	List<WebElement> colunas = tabela.findElements(By.xpath(".//th"));
	//agora estamos com todas as colunas. Basta iteragir com ela procurado pelo titulo passado no colunaBusca
	int idColuna = -1;
	for (int i =0; i<colunas.size(); i++ ) {
		
	
		if (colunas.get(i).getText().equals(coluna )){
			idColuna = i +1 ;//pq i+ 1? A lista � indexada por zero mas o xpath come�a com 1 . Ent�o devemos somar 1 ao idex da lista
			break;
			
		}
	}
	return idColuna;
	
	 
}


public boolean esperaPeloElemento(final By by, int esperaEmsegundos) {

	return new FluentWait<WebDriver>(driver).withTimeout(esperaEmsegundos, TimeUnit.SECONDS).pollingEvery(100, TimeUnit.MILLISECONDS)
	.ignoring(NoSuchElementException.class).ignoring(StaleElementReferenceException.class)
	.withMessage("n�o encontrei >> " + by).until(new Function<WebDriver, Boolean>() {
		public Boolean apply(WebDriver d) {

			// System.out.println("***" + new
			// SimpleDateFormat("dd/mm/yyyy_hh:mm:ss.SSSXXX").frmat(new
			// Date()));

		  return d.findElement(by).isDisplayed();
		}
	});

}

public boolean esperaPeloElemento(final String id, int esperaEmsegundos) {

	return new FluentWait<WebDriver>(driver).withTimeout(esperaEmsegundos, TimeUnit.SECONDS).pollingEvery(100, TimeUnit.MILLISECONDS)
	.ignoring(NoSuchElementException.class).ignoring(StaleElementReferenceException.class)
	.withMessage("n�o encontrei >> " + id).until(new Function<WebDriver, Boolean>() {
		public Boolean apply(WebDriver d) {

			// System.out.println("***" + new
			// SimpleDateFormat("dd/mm/yyyy_hh:mm:ss.SSSXXX").frmat(new
			// Date()));

		  return d.findElement(By.id(id)).isDisplayed();
		}
	});

}

public boolean esperaPeloElementoEnabled(final By by) {
	return new FluentWait<WebDriver>(driver).withTimeout(59, TimeUnit.SECONDS).pollingEvery(25, TimeUnit.MILLISECONDS)
	.ignoring(NoSuchElementException.class).until(new Function<WebDriver, Boolean>() {
		public Boolean apply(WebDriver d) {

			return d.findElement(by).isEnabled();
		}
	});

}

public boolean esperaPeloElementoEnabled(final String id) {
	return new FluentWait<WebDriver>(driver).withTimeout(59, TimeUnit.SECONDS).pollingEvery(25, TimeUnit.MILLISECONDS)
	.ignoring(NoSuchElementException.class).until(new Function<WebDriver, Boolean>() {
		public Boolean apply(WebDriver d) {

			return d.findElement(By.id(id)).isEnabled();
		}
	});

}

public boolean esperaPeloElementoDisabled(final By by,int esperaEmsegundos) {
	return new FluentWait<WebDriver>(driver).withTimeout(esperaEmsegundos, TimeUnit.SECONDS).pollingEvery(50, TimeUnit.MILLISECONDS)
	.ignoring(NoSuchElementException.class).until(new Function<WebDriver, Boolean>() {
		public Boolean apply(WebDriver d) {

			return !d.findElement(by).isEnabled();
		}
	});

}

public boolean esperaPeloElementoDisabled(final String id,int esperaEmsegundos) {
	return new FluentWait<WebDriver>(driver).withTimeout(esperaEmsegundos, TimeUnit.SECONDS).pollingEvery(50, TimeUnit.MILLISECONDS)
	.ignoring(NoSuchElementException.class).until(new Function<WebDriver, Boolean>() {
		public Boolean apply(WebDriver d) {

			return !d.findElement(By.id(id)).isEnabled();
		}
	});

}

public boolean esperaPeloElementoDesaparecer(final By by, int esperaEmsegundos) {
	return new FluentWait<WebDriver>(driver).withTimeout(esperaEmsegundos, TimeUnit.SECONDS).pollingEvery(25, TimeUnit.MILLISECONDS)
	.ignoring(NoSuchElementException.class).until(new Function<WebDriver, Boolean>() {
		public Boolean apply(WebDriver d) {

			return !(d.findElement(by).isDisplayed());
		}
	});

}
public boolean esperaPeloElementoDesaparecer(final String id, int esperaEmsegundos) {
	return new FluentWait<WebDriver>(driver).withTimeout(esperaEmsegundos, TimeUnit.SECONDS).pollingEvery(25, TimeUnit.MILLISECONDS)
	.ignoring(NoSuchElementException.class).until(new Function<WebDriver, Boolean>() {
		public Boolean apply(WebDriver d) {

			return !(d.findElement(By.id(id)).isDisplayed());
		}
	});

}

public void aguardaTemporizador() {
	 
	driver.manage().timeouts().implicitlyWait(500, TimeUnit.MILLISECONDS);
	
	if (driver.findElement(By.xpath("//*[@src='/portal-ui-web/resources/img/progress_indicator.gif?pfdrid_c=true']")).isDisplayed()) {
	
		esperaPeloElementoDesaparecer(By.xpath("//*[@src='/portal-ui-web/resources/img/progress_indicator.gif?pfdrid_c=true']"), 10);
	}
	driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
	
}

	
}
