public class URLSuspeita {

    public static String avaliarRisco(String url) {
        String u = url.trim().toLowerCase();

        if (!u.startsWith("http://") && !u.startsWith("https://")) {
            return "ALTO RISCO: O link usa um protocolo desconhecido ou formato inválido.";
        } else if (u.startsWith("http://")) {
            return "MÉDIO RISCO: O site não usa conexão segura (HTTPS).";
        }

        String[] bloqueados = {
                "hackers.com", "ganhedinheiro", "promocaofalsa", "cliqueja",
                "urgente", "bloqueio", "atualize", "sorteio"
        };

        for (String palavra : bloqueados) {
            if (u.contains(palavra)) {
                return "MÉDIO RISCO: A URL contém termos suspeitos (" + palavra + ").";
            }
        }

        return "BAIXO RISCO: Nenhum padrão óbvio de golpe foi detectado.";
    }
}