package SistemaCadastroProdutos.src.exceptions;

public class ProdutoDuplicadoException extends Exception {
    public ProdutoDuplicadoException() {
        super("Já existe um produto com esse nome.");
    }

    public ProdutoDuplicadoException(String mensagem) {
        super(mensagem);
    }
}