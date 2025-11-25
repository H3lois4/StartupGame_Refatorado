# Startup Game Refatorado

Projeto final da disciplina **Programação Orientada a Objetos - 2025/2**  
Este projeto é a refatoração completa do Startup Game, originalmente implementado em um único arquivo Java. O objetivo foi modularizar todo o sistema, aplicar padrões de projeto, usar Value Objects obrigatórios, implementar persistência em banco de dados H2 e estruturar o jogo com camadas bem definidas seguindo boas práticas de POO.

# Integrantes
- Heloisa — Refatoração principal (VO, Startup, Config, Persistência, GameEngine, ConsoleApp)
- Arthur — Implementação do padrão Observer e integração com eventos
- Pedro — Implementações ...

# Estrutura do projeto
resources/
  game.properties
  schema.sql
src/
    actions/
        CortarCustosStrategy.java
        DecisaoFactory.java
        DecisaoStrategy.java
        EquipeStrategy.java
        investidoresStrategy.java
        MarketingStrategy.java
        ProdutoStrategy.java
    app/
        Main.java
    config/
        Config.java
    engine/
        GameEngine.java
    model/
        vo/
            Dinheiro.java
            Percentual.java
            Humor.java
        Startup.java
    observer/
        ConsoleEventListener.java
        GameEvent.java
        GameEventListener.java
        GameEventManager.java
        GameEventType.java
    ...
    persistence/
        DataSourceProvider.java
        StartupRepository.java
    ui/
        ConsoleApp.java
.gitignore   
README.md
RELATORIO.md

# Requisitos Atendidos
➜ Requisitos obrigatórios
- Modularização completa das classes
- Uso de Value Objects (Dinheiro, Percentual, Humor)
- Padrão de projeto Strategy (decisões)
- Leitura de configuração via `game.properties`
- Persistência REAL usando banco H2 em arquivo
- Uso de exceções, encapsulamento e SRP
- Loop principal externo ao Main (arquitetura limpa)

➜ Opcionais implementados
- Observer (eventos de decisão, impacto, fechamento e ranking)
- ...

# Arquivos importantes
➜ game.properties: Configura o jogo
➜ schema.sql: Contém as tabelas do banco H2 (startups, historico e rodadas)

# Execução
➜ Via IDE (IntelliJ, VSCode, Eclipse)
1. Importe o projeto como **Java Project**  
2. Certifique-se de que o diretório `resources/` esteja no classpath  
3. Execute a classe: app.Main

➜ Via terminal
1. Compilar
    Windows:
        ```bash
        javac -cp "lib/h2.jar" -d out src/**/*.java

    Linux/Mac:
        javac -cp "lib/h2.jar" -d out src/**/*.java

2. Rodar
    Windows:
        java -cp "out;lib/h2.jar" app.Main

    Linux/Mac:
        java -cp "out:lib/h2.jar" app.Main

# Padrões de Projeto Utilizados
Strategy: aplicado às decisões do jogo (“Marketing”, “Equipe”, “Produto”, etc.).
Observer: notificação de eventos (decisões, impactos, ranking).
...
Encapsulamento + SRP + Coesão: startup é agora um modelo limpo usando VOs e comportamento bem distribuído.

# Conclusão
O projeto foi completamente reestruturado, saindo de código monolítico para uma arquitetura limpa, escalável e seguindo princípios fortes de Programação Orientada a Objetos.
Inclui:
- VOs corretamente aplicados
- Patterns
- Camadas
- Persistência
- Configurações externas
- Banco de dados
- Eventos (Observer e ...)