import java.util.Optional;

public class AuthService {
    private final UserRepository userRepository = new UserRepository();

    public void register(String username, String email, String password, String confirmPassword) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome de usuário não pode estar vazio.");
        }
        if (email == null || !email.contains("@")) {
            throw new IllegalArgumentException("E-mail inválido.");
        }
        if (password == null || password.length() < 8) {
            throw new IllegalArgumentException("A senha deve ter pelo menos 8 caracteres.");
        }
        if (!password.equals(confirmPassword)) {
            throw new IllegalArgumentException("As senhas não coincidem.");
        }

        String salt = PasswordService.generateSalt();
        String passwordHash = PasswordService.hashPassword(password, salt);

        userRepository.save(username, email, passwordHash, salt);
    }

    public User login(String loginInput, String passwordInput) {
        if (loginInput == null || loginInput.trim().isEmpty() || passwordInput == null || passwordInput.trim().isEmpty()) {
            throw new IllegalArgumentException("Preencha todos os campos.");
        }

        Optional<UserRepository.UserWithCredentials> userOpt = userRepository.findByUsernameOrEmail(loginInput);
        if (userOpt.isPresent()) {
            UserRepository.UserWithCredentials uc = userOpt.get();
            if (PasswordService.verifyPassword(passwordInput, uc.passwordHash, uc.salt)) {
                return new User(uc.id, uc.username, uc.email, uc.role);
            }
        }
        throw new IllegalArgumentException("Login ou senha inválidos.");
    }
}
