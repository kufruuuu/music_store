---------------------------------------------------------------------------------------------------------
--                                 Esquema music_store e suas tabelas
---------------------------------------------------------------------------------------------------------

CREATE SCHEMA music_store;

CREATE TABLE music_store.cliente
(
    login VARCHAR(50),
    senha VARCHAR(50),
    nome_completo VARCHAR(50),
    documento VARCHAR(50),
    data_nascimento DATE,
    email VARCHAR(50),
    telefone VARCHAR(50),
    ativado BOOLEAN,
    CONSTRAINT pk_cliente PRIMARY KEY (login)
);

CREATE TABLE music_store.disco
(
    nome VARCHAR(50),
    artista VARCHAR(50),
    estilo VARCHAR(50),
    ano_lancamento VARCHAR(4),
    unidades INT,
    CONSTRAINT pk_disco PRIMARY KEY (nome, artista),
    CONSTRAINT uq_disco UNIQUE (nome, artista, estilo, ano_lancamento)
);

CREATE TABLE music_store.pedido
(
    nota_fiscal SERIAL,
    cliente_login VARCHAR(50),
    disco_nome VARCHAR(50),
    disco_artista VARCHAR(50),
    CONSTRAINT pk_pedido PRIMARY KEY (nota_fiscal),
    CONSTRAINT fk_pedido_cliente_login FOREIGN KEY (cliente_login) REFERENCES music_store.cliente(login),
    CONSTRAINT fk_pedido_disco_nome_artista FOREIGN KEY (disco_nome, disco_artista) REFERENCES music_store.disco(nome, artista),
    CONSTRAINT chk_pedido_disco_unidades CHECK (music_store.check_disco_unidades(disco_nome, disco_artista))
);

---------------------------------------------------------------------------------------------------------
--                          Functions/Procedures para a tabela cliente
---------------------------------------------------------------------------------------------------------

CREATE OR REPLACE PROCEDURE music_store.update_cliente(input VARCHAR(50), loginCliente VARCHAR(50), option INT)
    LANGUAGE plpgsql
AS $$
BEGIN
    CASE option
        WHEN 0 THEN
            UPDATE music_store.cliente SET senha = md5(input) WHERE login = loginCliente;
        WHEN 1 THEN
            UPDATE music_store.cliente SET nome_completo = input WHERE login = loginCliente;
        WHEN 2 THEN
            UPDATE music_store.cliente SET documento = input WHERE login = loginCliente;
        WHEN 3 THEN
            UPDATE music_store.cliente SET email = input WHERE login = loginCliente;
        WHEN 4 THEN
            UPDATE music_store.cliente SET telefone = input WHERE login = loginCliente;
    END CASE;
END; $$;

---------------------------------------------------------------------------------------------------------
--                          Functions/Procedures para a tabela disco
---------------------------------------------------------------------------------------------------------

CREATE OR REPLACE FUNCTION music_store.read_discos(input VARCHAR(50), option INT)
    RETURNS TABLE(nome VARCHAR(50), artista VARCHAR(50), estilo VARCHAR(50), ano_lancamento VARCHAR(4), unidades INT)
    LANGUAGE plpgsql
AS $$
BEGIN
    CASE option
        WHEN 0 THEN
            RETURN QUERY SELECT * FROM music_store.disco WHERE disco.nome LIKE CONCAT('%', input, '%');
        WHEN 1 THEN
            RETURN QUERY SELECT * FROM music_store.disco WHERE disco.artista LIKE CONCAT('%', input, '%');
        WHEN 2 THEN
            RETURN QUERY SELECT * FROM music_store.disco WHERE disco.estilo LIKE CONCAT('%', input, '%');
        WHEN 3 THEN
            RETURN QUERY SELECT * FROM music_store.disco WHERE disco.ano_lancamento LIKE CONCAT('%', input, '%');
    END CASE;
END $$;

CREATE OR REPLACE FUNCTION music_store.create_disco(d_nome VARCHAR(50), d_artista VARCHAR(50), d_estilo VARCHAR(50), d_ano_lancamento VARCHAR(4), d_unidades INT)
    RETURNS TABLE(result BOOLEAN)
    LANGUAGE plpgsql
AS $$
    DECLARE
        teste_unidades INT;
        teste_estilo VARCHAR(50);
        teste_ano_lancamento VARCHAR(4);
BEGIN
    SELECT unidades, estilo, ano_lancamento INTO teste_unidades, teste_estilo, teste_ano_lancamento
    FROM music_store.disco WHERE nome = d_nome AND artista = d_artista;

    IF teste_unidades IS NULL THEN
        INSERT INTO music_store.disco(nome, artista, estilo, ano_lancamento, unidades) VALUES
        (d_nome, d_artista, d_estilo, d_ano_lancamento, d_unidades);
        RETURN QUERY SELECT TRUE;
    ELSE
        IF teste_estilo = d_estilo AND teste_ano_lancamento = d_ano_lancamento THEN
            UPDATE music_store.disco SET unidades = unidades + d_unidades
            WHERE nome = d_nome AND artista = d_artista AND estilo = d_estilo AND ano_lancamento = d_ano_lancamento;
            RETURN QUERY SELECT TRUE;
        ELSE
            RETURN QUERY SELECT FALSE;
        END IF;
    END IF;
END $$;

---------------------------------------------------------------------------------------------------------
--                            Functions/Procedures para a tabela pedido
---------------------------------------------------------------------------------------------------------

CREATE OR REPLACE FUNCTION music_store.check_disco_unidades(d_nome VARCHAR(50), d_artista VARCHAR(50))
    RETURNS BOOLEAN
    LANGUAGE plpgsql
AS $$
DECLARE
    total_pedidos INT;
    total_discos INT;
BEGIN
    SELECT COUNT(DISTINCT(pedido.nota_fiscal)) INTO total_pedidos
    FROM music_store.pedido WHERE disco_nome = d_nome AND disco_artista = d_artista;

    SELECT disco.unidades INTO total_discos
    FROM music_store.disco WHERE nome = d_nome AND artista = d_artista;

    IF total_pedidos < total_discos THEN
        RETURN TRUE;
    ELSE
        RETURN FALSE;
    END IF;
END $$;

---------------------------------------------------------------------------------------------------------

SELECT * FROM music_store.create_disco('Ohne Dich', 'Rammstein', 'Industrial Metal', '2006', 5);

SELECT unidades FROM music_store.disco WHERE nome = 'Ohne sDich' AND artista = 'Rammstein';

INSERT INTO music_store.disco(nome, artista, estilo, ano_lancamento, unidades) VALUES
    ('Ohne Dich', 'Rammstein', 'Industrial Metal', '2006', 5);

SELECT * FROM music_store.disco;

INSERT INTO music_store.cliente(login, senha, nome_completo, documento, data_nascimento, email, telefone, ativado) VALUES
    ('aizao', md5('shinigami'), 'Aizen Sosuke', '233566734', to_date('22-11-1920', 'dd-MM-yyyy'), 'aizen@sereitei.com', '2354-2345', TRUE);

delete from music_store.cliente WHERE login = 'shalashask2';

select * from music_store.cliente;

select * from music_store.pedido;

SELECT * FROM music_store.pedido WHERE cliente_login = 'aizao'
