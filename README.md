# Super Trunfo - Jogo de Cartas em Java Este projeto implementa o clássico jogo de cartas **Super Trunfo**, desenvolvido em Java com interface gráfica (Swing), comunicação em rede, e estrutura orientada a objetos. ##
## Esse projeto não tem fins lucrativos, apenas acadêmicos. Foi desenvolvido como projeto final da disciplina Programação Orientada a Objetos (ARQOBJ2), do curso Tecnólogo em Análise e Desenvolvimento de Sistemas, do IFSP Campus Araraquara, sob orientação do Professor Dr. Mario Popolin Neto

🎮 Funcionalidades
- Jogo entre dois jogadores via conexão TCP, onde a partida é gerenciada por thread
- Sistema de rodadas com escolha de atributos
- Exibição da carta do oponente após jogada
- Cartas lidas de arquivo XML (`cartas.xml`)
- Registro de log da partida em XML (`log_partidas.xml`)
- Sons de vitória, derrota e fim de jogo personalizados
- Interface gráfica estilizada com tema escuro e fonte moderna
- Painel dinâmico com imagem da carta atual

## Regras do Jogo:
- Super Trunfo é um jogo de cartas comparativas em que cada jogador recebe uma pilha de cartas com atributos numéricos. A cada rodada:

    ## O jogador da vez escolhe um atributo para competir.

    ## Ambos revelam suas cartas e comparam o valor do atributo escolhido.

    ## Quem tiver o maior valor vence a rodada e ganha a carta do oponente.

    ## Em caso de empate, cada jogador mantém sua carta.

    ## A carta especial Super Trunfo vence todas as outras, exceto aquelas com código iniciando em “A”.

    ## Vence o jogo quem ficar com todas as cartas ao final das rodadas.
