import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class VerificadorIA {
    public static final String OLLAMA_URL = "http://localhost:11434/api/generate";
    public static final String MODELO = "llama3.2";

    public static String analisarMensagem(String mensagemOriginal) {
        String prompt = montarPrompt(mensagemOriginal);
        String corpoJson = montarCorpoRequisicao(prompt);

        HttpClient client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(5))
                .build();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(OLLAMA_URL))
                .header("Content-Type", "application/json")
                .timeout(Duration.ofSeconds(30))
                .POST(HttpRequest.BodyPublishers.ofString(corpoJson))
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                return "Não foi possível consultar a IA (status: " + response.statusCode() + ")."
                        + "Verifique se o Ollama está rodando.";
            }

            return extrairCampoResponse(response.body());

        } catch (Exception e) {
            return "IA indisponível no momento (Ollama não respondeu) Análise por palavras-chave permance ativa.";
        }
        }
    }
    private static String montarPrompt(String mensagem) {
        return "Você é um analista de segurança digital. Analise a mensagem abaixo e responda em"
        + "português, de forma curta e objetiva, sobre:\n"
        + "1) Erros de ortografia/gramática incomuns (comuns em golpes)\n"
        + "2) Uso excessivo ou estranho de emojis\n"
        + "3) Uma nota de 0 a 10 de quão suspeita a escrita parece ser um golpe\n\n"
        + "Mensagem: \"" + mensagem + "\"";
    }

    private static String montarCorpoRequisicao(String prompt) {
        String promptEscapado = prompt
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n");

        return "{"
                + "\"model\":\"" + VerificadorIA.MODELO + "\","
                +"\"prompt\":\"" + promptEscapado + "\","
                + "\"stream\":false"
                + "}";
    }

    private static String extrairCampoResponse(String jsonBody) {
        String chave = "\"response\":\"";
        int inicio = jsonBody.indexOf(chave);
        if (fim == -1) fim = jsonBody.length();

        return jsonBody.substring(inicio, fim)
                .replace("\\n", "\n")
                .replace("\\\"", "\"");
    }
}
