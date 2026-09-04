# CT-002 - API de Temperatura: Suite de Testes Completa

## CT-002-001 - Registrar temperatura com sucesso

**Pré-condições:**
- Token Bearer válido disponível
- Sensor com ID válido cadastrado no sistema
- Temperatura dentro do intervalo permitido (> -10.0°C)

**Passos para Execução:**
1. Fazer POST request para `/api/v1/temperatura`
2. Incluir header `Authorization: Bearer <token-valido>`
3. Enviar body JSON com sensor_id, temperatura e unidade válidos

**Request:**
```json
{
  "sensor_id": "SENSOR-001",
  "temperatura": 25.5,
  "unidade": "C"
}
```

**Resultado Esperado:**
- HTTP Status: 201 Created
- Response body contém confirmação de registro
- Alerta gerado no sistema

**Severidade:** Alta

---

## CT-002-002 - Registrar temperatura acima do limite com alerta

**Pré-condições:**
- Token Bearer válido disponível
- Sensor com ID válido cadastrado no sistema
- Temperatura acima de -10.0°C

**Passos para Execução:**
1. Fazer POST request para `/api/v1/temperatura`
2. Incluir header `Authorization: Bearer <token-valido>`
3. Enviar temperatura de 35.0°C (acima do limite crítico)

**Request:**
```json
{
  "sensor_id": "SENSOR-002",
  "temperatura": 35.0,
  "unidade": "C"
}
```

**Resultado Esperado:**
- HTTP Status: 201 Created
- Alerta crítico gerado no sistema
- Response contém indicação de alerta

**Severidade:** Alta

---

## CT-002-003 - Rejeitar requisição sem sensor_id

**Pré-condições:**
- Token Bearer válido disponível
- Body sem o campo sensor_id

**Passos para Execução:**
1. Fazer POST request para `/api/v1/temperatura`
2. Incluir header `Authorization: Bearer <token-valido>`
3. Enviar body JSON sem o campo sensor_id

**Request:**
```json
{
  "temperatura": 20.0,
  "unidade": "C"
}
```

**Resultado Esperado:**
- HTTP Status: 400 Bad Request
- Response contém mensagem de erro indicando campo obrigatório
- Nenhum registro criado no sistema

**Severidade:** Alta

---

## CT-002-004 - Rejeitar requisição com sensor_id inválido

**Pré-condições:**
- Token Bearer válido disponível
- sensor_id com formato inválido ou não registrado

**Passos para Execução:**
1. Fazer POST request para `/api/v1/temperatura`
2. Incluir header `Authorization: Bearer <token-valido>`
3. Enviar sensor_id com valor inválido

**Request:**
```json
{
  "sensor_id": "INVALID@@#",
  "temperatura": 22.0,
  "unidade": "C"
}
```

**Resultado Esperado:**
- HTTP Status: 400 Bad Request
- Response contém mensagem de erro sobre sensor_id inválido
- Nenhum registro criado

**Severidade:** Alta

---

## CT-002-005 - Rejeitar requisição sem token Bearer

**Pré-condições:**
- Request sem header Authorization

**Passos para Execução:**
1. Fazer POST request para `/api/v1/temperatura`
2. Não incluir header Authorization
3. Enviar body JSON válido

**Request:**
```json
{
  "sensor_id": "SENSOR-003",
  "temperatura": 18.5,
  "unidade": "C"
}
```

**Resultado Esperado:**
- HTTP Status: 401 Unauthorized
- Response contém mensagem indicando falta de token
- Nenhum registro criado

**Severidade:** Alta

---

## CT-002-006 - Rejeitar requisição com token Bearer inválido

**Pré-condições:**
- Header Authorization com token inválido ou expirado

**Passos para Execução:**
1. Fazer POST request para `/api/v1/temperatura`
2. Incluir header `Authorization: Bearer <token-invalido>`
3. Enviar body JSON válido

**Request:**
```json
{
  "sensor_id": "SENSOR-004",
  "temperatura": 21.0,
  "unidade": "C"
}
```

**Resultado Esperado:**
- HTTP Status: 401 Unauthorized
- Response contém mensagem de token inválido/expirado
- Nenhum registro criado

**Severidade:** Alta

---

## CT-002-007 - Rejeitar token Bearer com formato incorreto

**Pré-condições:**
- Header Authorization com formato incorreto

