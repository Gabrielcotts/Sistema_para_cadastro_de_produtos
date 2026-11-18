package SistemaCadastroProdutos.src.exceptions;

public class NomeInvalidoException extends Exception {
    public NomeInvalidoException() {
        super("O nome do produto é inválido.");
    }

    public NomeInvalidoException(String mensagem) {
        super(mensagem);
    }
}