/![Review Assignment Due Date](https://classroom.github.com/assets/deadline-readme-button-22041afd0340ce965d47ae6ef1cefeee28c7c493a6346c4f15d667ab976d596c.svg)](https://classroom.github.com/a/8MfjtJ-y)

# Auto-Battler (Trabalho Final)

### Coisas para fazer:
* (Resolvido) Resolver bug de acesso fora da matriz: na PreparationScreen, jogo crasha quando insere tropa nas linhas do topo.


## Progresso 08/06
Nesta etapa inicial, estabelecemos a fundação do projeto cumprindo os requisitos de ambiente e as diretrizes de visualização para a web.

**O que foi feito:**
* Geração do esqueleto do projeto utilizando `gdx-liftoff`, garantindo a separação entre os módulos `core`, `desktop` (lwjgl3) e `html`.
* Configuração da classe principal herdeira de `Game` para gerenciamento de estados.
* Implementação do `FitViewport` (1280x720) para garantir que o campo de batalha mantenha a proporção correta e não sofra distorções quando renderizado no navegador via Itch.io.
* Criação dos protótipos iniciais das telas de Menu (`MainMenuScreen`), Preparação (`PreparationScreen`) e Combate (`CombatScreen`) com posicionamento preliminar da grid de batalha.

## Progresso 09/06
**O que foi feito:**
* Criação de uma classe abstrata `BaseScreen` para implementar as outras screens
* Criação da classe UIFactory para facilitar a criação de botões

## Progresso 10/06
**O que foi feito:**
* Spawn das unidades na tela `PreparationScreen`
* Tela de combate renderizando um protótipo das unidades
* Bugfix para não por unidades no mesmo grid

## Progresso 11/06
**O que foi feito:**
* Correção do bug de acesso fora da matriz em PreparationScreen.java.
* Comportamento de flocking baseado em Craig Reynolds
* Comportamento de seek modular para facilitar o desenvolvimento depois
* Optamos por não utilizar mais o `gdx-ai` para deixar o jogo o mais leve possível
