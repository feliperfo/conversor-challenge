# Conversor de Moedas 💰

Um programa em Java para converter moedas em tempo real, utilizando a API [ExchangeRate-API](https://www.exchangerate-api.com/). Nesse projeto, desenvolvido para o **Challenge Oracle Next Education** em parceria com a **Alura**, é possível selecionar moedas, realizar conversões e manter um histórico das operações.

---

## 📋 Funcionalidades

* Conversão entre diversas moedas: USD, BRL, ARS, BOB, CLP, COP, EUR, GBP, JPY, CAD, MXN, CNY.
* Histórico de conversões armazenado em arquivo (`historico_conversoes.txt`).
* Menu interativo no console:
    * `H` → Exibir histórico
    * `S` → Sair do programa
    * `C` → Converter moedas
* Busca de taxas de câmbio em tempo real via API.

---

## 💻 Tecnologias e Dependências

* **Java 17+**
* **Gson**

---

## ⚙️ Como usar

1.  Clone o repositório:

    ```bash
    git clone [https://github.com/SEU_USUARIO/conversor-challenge.git](https://github.com/SEU_USUARIO/conversor-challenge.git)
    cd conversor-challenge
    ```

2.  Acesse o site [ExchangeRate-API](https://www.exchangerate-api.com/), crie uma conta e obtenha sua chave de API.

3.  Configure a chave da API como uma variável de ambiente:

    #### Windows (PowerShell)
    ```bash
    setx CONVERSOR_API_KEY "SUA_CHAVE_AQUI"
    ```

    #### Linux/macOS
    ```bash
    export CONVERSOR_API_KEY="SUA_CHAVE_AQUI"
    ```

4. Execute o projeto:

   - **Usando uma IDE:**  
     Abra o projeto no IntelliJ ou VS Code e execute a classe principal diretamente.

   - **Via terminal:**  
     Ou navegue até a pasta do projeto no terminal.

     #### Linux/macOS
        Para compilar
     ```bash
     javac -d out -cp "libs/gson-2.10.1.jar" src/*.java
     ```

        Para executar
     ```bash
     java -cp "out:libs/gson-2.10.1.jar" conversor-challenge.ConversorDeMoedas
     ```

     #### Windows:
        Para compilar
     ```cmd
     javac -d out -cp "libs\gson-2.10.1.jar" src\*.java
     ```
        Para executar
     ```cmd
     java -cp "out;libs\gson-2.10.1.jar" conversor-challenge.ConversorDeMoedas
     ```
