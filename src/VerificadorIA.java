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
    private static String montarPrompt(String mensagem) {
        return "Você é um especialista em segurança digital e detecção de golpes.\n\n"

                + "Analise a mensagem considerando o CONTEXTO COMPLETO e não apenas palavras isoladas.\n"
                + "Um pedido de PIX, CPF, e-mail ou qualquer outro dado NÃO caracteriza um golpe por si só.\n"
                + "Considere como suspeitos apenas quando houver contexto de fraude, manipulação ou engenharia social.\n\n"

                + "Verifique os seguintes pontos:\n"
                + "- Linguagem alarmista ou senso de urgência.\n"
                + "- Pedido incomum de dados pessoais, bancários, senhas ou códigos.\n"
                + "- Pedido de PIX, transferência ou dinheiro acompanhado de pressão ou justificativa suspeita.\n"
                + "- Links suspeitos ou incentivo para clicar em páginas desconhecidas.\n"
                + "- Tentativa de se passar por banco, empresa, governo ou outra instituição.\n"
                + "- Erros incomuns de ortografia ou gramática.\n"
                + "- Uso exagerado de emojis, letras maiúsculas ou pontuação.\n"
                + "- Ameaças, pressão psicológica ou promessas exageradas.\n"
                + "- Outros indícios comuns de golpes digitais.\n\n"

                + "Classifique seguindo estas regras:\n"
                + "- Provavelmente legítima: nenhum indício relevante de golpe.\n"
                + "- Pouco suspeita: apenas um ou dois sinais fracos.\n"
                + "- Suspeita: vários sinais moderados de golpe.\n"
                + "- Parece falso Rick: fortes evidências de tentativa de golpe ou engenharia social.\n\n"

                + "Responda APENAS neste formato:\n\n"

                + "Pontos encontrados:\n"
                + "- ponto 1\n"
                + "- ponto 2\n\n"

                + "Classificação: Provavelmente legítima | Pouco suspeita | Suspeita | Parece falso Rick\n\n"

                + "Regras importantes:\n"
                + "- Seja objetivo.\n"
                + "- Responda em no máximo 5 linhas.\n"
                + "- Não explique seu raciocínio.\n"
                + "- Não faça introduções.\n"
                + "- Não dê dicas.\n"
                + "- Não use markdown.\n"
                + "- Se não houver nenhum indício relevante, escreva 'Nenhum indício relevante encontrado.'\n\n"

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
        if (inicio == -1) return "Não foi possível interpretar a resposta da IA.";

        inicio += chave.length();
        int fim = jsonBody.indexOf("\",\"done\"", inicio);
        if (fim == -1) fim = jsonBody.length();

        return jsonBody.substring(inicio, fim)
                .replace("\\n", "\n")
                .replace("\\\"", "\"");
    }
}
