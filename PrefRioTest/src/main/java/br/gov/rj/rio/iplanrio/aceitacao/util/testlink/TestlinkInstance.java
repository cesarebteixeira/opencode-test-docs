package br.gov.rj.rio.iplanrio.aceitacao.util.testlink;

import java.net.MalformedURLException;
import java.net.URL;

import br.eti.kinoshita.testlinkjavaapi.TestLinkAPI;
import br.eti.kinoshita.testlinkjavaapi.util.TestLinkAPIException;
import br.gov.rj.rio.iplanrio.aceitacao.util.Utils;

/**
 * Classe de conexao com o Testlink
 * 
 * @author Elias Nogueira <elias.nogueira@gmail.com>
 */
public class TestlinkInstance {

    private static TestLinkAPI instance = null;

    /**
     * Retorna uma nova instancia ou a instancia existente da conexao com o Testlink
     * @return instancia de conexao do Testlink
     */
    public static TestLinkAPI getInstance() {
        if (instance == null) {
            try {
                instance = new TestLinkAPI(new URL(Utils.getProperties("testlink.url")), Utils.getProperties("testlink.devkey"));
            } catch (TestLinkAPIException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
        return instance;
    }
	
}
