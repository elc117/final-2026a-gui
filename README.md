/![Review Assignment Due Date](https://classroom.github.com/assets/deadline-readme-button-22041afd0340ce965d47ae6ef1cefeee28c7c493a6346c4f15d667ab976d596c.svg)](https://classroom.github.com/a/8MfjtJ-y)

# Auto-Battler (Trabalho Final)

### Coisas para fazer (ordem de prioridade):
1. Sistema de dinheiro (jogador precisa comprar as tropas, limite de dinheiro por nível)
2. Refatorar o código implementar mais unidades
3. Unidades diferentes (mago, healer, ladino, etc)
4. Unidades serem spawnadas em formações diferentes (ex: arqueiro nascem em linha, guerreiros nascem em blocos)
5. Time inimigo com cor oposta (no tinyswords tem a mesma sprite mas vermelha)
6. CARTAS DE HABILIDADE!!! Que mudam o comportamento das tropas, dano, efeitos
7. Adição de efeitos visuais para as cartas, como unidades em chama, congeladas, fugindo.
8. Sistema para avanço de nível
9. MULTIPLAYER

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

## Progresso 13/06
**O que foi feito:**
* Refatoração de alguns códigos muito feios
* Criação de um sistema de seleção de níveis e criação de inimigos
* `LevelManager`, `SelectLevelScreen`

## Progresso 14/06
**O que foi feito:**
* Criada uma classe AnimationManager para carregar as texturas uma única vez quando o jogo abrir, e guardá-las na memória.
* Adicionado background na tela de combate.
* Adicionado sprites de arqueiro e soldado, com animações na região de combate.
* Implementado lógica de animação para espelhar o sprite horizontalmente dependendo a direção do movimento.
* Classe `Projectiles` para as flechas dos arqueiros e magias no futuro

## Progresso 15/06
**O que foi feito:**
* Começando a prototipar as interfaces
* Uso do programa SkinManager para fazer uma skin
* Skin nova para os elementos da UI
* Prototipação dos menus

## Progresso 16/06
**O que foi feito:**
* Implementação das interfaces
* Arrumando alguns erros do código e vazamentos de memória
* Algumas refatorações

## Progresso 17/06
**O que foi feito:**
* Terminando de criar as interfaces
* Começando a deixar o jogo mais divertido de jogar
* 

