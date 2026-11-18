package SistemaCadastroProdutos.src.exceptions;

public class EstoqueInvalidoException extends Exception {
    public EstoqueInvalidoException() {
        super("A quantidade em estoque é inválida.");
    }

    public EstoqueInvalidoException(String mensagem) {
        super(mensagem);
    }
}