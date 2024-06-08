``` console
docker compose up
```

Arquivo init-db.sql se encontra em /resources
``` console
docker exec -i postgres_container psql -U postgres -d app < init-db.sql
```