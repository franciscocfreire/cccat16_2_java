``` console
docker compose up
```

Arquivo init-db.sql se encontra em /resources
``` console
docker exec -i postgres_container psql -U postgres -d app < init-db.sql
```

Conectar no banco de dados docker-compose.yml

````agsl
psql -h localhost -p 5432 -U postgres -d app
````
Ver as contas cdastradas
```agsl
SELECT * FROM cccat16.account;
```

Code Smells

- if aninhados
  - early return para resolver
- espaços em branco

Separação de responsabilidade entre Api, Application.

Isso fez a possibilidade de fazer testes apenas da camada de aplicação

Separação de responsabilidade do Resource(Driven Actor)

Possibilidade de usar uma implementação em memoria (AccountDAOMemory) para os testes

