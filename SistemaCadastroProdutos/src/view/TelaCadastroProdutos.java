package SistemaCadastroProdutos.src.view;

import SistemaCadastroProdutos.src.exceptions.EstoqueInvalidoException;
import SistemaCadastroProdutos.src.exceptions.NomeInvalidoException;
import SistemaCadastroProdutos.src.exceptions.PrecoInvalidoException;
import SistemaCadastroProdutos.src.exceptions.ProdutoDuplicadoException;
import SistemaCadastroProdutos.src.model.Produto;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class TelaCadastroProdutos extends JFrame {

    private JTextField campoNome;
    private JTextField campoPreco;
    private JTextField campoQuantidade;
    private JComboBox<String> comboCategoria;
    private JTextArea areaProdutos;

    private JButton botaoCadastrar;
    private JButton botaoRemover;
    private JButton botaoLimpar;

    private ArrayList<Produto> produtos;

    public TelaCadastroProdutos() {
        super("Sistema de Cadastro de Produtos");

        produtos = new ArrayList<Produto>();

        campoNome = new JTextField(25);
        campoPreco = new JTextField(25);
        campoQuantidade = new JTextField(25);
        comboCategoria = new JComboBox<String>(new String[]{"Selecione uma categoria", "Alimento", "Eletrônico", "Vestuário"});

        areaProdutos = new JTextArea(12, 65);
        areaProdutos.setEditable(false);
        areaProdutos.setText("Nenhum produto cadastrado.");

        botaoCadastrar = new JButton("Cadastrar");
        botaoRemover = new JButton("Remover Selecionado");
        botaoLimpar = new JButton("Limpar");

        Container conteudo = getContentPane();
        conteudo.setLayout(new BorderLayout(10, 10));

        JPanel painelCampos = new JPanel();
        painelCampos.setLayout(new GridLayout(4, 2, 8, 8));
        painelCampos.add(new JLabel("Nome do produto:"));
        painelCampos.add(campoNome);
        painelCampos.add(new JLabel("Preço (ex.: 12.50):"));
        painelCampos.add(campoPreco);
        painelCampos.add(new JLabel("Quantidade em estoque:"));
        painelCampos.add(campoQuantidade);
        painelCampos.add(new JLabel("Categoria:"));
        painelCampos.add(comboCategoria);

        JPanel painelBotoes = new JPanel();
        painelBotoes.setLayout(new FlowLayout());
        painelBotoes.add(botaoCadastrar);
        painelBotoes.add(botaoRemover);
        painelBotoes.add(botaoLimpar);

        JPanel painelLista = new JPanel();
        painelLista.setLayout(new BorderLayout(5, 5));
        painelLista.add(new JLabel("Produtos cadastrados - selecione a linha completa para remover:"), BorderLayout.NORTH);
        painelLista.add(areaProdutos, BorderLayout.CENTER);

        JPanel painelSuperior = new JPanel();
        painelSuperior.setLayout(new BorderLayout(5, 5));
        painelSuperior.add(painelCampos, BorderLayout.CENTER);
        painelSuperior.add(painelBotoes, BorderLayout.SOUTH);

        conteudo.add(painelSuperior, BorderLayout.NORTH);
        conteudo.add(painelLista, BorderLayout.CENTER);

        TrataBotoes tratador = new TrataBotoes();
        botaoCadastrar.addActionListener(tratador);
        botaoRemover.addActionListener(tratador);
        botaoLimpar.addActionListener(tratador);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(820, 470);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private class TrataBotoes implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent evento) {
            if (evento.getSource() == botaoCadastrar) {
                cadastrarProduto();
            } else if (evento.getSource() == botaoRemover) {
                removerProdutoSelecionado();
            } else if (evento.getSource() == botaoLimpar) {
                limparCampos();
            }
        }
    }

    private void cadastrarProduto() {
        try {
            if (comboCategoria.getSelectedIndex() == 0) {
                JOptionPane.showMessageDialog(this,
                        "Selecione uma categoria antes de cadastrar o produto.",
                        "Categoria obrigatória", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String nome = campoNome.getText().trim();
            if (nome.length() < 3) {
                throw new NomeInvalidoException("O nome não pode estar vazio e deve ter pelo menos 3 caracteres.");
            }

            double preco = Double.parseDouble(campoPreco.getText().trim().replace(',', '.'));
            int quantidade = Integer.parseInt(campoQuantidade.getText().trim());

            if (preco <= 0) {
                throw new PrecoInvalidoException("O preço deve ser maior que zero.");
            }

            if (quantidade < 0) {
                throw new EstoqueInvalidoException("A quantidade em estoque não pode ser negativa.");
            }

            for (int i = 0; i < produtos.size(); i++) {
                if (produtos.get(i).getNome().equalsIgnoreCase(nome)) {
                    throw new ProdutoDuplicadoException("Já existe um produto cadastrado com o nome '" + nome + "'.");
                }
            }

            String categoria = (String) comboCategoria.getSelectedItem();
            Produto produto = new Produto(nome, preco, quantidade, categoria);
            produtos.add(produto);

            JOptionPane.showMessageDialog(this, "Produto cadastrado com sucesso!", "Cadastro realizado", JOptionPane.INFORMATION_MESSAGE);

            limparCampos();

        } catch (NumberFormatException erro) {
            JOptionPane.showMessageDialog(this, "Preço e quantidade devem ser numéricos.\n"
                            + "Use um número decimal para o preço e um inteiro para a quantidade.", "Entrada numérica inválida", JOptionPane.ERROR_MESSAGE);

        } catch (NomeInvalidoException erro) { JOptionPane.showMessageDialog(this, erro.getMessage(),
                    "Nome inválido", JOptionPane.ERROR_MESSAGE);

        } catch (PrecoInvalidoException erro) {JOptionPane.showMessageDialog(
                this, erro.getMessage(), "Preço inválido", JOptionPane.ERROR_MESSAGE);

        } catch (EstoqueInvalidoException erro) {JOptionPane.showMessageDialog(
                this, erro.getMessage(), "Estoque inválido", JOptionPane.ERROR_MESSAGE);

        } catch (ProdutoDuplicadoException erro) {JOptionPane.showMessageDialog(
                this, erro.getMessage(), "Produto duplicado", JOptionPane.ERROR_MESSAGE);

        } catch (Exception erro) {
            JOptionPane.showMessageDialog(this, "Ocorreu um erro inesperado durante o cadastro. Tente novamente.",
                    "Erro inesperado", JOptionPane.ERROR_MESSAGE);
        } finally {
            atualizarLista();
        }
    }

    private void removerProdutoSelecionado() {
        try {
            String textoSelecionado = areaProdutos.getSelectedText();

            if (textoSelecionado == null || textoSelecionado.trim().length() == 0) {
                throw new IllegalStateException("Selecione a linha completa de um produto antes de removê-lo.");
            }

            String linhaSelecionada = textoSelecionado.trim();
            boolean encontrou = false;

            for (int i = 0; i < produtos.size(); i++) {
                if (produtos.get(i).toString().equals(linhaSelecionada)) {
                    produtos.remove(i);
                    encontrou = true;
                    break;
                }
            }

            if (!encontrou) {
                throw new IllegalStateException("Não foi possível identificar o produto. Selecione a linha completa.");
            }

            JOptionPane.showMessageDialog(this, "Produto removido com sucesso.",
                    "Remoção realizada", JOptionPane.INFORMATION_MESSAGE);

        } catch (IllegalStateException erro) {JOptionPane.showMessageDialog(
                this, erro.getMessage(), "Nenhum produto selecionado", JOptionPane.WARNING_MESSAGE);

        } catch (Exception erro) {JOptionPane.showMessageDialog(this,
                "Ocorreu um erro inesperado durante a remoção. Tente novamente.",
                "Erro inesperado", JOptionPane.ERROR_MESSAGE);

        } finally {
            atualizarLista();
        }
    }

    private void limparCampos() {
        campoNome.setText("");
        campoPreco.setText("");
        campoQuantidade.setText("");
        comboCategoria.setSelectedIndex(0);
        campoNome.requestFocus();
    }

    private void atualizarLista() {
        String texto = "";

        if (produtos.size() == 0) {
            texto = "Nenhum produto cadastrado.";
        } else {
            for (int i = 0; i < produtos.size(); i++) {
                texto = texto + produtos.get(i).toString() + "\n";
            }
        }
        areaProdutos.setText(texto);
    }
}