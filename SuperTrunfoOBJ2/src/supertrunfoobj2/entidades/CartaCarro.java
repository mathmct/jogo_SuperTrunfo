package supertrunfoobj2.entidades;

public class CartaCarro extends Carta {
    private int cilindrada;
    private int potencia;
    private int rotacoes;
    private int velocidade;
    private double comprimento;
    private boolean superTrunfo;
    private String caminhoImagem;

    public CartaCarro(String nome, String codigo,
                      int cilindrada, int potencia,
                      int rotacoes, int velocidade,
                      double comprimento,
                      boolean superTrunfo) throws Exception {
        super(nome, codigo);
        setCilindrada(cilindrada);
        setPotencia(potencia);
        setRotacoes(rotacoes);
        setVelocidade(velocidade);
        setComprimento(comprimento);
        setSuperTrunfo(superTrunfo);
        this.caminhoImagem = "imagens/" + codigo.toLowerCase() + ".png";
    }

    public int getCilindrada() {
        return cilindrada;
    }

    private void setCilindrada(int cilindrada) throws Exception {
        if (cilindrada > 0) {
            this.cilindrada = cilindrada;
        } else {
            throw new Exception("Cilindrada deve ser >0");
        }
    }

    public int getPotencia() {
        return potencia;
    }

    private void setPotencia(int potencia) throws Exception {
        if (potencia > 0) {
            this.potencia = potencia;
        } else {
            throw new Exception("Potência deve ser >0");
        }
    }

    public int getRotacoes() {
        return rotacoes;
    }

    private void setRotacoes(int rotacoes) throws Exception {
        if (rotacoes > 0) {
            this.rotacoes = rotacoes;
        } else {
            throw new Exception("Rotações devem ser >0");
        }
    }

    public int getVelocidade() {
        return velocidade;
    }

    private void setVelocidade(int velocidade) throws Exception {
        if (velocidade > 0) {
            this.velocidade = velocidade;
        } else {
            throw new Exception("Velocidade deve ser >0");
        }
    }

    public double getComprimento() {
        return comprimento;
    }

    private void setComprimento(double comprimento) throws Exception {
        if (comprimento > 0) {
            this.comprimento = comprimento;
        } else {
            throw new Exception("Comprimento deve ser >0");
        }
    }

    public boolean isSuperTrunfo() {
        return superTrunfo;
    }

    private void setSuperTrunfo(boolean superTrunfo) {
        this.superTrunfo = superTrunfo;
    }

    public String getCaminhoImagem() {
        return caminhoImagem;
    }

    @Override
    public String toString() {
    return String.format("%s | Pot: %d | Vel: %dkm/h | Comp: %.2fm | RPM: %d | Cil: %dcc%s",
        super.toString(), potencia, velocidade, comprimento, rotacoes, cilindrada,
        superTrunfo ? " [SUPER TRUNFO]" : "");
}

}
