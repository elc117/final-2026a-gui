# Auto-Battler: General Deck

### 1. Identificação
* **Nomes:** Guilherme Martini e Guilherme Dapieve
* **Curso:** Sistemas de Informação

### 2. Proposta
O projeto é um jogo do gênero "Auto-Battler" desenvolvido em Java utilizando o framework LibGDX. O objetivo do jogo é montar uma estratégia de posicionamento e melhorias de exército. O jogador compra tropas (como Guerreiros, Arqueiros e Monges), aplica cartas de habilidade que alteram atributos ou adicionam efeitos (como Roubo de Vida ou Flechas Perfurantes), e assiste ao combate automatizado contra tropas inimigas que possuem comportamentos independentes de movimentação (flocking/seek).

### 3. Processo de Desenvolvimento

### Progresso 08/06
Nesta etapa inicial, estabelecemos a fundação do projeto cumprindo os requisitos de ambiente e as diretrizes de visualização para a web.

**O que foi feito:**
* Geração do esqueleto do projeto utilizando `gdx-liftoff`, garantindo a separação entre os módulos `core`, `desktop` (lwjgl3) e `html`.
* Configuração da classe principal herdeira de `Game` para gerenciamento de estados.
* Implementação do `FitViewport` (1280x720) para garantir que o campo de batalha mantenha a proporção correta e não sofra distorções quando renderizado no navegador via Itch.io.
* Criação dos protótipos iniciais das telas de Menu (`MainMenuScreen`), Preparação (`PreparationScreen`) e Combate (`CombatScreen`) com posicionamento preliminar da grid de batalha.

### Progresso 09/06
**O que foi feito:**
* Criação de uma classe abstrata `BaseScreen` para implementar as outras screens
* Criação da classe UIFactory para facilitar a criação de botões

### Progresso 10/06
**O que foi feito:**
* Spawn das unidades na tela `PreparationScreen`
* Tela de combate renderizando um protótipo das unidades
* Bugfix para não por unidades no mesmo grid

### Progresso 11/06
**O que foi feito:**
* Correção do bug de acesso fora da matriz em PreparationScreen.java.
* Comportamento de flocking baseado em Craig Reynolds
* Comportamento de seek modular para facilitar o desenvolvimento depois

### Progresso 13/06
**O que foi feito:**
* Refatoração de alguns códigos muito feios
* Criação de um sistema de seleção de níveis e criação de inimigos
* `LevelManager`, `SelectLevelScreen`

### Progresso 14/06
**O que foi feito:**
* Criada uma classe AnimationManager para carregar as texturas uma única vez quando o jogo abrir, e guardá-las na memória.
* Adicionado background na tela de combate.
* Adicionado sprites de arqueiro e soldado, com animações na região de combate.
* Implementado lógica de animação para espelhar o sprite horizontalmente dependendo a direção do movimento.
* Classe `Projectiles` para as flechas dos arqueiros e magias no futuro

### Progresso 15/06
**O que foi feito:**
* Começando a prototipar as interfaces
* Uso do programa SkinManager para fazer uma skin
* Skin nova para os elementos da UI
* Prototipação dos menus

### Progresso 16/06
**O que foi feito:**
* Implementação das interfaces
* Arrumando alguns erros do código e vazamentos de memória
* Algumas refatorações

### Progresso 17/06
**O que foi feito:**
* Terminando de criar as interfaces
* Começando a deixar o jogo mais divertido de jogar
* Refatorando para conseguirmos adicionar mais unidades

### Progresso 18/06
**O que foi feito:**
* Adicionado sistema de economia
* Adicionado cursor customizado

### Progresso 19/06
**O que foi feito:**
* Adicionado novo personagem: **Monge de cura** 
* Refinamentos nas animações
* Mudança na quantidade das tropas
* Mudança na cor da tropa inimiga para vermelho
* Na PreparationScreen, adicionado card com informações dos personagens ao passar mouse em cima
* Refinamentos nas telas e nos backgrounds

### Progresso 23/06
**O que foi feito:**
* Adicionado sistema de cartas de habilidades

### 4. Diagrama de Classes
<img width="2568" height="7696" alt="generaldeck3" src="https://github.com/user-attachments/assets/61d21e93-4739-45c5-b406-14e9a1bbc2e3" />

*Diagrama gerado utilizando a ferramenta: IntelliJ IDEA

### 5. Orientações para Execução
O projeto foi gerado utilizando a ferramenta `gdx-liftoff`. Para rodar o jogo na sua máquina local:
1. Certifique-se de ter o JDK (Java Development Kit) instalado.
2. Abra a pasta raiz do projeto na sua IDE (IntelliJ IDEA ou Eclipse).
3. Sincronize o projeto com o Gradle.
4. Execute a classe `DesktopLauncher.java` localizada no módulo `desktop` (ou `lwjgl3`).

### 6. Resultado Final


### 7. Referências e Créditos
* **Framework:** LibGDX (gerado via `gdx-liftoff`)
* **Sprites das Tropas e Tilesets:** Pacote "Tiny Swords" criado por Pixel Frog
* **Cenários de Combate:** Pacote "Free Elven Land 2D Battle Backgrounds" criado por Free Game Assets
