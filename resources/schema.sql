-- TABELA PRINCIPAL
CREATE TABLE IF NOT EXISTS startups (
    id IDENTITY PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    caixa DOUBLE NOT NULL,
    receita_base DOUBLE NOT NULL,
    reputacao INT NOT NULL,
    moral INT NOT NULL,
    score DOUBLE NOT NULL,
    criado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_startup_nome ON startups (nome);

-- TABELA DE HISTÓRICO (ações)
CREATE TABLE IF NOT EXISTS historico (
    id IDENTITY PRIMARY KEY,
    startup_id BIGINT NOT NULL,
    mensagem VARCHAR(500) NOT NULL,
    criado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_hist_startup
        FOREIGN KEY (startup_id)
        REFERENCES startups(id)
        ON DELETE CASCADE
);

-- TABELA DE RODADAS
CREATE TABLE IF NOT EXISTS rodadas (
    id IDENTITY PRIMARY KEY,
    startup_id BIGINT NOT NULL,
    rodada_numero INT NOT NULL,
    caixa DOUBLE,
    receita DOUBLE,
    reputacao INT,
    moral INT,
    mensagem VARCHAR(500),
    criado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_rodada_startup
        FOREIGN KEY (startup_id)
        REFERENCES startups(id)
        ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS idx_rodadas_startup ON rodadas (startup_id, rodada_numero);
