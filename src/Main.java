
import java.util.Arrays;
import java.util.Scanner;


public class Main {
    public static String[] processamentoMsg(String msg) {
        String msgMinuscula = msg.toLowerCase();
        String semPontuacao = msgMinuscula.replaceAll("\\p{P}", "");
        String[] palavras = semPontuacao.split(" ");
        return palavras;
    }
    public static boolean processamentodadospessoais(String[] msg){
        return Arrays.stream(msg).anyMatch(PalavrasGolpe.DADOS_PESSOAIS::contains);
    }

    public static boolean acaousuario(String[] msg) {
        return Arrays.stream(msg).anyMatch(PalavrasGolpe.ACAO_USUARIO::contains);
    }

    public static boolean ameacas(String[] msg) {
        return Arrays.stream(msg).anyMatch(PalavrasGolpe.AMEACAS::contains);
    }

    public static void main(String[] args) {
        System.out.println("Olá, este é o programa PareceFalso!");
        System.out.println("Este programa tem o intuito de te ajudar e auxiliar para não cair em golpes \n");
        Scanner scanner = new Scanner(System.in);

        int opcao = 0;
        while (opcao != 4) {
            System.out.println("=== MENU ===");
            System.out.println("1 - Identificar mensagem suspeita de golpe \n" +
                    "2 - Identificar URL suspeita de golpe \n" +
                    "3 - Dicas de como não cair em mensagens fraudulentas \n" +
                    "4 - Sair do programa \n");

            System.out.print("Digite uma opção: ");
            opcao = scanner.nextInt();

            switch (opcao) {
                case 1:
                    System.out.println("Opção 1 - Indentificar mensagem suspeita de golpe");
                    System.out.print("Digite a mensagem que você acha suspeita: ");
                    String mnsg = scanner.nextLine();
                    break;

                case 2:
                    System.out.println("Opção 2 - Indentificar URL suspeita de golpe");
                    System.out.print("Digite sla a URL que você acha suspeita: ");
                    String url = scanner.nextLine();
                    break;

                case 3:
                    System.out.println("Opção 3 - Dicas para não em mensagens fraudulentas");
                    System.out.println("Desconfie de urgência excessiva, verifique links e canais \n" +
                            "oficiais, e jamais compartilhe senhas ou códigos de confirmação. Orientações \n" +
                            "adicionais e práticas de segurança ajudam a manter seus dados e dinheiro protegidos\n" +
                            "no dia a dia. \n");
                    break;
                case 4:
                    System.out.println("Saindo do programa...");
                    break;
                default:
                    System.out.println("Opção Inválida! Tente novamente.\n");
            }
        }
    }
}