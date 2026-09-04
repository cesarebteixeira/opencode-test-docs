# CT-001 - Login com Credenciais Válidas - Admin

**Pré-condições:**
- Usuário com perfil Admin cadastrado na plataforma
- E-mail: admin@prefrio.com
- Senha: Senha123!
- Usuário nunca bloqueado

**Passos para Execução:**
1. Acessar a página de login
2. Informar e-mail: admin@prefrio.com
3. Informar senha: Senha123!
4. Clicar em "Entrar"

**Resultado Esperado:**
- Usuário é autenticado com sucesso
- Usuário é redirecionado para /admin
- Dashboard admin é exibido

**Severidade:** Alta

---

# CT-002 - Login com Credenciais Válidas - Operador

**Pré-condições:**
- Usuário com perfil Operador cadastrado na plataforma
- E-mail: operador@prefrio.com
- Senha: Senha456!
- Usuário nunca bloqueado

**Passos para Execução:**
1. Acessar a página de login
2. Informar e-mail: operador@prefrio.com
3. Informar senha: Senha456!
4. Clicar em "Entrar"

**Resultado Esperado:**
- Usuário é autenticado com sucesso
- Usuário é redirecionado para /dashboard
- Dashboard de operador é exibido

**Severidade:** Alta

---

# CT-003 - Login com E-mail Incorreto

**Pré-condições:**
- Página de login acessível
- Usuário com 0 tentativas incorretas

**Passos para Execução:**
1. Acessar a página de login
2. Informar e-mail: emailerrado@prefrio.com
3. Informar senha: Senha123!
4. Clicar em "Entrar"

**Resultado Esperado:**
- Mensagem de erro exibida: "Credenciais inválidas"
- Usuário permanece na página de login
- Campo de e-mail é limpo ou permanece preenchido

**Severidade:** Alta

---

# CT-004 - Login com Senha Incorreta

**Pré-condições:**
- Página de login acessível
- Usuário admin@prefrio.com existe
- Usuário com 0 tentativas incorretas

**Passos para Execução:**
1. Acessar a página de login
2. Informar e-mail: admin@prefrio.com
3. Informar senha: SenhaErrada123!
4. Clicar em "Entrar"

**Resultado Esperado:**
- Mensagem de erro exibida: "Credenciais inválidas"
- Usuário permanece na página de login
- Tentativa incorreta é contabilizada

**Severidade:** Alta

---

# CT-005 - Login com E-mail e Senha Vazios

**Pré-condições:**
- Página de login acessível

**Passos para Execução:**
1. Acessar a página de login
2. Deixar e-mail vazio
3. Deixar senha vazia
4. Clicar em "Entrar"

**Resultado Esperado:**
- Validação é exibida
- Usuário não consegue fazer login
- Mensagem indicando campos obrigatórios (se aplicável)

**Severidade:** Alta

---

# CT-006 - Login com Múltiplas Tentativas Incorretas (3 tentativas)

**Pré-condições:**
- Página de login acessível
- Usuário operador@prefrio.com existe
- Usuário com 0 tentativas incorretas

**Passos para Execução:**
1. Acessar a página de login
2. Informar e-mail: operador@prefrio.com e senha incorreta
3. Clicar em "Entrar"
4. Repetir passos 2-3 mais duas vezes (total de 3 tentativas incorretas)

**Resultado Esperado:**
- Após a 3ª tentativa incorreta, mensagem exibida: "Conta temporariamente bloqueada"
- Usuário não consegue fazer login
- Conta é temporariamente bloqueada

**Severidade:** Alta

---

# CT-007 - Login com Conta Bloqueada

**Pré-condições:**
- Página de login acessível
- Usuário admin@prefrio.com com conta bloqueada (>3 tentativas incorretas)

**Passos para Execução:**
1. Acessar a página de login
2. Informar e-mail: admin@prefrio.com
3. Informar senha: Senha123! (correta)
4. Clicar em "Entrar"

**Resultado Esperado:**
- Mensagem exibida: "Conta temporariamente bloqueada"
- Login é rejeitado mesmo com senha correta
- Usuário é impedido de acessar

**Severidade:** Alta

---

# CT-008 - Validação de Formato de E-mail

**Pré-condições:**
- Página de login acessível

**Passos para Execução:**
1. Acessar a página de login
2. Informar e-mail: emailinvalido
3. Informar senha: Senha123!
4. Clicar em "Entrar"

**Resultado Esperado:**
- Validação de formato de e-mail é executada
- Mensagem de erro é exibida ou login é bloqueado
- Usuário permanece na página de login

**Severidade:** Média

---

# CT-009 - Login com Espaços em Branco no E-mail

**Pré-condições:**
- Página de login acessível
- Usuário admin@prefrio.com existe

**Passos para Execução:**
1. Acessar a página de login
2. Informar e-mail: "  admin@prefrio.com  " (com espaços)
3. Informar senha: Senha123!
4. Clicar em "Entrar"

**Resultado Esperado:**
- Espaços em branco são removidos automaticamente
- Login é bem-sucedido
- Usuário é redirecionado corretamente

**Severidade:** Média

---

# CT-010 - Login com Sensibilidade de Maiúsculas em E-mail

**Pré-condições:**
- Página de login acessível
- Usuário admin@prefrio.com existe

**Passos para Execução:**
1. Acessar a página de login
2. Informar e-mail: ADMIN@PREFRIO.COM (em maiúscula)
3. Informar senha: Senha123!
4. Clicar em "Entrar"

**Resultado Esperado:**
- Sistema aceita e-mail em qualquer formato de maiúscula/minúscula
- Login é bem-sucedido
- Usuário é redirecionado corretamente

**Severidade:** Média
