package SistemaCadastroProdutos.src.model;

public class Produto {
    private String nome;
    private double preco;
    private int quantidade;
    private String categoria;

    public Produto(String nome, double preco, int quantidade, String categoria) {
        this.nome = nome;
        this.preco = preco;
        this.quantidade = quantidade;
        this.categoria = categoria;
    }

    public String getNome() {
        return nome;
    }

    public double getPreco() {
        return preco;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public String getCategoria() {
        return categoria;
    }

    @Override
    public String toString() {
        return nome + " | Preço: R$ " + preco
                + " | Estoque: " + quantidade
                + " | Categoria: " + categoria;
    }
}