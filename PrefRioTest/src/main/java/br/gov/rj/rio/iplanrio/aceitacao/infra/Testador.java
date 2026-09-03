package br.gov.rj.rio.iplanrio.aceitacao.infra;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;

import com.googlecode.seleniumjavaevidence.report.GenerateEvidenceReport;
import com.googlecode.seleniumjavaevidence.selenium.SeleniumEvidence;

import br.gov.rj.rio.iplanrio.aceitacao.util.ReportaErro;
import br.gov.rj.rio.iplanrio.aceitacao.util.SeleniumUtils;
import br.gov.rj.rio.iplanrio.aceitacao.util.SpreadsheetData;
import br.gov.rj.rio.iplanrio.aceitacao.util.Utils;
import br.gov.rj.rio.iplanrio.aceitacao.util.jira.JiraUtils;
import br.gov.rj.rio.iplanrio.aceitacao.util.testlink.TestlinkUtils;


public class Testador {

	private List<Map<String, String>> dadosDaPlanilha;

	private List<SeleniumEvidence> evidencias = new ArrayList<SeleniumEvidence>();

	private String resumoDeErros;

	private boolean testLinkHabilitado = false;

	private boolean jiraHabilitado = false;

	private WebDriver driver;

	private boolean capturaDeMensagensHabilitada = false;

	private String elementoDeMensagem;

	public WebDriver getDriver() {

		driver = SeleniumUtils.getDriver(SeleniumUtils
				.getSeleniumProperties("selenium.browser"));

		return driver;
	}

	
		
	
	
	public Testador() {

	}

	/**
	 * Recupera um driver específico de um browser: firefox, chrome, ie e opera
	 * 
	 * @param browser
	 * @return
	 */
	public WebDriver getDriver(String browser) {
		System.out.println("instanvia com browser:"+ browser);
		return SeleniumUtils.getDriver(browser);
	}

	public void carregaUmaPlanilha(String nomeDaPlanilha) {

		System.out.println("Vou usar a planilha :" + nomeDaPlanilha);

		dadosDaPlanilha = SpreadsheetData.readExcelDataList(nomeDaPlanilha,
				"planilha/arquivo_de_dados_v1.xls", "dados");
		
		System.out.println("já li todos os registrod  da planilha :" + nomeDaPlanilha);


	}

	public List<Map<String, String>> getDadosDaPlanilha() {
		
		return dadosDaPlanilha;
	}

	public Testador registraNoTestLink() {

		return this;
	}

	public Testador registraNoJira() {

		return this;
	}

	public void reportaErroNaEstruturaDoTeste(Exception e,
			Map<String, String> dado, WebDriver driver)
			throws WebDriverException, Exception {

		criaErro(e, dado, driver, "ERRO NA ESTRUTURA DO TESTE: ");

	}

	public void reportaErroNaAplicacao(AssertionError e,
			Map<String, String> dado, WebDriver driver)
			throws WebDriverException, Exception {

		ReportaErro erro = criaErro(e, dado, driver, e.getMessage());

		String issueKey = this.reportaNoJira(erro, dado);

		this.reportaNoTestlink(erro, dado, issueKey);
	}

	private void reportaNoTestlink(ReportaErro erro, Map<String, String> dado,
			String issueKey) {

		if (testLinkHabilitado) {

			int execucao = TestlinkUtils.reportaResultadoExecucao(
					Utils.getProperties("jira.chave"),
					dado.get("planoDeTeste"), dado.get("casoDeTeste"),
					erro.getStackTrace(), issueKey);

			String data = new SimpleDateFormat("dd/mm/yyyy_hh:mm:ss")
					.format(new Date());

			TestlinkUtils.anexaEvidenciaExecucao(execucao, erro.getEvidencia(),
					dado.get("casoDeTeste") + "- Tela de Evidência de erro "
							+ data, erro.getStackTrace(), "ErroExecucao-"
							+ data + ".png");
		}
	}

