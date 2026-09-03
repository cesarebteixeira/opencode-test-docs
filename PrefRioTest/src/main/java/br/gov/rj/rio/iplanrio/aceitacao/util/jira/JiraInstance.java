package br.gov.rj.rio.iplanrio.aceitacao.util.jira;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;

import br.gov.rj.rio.iplanrio.aceitacao.util.Utils;

import com.atlassian.jira.rest.client.api.JiraRestClient;
import com.atlassian.jira.rest.client.internal.async.AsynchronousJiraRestClientFactory;

public class JiraInstance {

	private static JiraInstance instance = null;
	private static JiraRestClient jiraRestClient = null;

	private JiraInstance() throws URISyntaxException {

		URI jiraServerUri = new URI(Utils.getProperties("jira.uri"));
		AsynchronousJiraRestClientFactory factory = new AsynchronousJiraRestClientFactory();
		jiraRestClient = factory.createWithBasicHttpAuthentication(jiraServerUri, Utils.getProperties("jira.user"), Utils.getProperties("jira.pass"));
	}

	public static JiraInstance getInstance() {

		if (jiraRestClient == null) {
			try {

				instance = new JiraInstance();
			} catch (URISyntaxException e) {
				Logger.getLogger(JiraInstance.class.getName()).log(Level.SEVERE, null, e);
			}

			catch (Exception e) {
				Logger.getLogger(JiraInstance.class.getName()).log(Level.SEVERE, null, e);
			}
		}

		return instance;
	}
	
	public static JiraRestClient getRestClient() {

		if(jiraRestClient == null){
			getInstance();
		}

		return jiraRestClient;
	}

}
