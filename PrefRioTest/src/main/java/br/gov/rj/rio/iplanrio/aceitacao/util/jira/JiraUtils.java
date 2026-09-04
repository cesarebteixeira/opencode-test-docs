package br.gov.rj.rio.iplanrio.aceitacao.util.jira;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.atlassian.jira.rest.client.api.JiraRestClient;
import com.atlassian.jira.rest.client.api.domain.BasicIssue;
import com.atlassian.jira.rest.client.api.domain.Issue;
import com.atlassian.jira.rest.client.api.domain.Version;
import com.atlassian.jira.rest.client.api.domain.input.AttachmentInput;
import com.atlassian.jira.rest.client.api.domain.input.IssueInput;
import com.atlassian.jira.rest.client.api.domain.input.IssueInputBuilder;
import com.atlassian.util.concurrent.Promise;

public class JiraUtils {

	public static String reportaBug(String casoDeTeste, String chaveDoProjeto, String sumario, String descricao, FileInputStream anexo, String versaoAfetada) throws IOException {

		JiraRestClient jiraRestClient = JiraInstance.getRestClient();
		try {
			System.out.println("inicio");
			String data = new SimpleDateFormat("dd/mm/yyyy hh:mm:ss").format(new Date());
			
			//verifica se o bug já existe
//			jiraRestClient.getSearchClient().searchJql("").
			
			IssueInputBuilder builder = new IssueInputBuilder(chaveDoProjeto, 1L, "[" + casoDeTeste + "] " + sumario + data);
			builder.setDescription(descricao);
			
			//verifica se a versão existe.
			Iterable<Version> listaDeVersoes = jiraRestClient.getProjectClient().getProject(chaveDoProjeto).get().getVersions();
			
			for (Version versao : listaDeVersoes) {
				System.out.println("Pesquisando versão: " + versao.getName());
				if(versao.getName().equals(versaoAfetada)){
			
					List<String> versoes = new ArrayList<String>();
					versoes.add(versaoAfetada);
					builder.setAffectedVersionsNames(versoes);
					
					break;
				}
			}
			
		    IssueInput issueInput = builder.build();
		 
			Promise<BasicIssue> basicIssuePromise = jiraRestClient.getIssueClient().createIssue(issueInput);
			BasicIssue basicIssue = basicIssuePromise.claim();
			
			
			System.out.println("Salvei a issue - claim");
			System.out.println(basicIssue);

			Promise<Issue> issue = jiraRestClient.getIssueClient().getIssue(basicIssue.getKey());
			System.out.println("recuperei uma issue " + issue.get().getAttachmentsUri());
			System.out.println("Vou adicionar o Anexo");
			
			AttachmentInput attachmentInput = new AttachmentInput(casoDeTeste.toLowerCase() + "-tela.png", anexo);
			jiraRestClient.getIssueClient().addAttachments(issue.get().getAttachmentsUri(), attachmentInput).claim();

			System.out.println("Acho que adicionei.");
			
			return issue.get().getKey();
//			URI attachmentsUri = UriBuilder.fromUri(basicIssue.get().getSelf()).path("attachments").build();
//			restClient.getIssueClient().addAttachment(attachmentsUri, new FileInputStream("/tmp/imgTmp.png"), "imgTmp.png");

		}catch (Exception e) {

			e.printStackTrace();
		} 
		finally {
			// cleanup the restClient
//			System.out.println("fechando conexão");
//			jiraRestClient.close();
		}
		return "";
	}
	
	public static String getProperties(String name) {
		Properties properties = new Properties();
		String value = null;
		
		try {
		    properties.load(new FileInputStream("config.properties"));	// se necessitar altere o caminho e/ou o nome do arquivo
		    value = properties.getProperty(name);
		    
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return value;
	}
}
