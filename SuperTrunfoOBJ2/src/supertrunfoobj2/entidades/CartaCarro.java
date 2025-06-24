package supertrunfoobj2.entidades;

public class CartaCarro extends Carta {
    private int cilindrada;
    private int potencia;
    private int rotacoes;
    private int velocidade;
    private double comprimento;
    private boolean superTrunfo;

    public CartaCarro(String nome, String codigo,
                      int cilindrada, int potencia,
                      int rotacoes, int velocidade,
                      double comprimento,
                      boolean superTrunfo) {
        super(nome, codigo);
        setCilindrada(cilindrada);
        setPotencia(potencia);
        setRotacoes(rotacoes);
        setVelocidade(velocidade);
        setComprimento(comprimento);
        setSuperTrunfo(superTrunfo);
    }

    public int getCilindrada() {
        return cilindrada;
    }

    public void setCilindrada(int cilindrada) {
        if (cilindrada <= 0) throw new IllegalArgumentException("Cilindrada deve ser >0");
        this.cilindrada = cilindrada;
    }

    public int getPotencia() {
        return potencia;
    }

    public void setPotencia(int potencia) {
        if (potencia <= 0) throw new IllegalArgumentException("Potência deve ser >0");
        this.potencia = potencia;
    }

    public int getRotacoes() {
        return rotacoes;
    }

    public void setRotacoes(int rotacoes) {
        if (rotacoes <= 0) throw new IllegalArgumentException("Rotações devem ser >0");
        this.rotacoes = rotacoes;
    }

    public int getVelocidade() {
        return velocidade;
    }

    public void setVelocidade(int velocidade) {
        if (velocidade <= 0) throw new IllegalArgumentException("Velocidade deve ser >0");
        this.velocidade = velocidade;
    }

    public double getComprimento() {
        return comprimento;
    }

    public void setComprimento(double comprimento) {
        if (comprimento <= 0) throw new IllegalArgumentException("Comprimento deve ser >0");
        this.comprimento = comprimento;
    }

    public boolean isSuperTrunfo() {
        return superTrunfo;
    }

    public void setSuperTrunfo(boolean superTrunfo) {
        this.superTrunfo = superTrunfo;
    }

    @Override
    public String toString() {
        return String.format("%s | Cil: %dcc | Pot: %d | RPM: %d | Vel: %dkm/h | Comp: %.2fm%s",
                super.toString(), cilindrada, potencia, rotacoes, velocidade, comprimento,
                superTrunfo ? " [SUPER TRUNFO]" : "");
    }
}
