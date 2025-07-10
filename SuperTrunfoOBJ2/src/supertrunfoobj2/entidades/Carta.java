package supertrunfoobj2.entidades;

public abstract class Carta {
    private String nome;
    private String codigo;

    public Carta(String nome, String codigo) {
        setNome(nome);
        setCodigo(codigo);
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("Nome não pode ser vazio");
        }
        this.nome = nome;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        if (codigo == null || codigo.isBlank()) {
            throw new IllegalArgumentException("Código não pode ser vazio");
        }
        this.codigo = codigo;
    }

    @Override
    public String toString() {
        return String.format("%s (%s)", nome, codigo);
    }
}
