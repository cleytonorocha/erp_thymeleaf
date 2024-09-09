package tech.leonam.erp.query;

public class ClientQuery {
    public static final String SELECT_BY_ID = "select * from clientes where id = :id";
    public static final String INSERT_INTO = "INSERT INTO clientes (is_cpf, nome, cpf_or_cnpj, numero_contato, cep, endereco, bairro, cidade, uf, numero_casa)"
            + " VALUES (:isCPF, :nome, :cpfOrCnpj, :numeroContato, :cep, :endereco, :bairro, :cidade, :uf, :numeroCasa)";
    public static final String DELETE_BY_ID = "delete from clientes where id = :id";
    public static final String EXISTS_ID = "select 1 from clientes where cpf_or_cnpj = :cpf_or_cnpj limit 1";

}
