import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        AuthService authService = new AuthService();
        User loggedUser = null;

        int opcao = 0;
        while (opcao != 3) {
            System.out.println("\n=== SISTEMA DE LOGIN ===");
            if (loggedUser == null) {
                System.out.println("1. Cadastrar");
                System.out.println("2. Entrar (Login)");
                System.out.println("3. Sair");
            } else {
                System.out.println("1. Ver Área Protegida (Meus Dados)");
                System.out.println("2. Logout (Sair da conta)");
                System.out.println("3. Sair");
            }
            System.out.print("Escolha uma opção: ");

            if (scanner.hasNextInt()) {
                opcao = scanner.nextInt();
                scanner.nextLine();

                if (loggedUser == null) {
                    if (opcao == 1) {
                        System.out.print("Usuário: ");
                        String u = scanner.nextLine();
                        System.out.print("E-mail: ");
                        String e = scanner.nextLine();
                        System.out.print("Senha (mín. 8 caracteres): ");
                        String s = scanner.nextLine();
                        System.out.print("Confirme a senha: ");
                        String c = scanner.nextLine();

                        try {
                            authService.register(u, e, s, c);
                            System.out.println("Sucesso! Utilizador cadastrado.");
                        } catch (Exception ex) {
                            System.out.println("Erro: " + ex.getMessage());
                        }
                    } else if (opcao == 2) {
                        System.out.print("Usuário ou e-mail: ");
                        String l = scanner.nextLine();
                        System.out.print("Senha: ");
                        String p = scanner.nextLine();

                        try {
                            loggedUser = authService.login(l, p);
                            System.out.println("Login bem-sucedido! Bem-vindo, " + loggedUser.username() + "!");
                        } catch (Exception ex) {
                            System.out.println("Login ou senha inválidos.");
                        }
                    }
                } else {
                    if (opcao == 1) {
                        System.out.println("\n--- ÁREA PROTEGIDA ---");
                        System.out.println("ID: " + loggedUser.id());
                        System.out.println("Usuário: " + loggedUser.username());
                        System.out.println("E-mail: " + loggedUser.email());
                    } else if (opcao == 2) {
                        loggedUser = null;
                        System.out.println("Logout realizado com sucesso.");
                        opcao = 0;
                    }
                }
            } else {
                System.out.println("Digite um número válido.");
                scanner.nextLine();
            }
        }
        scanner.close();
        System.out.println("Aplicação encerrada.");
    }
}
