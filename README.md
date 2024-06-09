``` console
docker compose up
```

Arquivo init-db.sql se encontra em /resources
``` console
docker exec -i postgres_container psql -U postgres -d app < init-db.sql
```

Code Smells

- if aninhados
  - early return para resolver
- espaços em branco

Separação de responsabilidade entre Api, Application, Resource.
Isso fez a possibilidade de fazer testes apenas da camada de aplicação