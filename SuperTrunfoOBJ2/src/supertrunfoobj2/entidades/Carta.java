package supertrunfoobj2.entidades;

public abstract class Carta {
    private String nome;
    private String codigo;

    public Carta(String nome, String codigo) throws Exception {
        setNome(nome);
        setCodigo(codigo);
    }

    public String getNome() {
        return nome;
    }

    private void setNome(String nome) throws Exception {
        if (nome != null) {
            this.nome = nome;
        } else {
            throw new Exception("Nome não pode ser nulo");
        }
    }

    public String getCodigo() {
        return codigo;
    }

    private void setCodigo(String codigo) throws Exception {
        if (codigo != null) {
            this.codigo = codigo;
        } else {
            throw new Exception("Código não pode ser nulo");
        }
    }

    @Override
    public String toString() {
        return String.format("%s (%s)", nome, codigo);
    }
}