	private String reportaNoJira(ReportaErro erro, Map<String, String> dado)
			throws IOException {

		String issueKey = null;

		if (jiraHabilitado) {

			issueKey = JiraUtils.reportaBug(
					dado.get("casoDeTeste"),
					Utils.getProperties("jira.chave"),
					erro.getStackTrace()
							+ " - "
							+ new SimpleDateFormat("dd/mm/yyyy_hh:mm:ss")
									.format(new Date()), erro.getStackTrace(),
					Utils.decodeImageToFile(erro.getEvidencia()), null);
		}

		return issueKey;

	}

	private ReportaErro criaErro(Throwable e, Map<String, String> dado,
			WebDriver driver, String mensagem) throws WebDriverException,
			Exception {

		String message = dado.get("planoDeTeste") + " : "
				+ dado.get("casoDeTeste") + " - " + mensagem
				+ e.fillInStackTrace().getMessage();
		evidencias.add(new SeleniumEvidence(message +"\n\n", ((TakesScreenshot) driver)
				.getScreenshotAs(OutputType.BASE64)));

		
		System.out.println("#ct :" + message);
		e.printStackTrace();

		resumeErros(message);

		return new ReportaErro(
				((TakesScreenshot) driver).getScreenshotAs(OutputType.BASE64),
				e.getMessage());
	}

	private void resumeErros(String message) {

		resumoDeErros = resumoDeErros == null ? message : resumoDeErros + "; "
				+ message;
	}

	public void reportaQuePassouNoTeste(Map<String, String> dado,
			WebDriver driver) throws WebDriverException, Exception {
		
	
	//	if(!" ".equals(dado.get("resultadoEsperado"))){	  //usado para burlar um erro no webdriver, momentaneamente ao a func EmitirDarm
			evidencias.add(new SeleniumEvidence(dado.get("planoDeTeste") + ":" + dado.get("casoDeTeste") + " - PASSOU",
					((TakesScreenshot) driver).getScreenshotAs(OutputType.BASE64)));
	//	}
	}

	public void defineSeDeveReportarNoTestlink(boolean habilitado) {

		this.testLinkHabilitado = habilitado;

	}

	public void defineSeDeveReportarNoJira(boolean habilitado) {

		this.jiraHabilitado = habilitado;

	}

	public void geraRelatorioDeEvidencias() throws IOException {

		if (evidencias.size() > 0) {
			Date now = new Date();
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
 
			GenerateEvidenceReport
					.generatePDFEvidence(evidencias,
							Utils.getProperties("jira.chave") + df.format(now),
							"GSP", Utils.getProperties("testlink.projeto"),
							this.resumoDeErros);
 
		}

	}

	public void capturaMensagensDaTela() throws IOException {

		if (capturaDeMensagensHabilitada) {
			String msg = driver.findElement(By.xpath(elementoDeMensagem))
					.getText();
			
			System.out.println(msg);

		}
	}

	public void defineSeDeveCapturarMensagensDeErro(boolean habilitar,
			String elementoDeMensagem) {

		capturaDeMensagensHabilitada = habilitar;
		this.elementoDeMensagem = elementoDeMensagem;

	}





	public void conectaAoBancoDeDados(String string) throws SQLException {
		 

      

                  Connection conexao = ObterConexao();

                  Statement statement = conexao.createStatement();


                  String query = "SELECT sysdate FROM dual";


                  ResultSet resultSet = statement.executeQuery(query);


                  if (resultSet.next()) {

                           System.out.println(resultSet.getDate("sysdate"));

                  }

        }


        private static Connection ObterConexao() {

                  Connection conexao = null;


                  try {

                           Class.forName("oracle.jdbc.driver.OracleDriver");

                           conexao = DriverManager.getConnection(

                                              "jdbc:oracle:thin:@s1153232:1521:p16", "26211010", "carioca_");

                  } catch (ClassNotFoundException e) {

                           e.printStackTrace();

                  } catch (SQLException e) {

                           e.printStackTrace();

                  }


                  return conexao;

        }





}
