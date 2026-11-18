package SistemaCadastroProdutos.src.exceptions;

public class PrecoInvalidoException extends Exception {
    public PrecoInvalidoException() {
        super("O preço do produto é inválido.");
    }

    public PrecoInvalidoException(String mensagem) {
        super(mensagem);
    }
}