**Passos para Execução:**
1. Fazer POST request para `/api/v1/temperatura`
2. Incluir header `Authorization: <token-sem-bearer>`
3. Enviar body JSON válido

**Request:**
```json
{
  "sensor_id": "SENSOR-005",
  "temperatura": 23.0,
  "unidade": "C"
}
```

**Resultado Esperado:**
- HTTP Status: 401 Unauthorized
- Response contém mensagem de formato de token inválido
- Nenhum registro criado

**Severidade:** Alta

---

## CT-002-008 - Registrar temperatura negativa válida

**Pré-condições:**
- Token Bearer válido disponível
- Sensor com ID válido cadastrado
- Temperatura acima de -10.0°C (ex: -5.0°C)

**Passos para Execução:**
1. Fazer POST request para `/api/v1/temperatura`
2. Incluir header `Authorization: Bearer <token-valido>`
3. Enviar temperatura negativa mas acima do limite mínimo

**Request:**
```json
{
  "sensor_id": "SENSOR-006",
  "temperatura": -5.5,
  "unidade": "C"
}
```

**Resultado Esperado:**
- HTTP Status: 201 Created
- Temperatura registrada com sucesso
- Possível alerta dependendo das regras do sistema

**Severidade:** Média

---

## CT-002-009 - Rejeitar temperatura igual a -10.0°C (limite mínimo)

**Pré-condições:**
- Token Bearer válido disponível
- Sensor com ID válido cadastrado
- Temperatura exatamente em -10.0°C

**Passos para Execução:**
1. Fazer POST request para `/api/v1/temperatura`
2. Incluir header `Authorization: Bearer <token-valido>`
3. Enviar temperatura de -10.0°C

**Request:**
```json
{
  "sensor_id": "SENSOR-007",
  "temperatura": -10.0,
  "unidade": "C"
}
```

**Resultado Esperado:**
- HTTP Status: 400 Bad Request (conforme regra: aceita se > -10.0°C)
- Response contém mensagem de temperatura fora do intervalo permitido
- Nenhum registro criado

**Severidade:** Média

---

## CT-002-010 - Rejeitar temperatura abaixo de -10.0°C

**Pré-condições:**
- Token Bearer válido disponível
- Sensor com ID válido cadastrado
- Temperatura abaixo de -10.0°C (ex: -15.0°C)

**Passos para Execução:**
1. Fazer POST request para `/api/v1/temperatura`
2. Incluir header `Authorization: Bearer <token-valido>`
3. Enviar temperatura abaixo do limite mínimo

**Request:**
```json
{
  "sensor_id": "SENSOR-008",
  "temperatura": -15.0,
  "unidade": "C"
}
```

**Resultado Esperado:**
- HTTP Status: 400 Bad Request
- Response contém mensagem de temperatura fora do intervalo permitido
- Nenhum registro criado

**Severidade:** Alta

---

## CT-002-011 - Validar unidade de temperatura obrigatória

**Pré-condições:**
- Token Bearer válido disponível
- Sensor com ID válido cadastrado
- Body sem o campo unidade

**Passos para Execução:**
1. Fazer POST request para `/api/v1/temperatura`
2. Incluir header `Authorization: Bearer <token-valido>`
3. Enviar body JSON sem o campo unidade

**Request:**
```json
{
  "sensor_id": "SENSOR-009",
  "temperatura": 25.0
}
```

**Resultado Esperado:**
- HTTP Status: 400 Bad Request
- Response contém mensagem de campo obrigatório (unidade)
- Nenhum registro criado

**Severidade:** Média

---

## CT-002-012 - Validar temperatura como tipo numérico

**Pré-condições:**
- Token Bearer válido disponível
- Sensor com ID válido cadastrado
- Temperatura com tipo de dado inválido (string)

**Passos para Execução:**
1. Fazer POST request para `/api/v1/temperatura`
2. Incluir header `Authorization: Bearer <token-valido>`
3. Enviar temperatura como string ao invés de número

**Request:**
```json
{
  "sensor_id": "SENSOR-010",
  "temperatura": "25.5",
  "unidade": "C"
}
```

**Resultado Esperado:**
- HTTP Status: 400 Bad Request
- Response contém mensagem de tipo de dado inválido
- Nenhum registro criado

**Severidade:** Média
