# Trabalho de Sistemas Distribuídos – Serviço de Mensagens Distribuídas com Consistência Eventual e Autenticação Básica 
- **Instituição:** IFBA - Instituto Federal da Bahia
- **Curso:** Análise e Desenvolvimento de Sistemas (ADS)
- **Disciplina:** Sistemas Distribuídos
- **Professor:** Felipe de Souza Silva
- **Semestre:** 5
- **Ano:** 2025.1

---
## 📌 Projeto: Sistema Distribuído de Controle Colaborativo

### Objetivo:
Desenvolver um sistema distribuído simples de publicação e leitura de mensagens entre múltiplos nós, explorando consistência eventual em cenários de falha de comunicação e controle básico de autenticação. 
---
## Integrantes do Projeto

<table>
  <tr>
    <td align="center">
      <img src="https://avatars.githubusercontent.com/u/129338943?v=4" width="100px;" alt="Foto da Integrante Ronaldo"/><br />
      <sub><b><a href="https://github.com/Ronaldo-Correia">Ronaldo Correia</a></b></sub>
    </td>
    <td align="center">
      <img src="https://avatars.githubusercontent.com/u/114780494?v=4" width="100px;" alt="Foto da Integrante Marcelo"/><br />
      <sub><b><a href="https://github.com/marceloteclas">Marcelo Jesus</a></b></sub>
    </td>
  </tr>
</table>

---

## 📁Estrutura do Projeto
```
src/
└── br/
    └── ifba/
        └── saj/
            └── nac/
                └── wall/
                    ├── Main.java                        # Classe principal (inicialização do nó)
                    ├── core/
                    │   ├── NodeState.java              # Estado local do nó (mensagens, relógio, peers)
                    │   ├── MessageService.java         # Lógica de postagem e replicação
                    │   └── ReplicationManager.java     # Gerencia reconciliações
                    ├── model/
                    │   └── Message.java                # Classe que representa uma mensagem
                    ├── net/
                    │   ├── ReplicationClient.java      # Cliente TCP para replicar mensagens
                    │   └── ReplicationServer.java      # Servidor TCP que recebe mensagens de outros nós
                    ├── replication/
                    │   └── AntiEntropyTask.java        # Tarefa periódica de reconciliação (consistência eventual)
                    └── simulation/
                        └── FailureSimulator.java       # Simulador de falha/recuperação

```

---
## 🚀 Requisitos

- Java 21
- Maven 3.8+

---

## 👨‍💻Como Executar
1. 📥Clone este repositório:
```bash
git clone https://github.com/Ronaldo-Correia/ATV8-Servico-de-Mensagens-Distribuidas.git
```
2.  📁Navegue até o local que foi clonado o repositório:
```
cd ATV8-Servico-de-Mensagens-Distribuidas
```
3. ⚙️ Compile o projeto (caso necessário):

- Se estiver usando Maven, rode:
```
mvn compile
```
Ou, se quiser compilar manualmente:
```
javac -d out src/br/ifba/saj/nac/wall/**/*.java
```
4.🚦 Execute os nós(3 terminais):

Você precisará de 3 terminais abertos. Em cada um, execute um nó com um nodeId, uma porta e a lista de peers (outros nós).

Você precisa abrir 3 terminais separados, um para cada nó.
Cada nó deve ser iniciado com:
```
java -cp target/classes br.ifba.saj.nac.wall.Main <nodeId> <porta> <peer1Host:port,peer2Host:port>
```

No Windows execute os 3 comandos em um único terminal:
```
start cmd /k "java -cp target/classes br.ifba.saj.nac.wall.Main A 9001 127.0.0.1:9002,127.0.0.1:9003"
start cmd /k "java -cp target/classes br.ifba.saj.nac.wall.Main B 9002 127.0.0.1:9001,127.0.0.1:9003"
start cmd /k "java -cp target/classes br.ifba.saj.nac.wall.Main C 9003 127.0.0.1:9001,127.0.0.1:9002"
```
5. 🧪 Testando:

No terminal de qualquer nó, você pode digitar os seguintes comandos:

- **show** – exibe as mensagens armazenadas localmente.

- **post** – permite postar uma nova mensagem no mural.

- **fail** – simula falha no nó (ele para de receber mensagens).

- **recover** – recupera o nó e força reconciliação com os peers.