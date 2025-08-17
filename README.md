# Conversor de Moedas 💰

Um programa em Java para converter moedas em tempo real, utilizando a API [ExchangeRate-API](https://www.exchangerate-api.com/). Nesse projeto desenvolvido para o Challenge da **Oracle Next Education** em parceria com a **Alura**, é possível selecionar moedas, realizar conversões e manter um histórico das operações.

---

## 📋 Funcionalidades

- Conversão entre diversas moedas: USD, BRL, ARS, BOB, CLP, COP, EUR, GBP, JPY, CAD, MXN, CNY.
- Histórico de conversões armazenado em arquivo (`historico_conversoes.txt`).
- Menu interativo no console:
  - `H` → Exibir histórico
  - `S` → Sair do programa
  - `C` → Converter moedas
- Busca de taxas de câmbio em tempo real via API.

---

## 💻 Tecnologias e bibliotecas

- **Java 17+**
- **Gson** 

---

## ⚙️ Como usar

1. Clone o repositório:

```bash
git clone https://github.com/SEU_USUARIO/conversor-challenge.git
cd conversor-challenge
```

2. Acesse o site [ExchangeRate-API](https://www.exchangerate-api.com/) e obtenha sua chave
- Basta escrever seu e-mail 

3. Configure sua chave da API da ExchangeRate-API como variável de ambiente:

### Windows (PowerShell)
```bash
setx CONVERSOR_API_KEY "SUA_CHAVE_AQUI"
```

### Linux/macOS
```bash
export CONVERSOR_API_KEY="SUA_CHAVE_AQUI"
```

4. Execute o projeto
- Navegue até a pasta do projeto e comece a execução
