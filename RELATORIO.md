========== Visão Geral do Projeto ==========

   O StartupGame_Refatorado é uma aplicação Java 
 de console que simula decisões de gestão em uma startup.

   O sistema utiliza diversos padrões de projeto e uma arquitetura organizada em camadas, 
 incluindo Observer, Strategy, Factory e separação em ui, engine, model, persistence, dentre outros.
 
   O objetivo é oferecer ao usuário uma experiência tomada de decisões 
 que afetam métricas dentro do jogo como: dinheiro, humor da equipe e percentual de progresso.

   java -cp out:resources app.Main (roda)


        find src -name "*.java" \! -path "src/test/*" > /tmp/sources.txt
        mkdir -p out
        javac -d out @/tmp/sources.txt           (Compila)

========== Descrição das Camadas ==========

--> actions/
        Contém todas as estratégias usadas pelas decisões do jogador.
        Padrões usados: Strategy Pattern
        Exemplos:
        MarketingStrategy
        InvestidoresStrategy
        ProdutoStrategy
        Essas classes permitem alterar comportamentos sem modificar a engine.

--> app/
        Contém o Main.java, responsável por iniciar o sistema.
        Funções principais:
        Criar o GameEventManager
        Registrar os listeners
        Iniciar a UI (ConsoleApp)

--> config/
        Contém a classe Config.java, usada para carregar propriedades do jogo.

--> engine/
        Responsável pela lógica principal do jogo.
        Processa decisões
        Atualiza o estado da startup
        Notifica eventos
        Classes:
        GameEngine
        GameEngine.TipoDecisao

--> model/

        Contém as classes que representam o estado da startup:
        Startup.java
        Value Objects (VO):
        Dinheiro
        Humor
        Percentual

--> observer/
        Implementação completa do padrão Observer, usada para exibir eventos no console.
        Componentes:
        GameEvent
        GameEventType
        GameEventManager
        GameEventListener
        ConsoleEventListener

--> persistence/
        Camada responsável pelo acesso a dados.
        StartupRepository
        DataSourceProvider
        Pode integrar com arquivos ou banco de dados.

--> ui/
        Interface com o usuário via console.
        ConsoleApp.java
        Mostra menus, lê entrada, envia decisões para a  engine.

--> test/

        Contém testes unitários usando JUnit, garantindo que as estratégias funcionem corretamente.

        Exemplos de testes:
        CortarCustosStrategyTest.java
        EquipeStrategyTest.java
        InvestidoresStrategyTest.java
        MarketingStrategyTest.java
        ProdutoStrategyTest.java

========== Fluxo de Execução do Sistema =========

    1. O usuário inicia o programa (Main).
    2. O GameEventManager é criado.
    3. A UI (ConsoleApp) é inicializada.
    4. O usuário escolhe ações (ex: Marketing, Produto, Equipe).
    5. A GameEngine processa a decisão usando a estratégia adequada.
    6. A engine atualiza o estado da startup.
    7. Eventos são disparados via Observer.
    8. O console exibe as mudanças.
    9. O jogo avança para o próximo turno.


========= Padrões de Projeto Utilizados =========
    -Observer Pattern
    -Eventos do jogo notificam automaticamente os ouvintes.
    -Strategy Pattern
    -Comportamento das decisões é intercambiável.
    -Factory Pattern
    -DecisaoFactory cria estratégias conforme o tipo de decisão.
    -Separação de camadas
    -Facilita manutenção, escalabilidade e testes.

========= Conclusão =========
  O projeto é bem estruturado, com separação clara de responsabilidades entre as pastas, 
padrões de projeto bem aplicados e uma arquitetura extensível.
  A organização facilita manutenção, entendimento e futuras expansões como IA de decisões.
