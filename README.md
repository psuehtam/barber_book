# BarberBook

Aplicação web de agendamento para uma única barbearia, desenvolvida como
desafio técnico.

## Stack prevista

- Backend: Java 21 e Spring Boot
- Frontend: React, TypeScript e Vite
- Banco: PostgreSQL
- Integrações: Stripe Checkout e BrasilAPI
- Infraestrutura local: Docker Compose

## API pública

O backend está publicado no Render:

- URL base: https://barber-book-vll9.onrender.com
- Health check: [GET /health](https://barber-book-vll9.onrender.com/health)

Para verificar a disponibilidade da API:

```bash
curl -i https://barber-book-vll9.onrender.com/health
```

Exemplo de resposta:

```json
{
  "timestamp": "2026-08-03T15:41:56.778766470Z",
  "status": "UP"
}
```

## Status

Em desenvolvimento.
