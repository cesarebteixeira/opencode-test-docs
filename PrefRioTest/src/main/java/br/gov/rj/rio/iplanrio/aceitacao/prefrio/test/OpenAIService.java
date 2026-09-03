package br.gov.rj.rio.iplanrio.aceitacao.prefrio.test;
import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class OpenAIService {

    private static final String API_URL = "https://api.openai.com/v1/chat/completions";
    private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

    private final OkHttpClient client = new OkHttpClient();
    private final Gson gson = new Gson();
    private final String apiKey;

    private static final String ENV_VAR_KEY = "OPENAI_API_KEY";

    public OpenAIService() {
        String key = System.getenv(ENV_VAR_KEY);

        if (key == null || key.isEmpty()) {
            key = System.getProperty(ENV_VAR_KEY);
        }

      //  System.out.println("API KEY LIDA: " + key);

        if (key == null || key.isEmpty()) {
            throw new IllegalArgumentException("OPENAI_API_KEY não definido.");
        }

        this.apiKey = key;
    }

    // =========================================================
    //  NÍVEL 2 — validar coerência entre Categoria e Título
    // =========================================================
    public String validarTitulo(String categoria, String titulo) throws IOException {

        String systemPrompt =
            "Você é um validador de estrutura de portais governamentais.\n" +
            "Sua tarefa é avaliar se um TÍTULO DE SERVIÇO pertence ao TÓPICO PAI informado.\n\n" +
            "Regras:\n" +
            "- Se o título fizer sentido dentro do tópico → responda somente: SIM\n" +
            "- Se não fizer sentido → responda: NÃO - <motivo curto> e explique a sua motivação - \n" +
            "- Motivo deve ter no máximo 12 palavras.\n" +
            "- Não avalie conteúdo de página, avalie apenas a relação lógica entre os títulos.\n";

        String userPrompt =
            "Tópico pai: " + categoria + "\n" +
            "Título do serviço: " + titulo + "\n" +
            "O título pertence a este tópico?";

        return enviarParaOpenAI(systemPrompt, userPrompt);
    }

    // =========================================================
    //  NÍVEL 3 — validar se o conteúdo da página corresponde ao título clicado
    // =========================================================
    public String validarConteudo(String tituloServico, String conteudoPagina) throws IOException {

        String trimmed = conteudoPagina.substring(0, Math.min(12000, conteudoPagina.length()));

        String systemPrompt =
            "Você é um validador de páginas de serviços governamentais.\n\n" +
            "Sua tarefa é verificar se o CONTEÚDO DA PÁGINA corresponde ao TÍTULO DO SERVIÇO.\n\n" +
            "Regras:\n" +
            "- Se a página tratar claramente do serviço → responda: SIM\n" +
            "- Se não houver relação → responda: NÃO - <motivo curto>\n" +
            "- Motivo deve ter no máximo 20 palavras.\n" +
            "- Conteúdo genérico (ex: cookies) → NÃO\n";

        String userPrompt =
            "Título do serviço: " + tituloServico + "\n\n" +
            "Conteúdo da página:\n" + trimmed + "\n\n" +
            "A página corresponde ao título informado?";

        return enviarParaOpenAI(systemPrompt, userPrompt);
    }

    // =========================================================
    //  Função base para enviar requisições
    // =========================================================
    private String enviarParaOpenAI(String systemPrompt, String userPrompt) throws IOException {

        JsonObject body = new JsonObject();
        body.addProperty("model", "gpt-4o-mini");

        JsonArray messages = new JsonArray();

        JsonObject sys = new JsonObject();
        sys.addProperty("role", "system");
        sys.addProperty("content", systemPrompt);

        JsonObject usr = new JsonObject();
        usr.addProperty("role", "user");
        usr.addProperty("content", userPrompt);

        messages.add(sys);
        messages.add(usr);
        body.add("messages", messages);

        Request request = new Request.Builder()
            .url(API_URL)
            .header("Authorization", "Bearer " + apiKey)
            .header("Content-Type", "application/json")
            .post(RequestBody.create(JSON, body.toString()))
            .build();

        Response response = client.newCall(request).execute();

        if (!response.isSuccessful()) {
            throw new IOException("Erro HTTP: " + response.code() + " - " + response.body().string());
        }

        JsonObject json = gson.fromJson(response.body().string(), JsonObject.class);
        return json.getAsJsonArray("choices")
                   .get(0).getAsJsonObject()
                   .getAsJsonObject("message")
                   .get("content").getAsString()
                   .trim();
    }
}