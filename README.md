# Simulador de Processos e Escalonamento

Simulador de gerenciamento e escalonamento de processos desenvolvido em Java, executado pelo terminal. O projeto permite cadastrar processos, gerenciar suas informações, simular diferentes políticas de escalonamento e comparar os resultados por meio de métricas de desempenho.

O objetivo é compreender, na prática, como os algoritmos de escalonamento influenciam a execução dos processos e o desempenho de um sistema operacional.

## 📋 Sobre o projeto

Este projeto foi desenvolvido como parte de um trabalho acadêmico da disciplina de Sistemas Operacionais.

O simulador parte de um modelo de execução lógica de processos, sem utilizar threads reais ou executar processos do sistema operacional. A passagem do tempo, a utilização da CPU e as mudanças de estado são representadas por uma simulação textual no terminal.

### Objetivos

- Implementar o gerenciamento básico de processos.
- Representar os estados dos processos durante a simulação.
- Implementar diferentes algoritmos de escalonamento.
- Visualizar a linha do tempo de execução da CPU.
- Calcular métricas de desempenho.
- Comparar os resultados obtidos pelos algoritmos em diferentes cenários.

## ⚙️ Funcionalidades

- **Cadastro de processos:** criação de processos com ID, tempo de chegada, tempo de execução, prioridade e parâmetros opcionais de entrada e saída (E/S).
- **Listagem de processos:** visualização das informações dos processos cadastrados.
- **Remoção de processos:** encerramento e remoção de um processo pelo seu ID.
- **Simulação de escalonamento:** execução dos processos utilizando um algoritmo selecionado pelo usuário.
- **Configuração do Round Robin:** definição do quantum de execução.
- **Linha do tempo:** exibição textual dos eventos ocorridos durante a simulação.
- **Simulação de E/S:** configuração de eventos de bloqueio e duração da espera.
- **Comparação de algoritmos:** execução das políticas disponíveis sobre um mesmo conjunto de processos.
- **Cenários de teste:** carregamento de conjuntos de processos para facilitar os experimentos.
- **Métricas de desempenho:** apresentação de indicadores para análise comparativa.
- **Validação de entradas:** tratamento de valores inválidos informados pelo usuário.

## 🧠 Conceitos de Sistemas Operacionais

### Estados dos processos

O modelo utiliza os seguintes estados:

| Estado | Descrição |
|---|---|
| `PRONTO` | Processo apto a executar, aguardando disponibilidade da CPU. |
| `EM_EXECUÇÃO` | Processo que está utilizando a CPU no instante simulado. |
| `BLOQUEADO` | Processo aguardando a conclusão de uma operação de E/S. |

Um processo pode passar de pronto para execução, de execução para bloqueado durante uma operação de E/S ou retornar à fila de prontos após o término do bloqueio. Quando conclui todo o seu tempo de CPU, deixa de precisar de execução.

### Atributos dos processos

Cada processo deve possuir, no mínimo:

| Atributo | Descrição |
|---|---|
| ID | Identificador único do processo. |
| Tempo de chegada | Instante em que o processo entra no sistema. |
| Tempo total de execução | Quantidade total de CPU necessária para concluir o processo. |
| Tempo restante | Quantidade de CPU que ainda precisa ser executada. |
| Estado | Situação atual do processo na simulação. |
| Prioridade | Valor utilizado por algoritmos baseados em prioridade. |
| Momento de E/S | Instante de execução em que o processo solicita uma operação de E/S, quando configurada. |
| Duração da E/S | Tempo durante o qual o processo permanece bloqueado. |

## 🔄 Algoritmos de escalonamento

O simulador foi estruturado para disponibilizar diferentes políticas de escalonamento por meio de um menu textual.

O trabalho exige a implementação de pelo menos três algoritmos, incluindo FCFS e Round Robin. O terceiro algoritmo e os detalhes de suas regras devem ser documentados conforme a implementação efetivamente presente no código.

### FCFS — First-Come, First-Served

Executa os processos conforme a ordem de chegada à fila de prontos. Em sua versão tradicional, é um algoritmo não preemptivo: um processo mantém a CPU até concluir sua execução ou realizar uma operação que o retire da CPU.

### Round Robin (RR)

Distribui a CPU entre os processos prontos utilizando um intervalo máximo de execução chamado *quantum*.

Quando o quantum termina e o processo ainda precisa de CPU, ele pode retornar ao final da fila de prontos, permitindo que outros processos sejam atendidos.

O usuário informa o quantum antes de iniciar a simulação.

### Terceiro algoritmo

O terceiro algoritmo deve ser descrito aqui após a confirmação de sua implementação. Exemplos possíveis são Shortest Job First (SJF) e escalonamento por prioridade.

A descrição definitiva deve explicar o critério de seleção, se existe preempção e como os empates são tratados.

## 📊 Métricas de desempenho

O simulador apresenta métricas destinadas à análise do comportamento dos algoritmos.

### Turnaround

Representa o tempo total entre a chegada de um processo e sua conclusão.

**Fórmula:**

`Turnaround = Tempo de conclusão - Tempo de chegada`

Quanto menor o turnaround médio, menor é o tempo total que os processos passam no sistema, em média.

### Tempo de resposta

Representa o tempo entre a chegada do processo e sua primeira execução na CPU.

**Fórmula:**

`Tempo de resposta = Instante da primeira execução - Tempo de chegada`

Essa métrica ajuda a avaliar a rapidez com que os processos começam a receber atendimento.

### Tempo de espera

Representa o tempo acumulado em que o processo permanece aguardando na fila de prontos.

O cálculo deve considerar os intervalos de espera e excluir os períodos de execução e de bloqueio por E/S.

### Quantidade de processos concluídos

Indica quantos processos finalizaram sua execução durante a simulação.

### Tempo total da simulação

Representa a duração total da simulação, considerando os instantes de chegada, execução, bloqueio e conclusão, conforme o modelo adotado.

### Utilização da CPU

Indica o percentual do tempo total da simulação em que a CPU esteve ocupada.

`Utilização da CPU = (Tempo de CPU ocupada / Tempo total da simulação) × 100`

### Throughput

Representa a quantidade de processos concluídos por unidade de tempo.

`Throughput = Quantidade de processos concluídos / Tempo total da simulação`

### Trocas de contexto

Representa a quantidade de mudanças entre processos durante a execução. A contagem depende da definição adotada pelo simulador e deve ser consistente entre todos os algoritmos.

## 🛠️ Tecnologias utilizadas

- **Java:** linguagem de programação.
- **Java Collections Framework:** gerenciamento da lista dinâmica de processos.
- **Scanner:** leitura das entradas do usuário pelo terminal.
- **Git:** controle de versão.
- **GitHub:** hospedagem do código-fonte.

O projeto utiliza uma interface textual, sem necessidade de interface gráfica.

## 📁 Estrutura do projeto

A organização esperada dos arquivos é:

```text
simulador-processos/
├── src/
│   ├── Main.java
│   ├── Processo.java
│   ├── Simulador.java
│   ├── Algoritmo.java
│   ├── Resultado.java
│   └── Cenarios.java
├── README.md
└── .gitignore
```

A estrutura acima é uma referência. Ajuste os caminhos conforme a organização real do projeto.

### Responsabilidade das classes

| Classe | Responsabilidade |
|---|---|
| `Main` | Exibe o menu, recebe as entradas e coordena as operações. |
| `Processo` | Representa um processo e seus atributos. |
| `Simulador` | Executa a simulação e controla a evolução temporal dos processos. |
| `Algoritmo` | Identifica os algoritmos de escalonamento disponíveis. |
| `Resultado` | Armazena e apresenta as métricas da execução. |
| `Cenarios` | Fornece conjuntos predefinidos de processos para testes. |

## 💻 Requisitos

Para executar o projeto, você precisará de:

- JDK instalado.
- Terminal ou console.
- Editor de código ou IDE Java, como IntelliJ IDEA, Eclipse ou Visual Studio Code.

A versão mínima do Java deve ser compatível com a versão utilizada no desenvolvimento do projeto.

Para verificar a instalação, execute:

```bash
java -version
javac -version
```

## ▶️ Como executar

### 1. Clone o repositório

```bash
git clone URL_DO_SEU_REPOSITORIO
```

Substitua `URL_DO_SEU_REPOSITORIO` pelo endereço real do repositório.

### 2. Acesse a pasta do projeto

```bash
cd simulador-processos
```

### 3. Compile os arquivos

Se os arquivos `.java` estiverem diretamente na pasta `src/`, sem declaração de pacotes, execute:

```bash
javac -d out src/*.java
```

### 4. Execute o programa

```bash
java -cp out Main
```

Se você estiver utilizando uma IDE, também poderá executar diretamente a classe `Main`.

**Observação:** os comandos de compilação acima pressupõem que todos os arquivos estejam em `src/` e não utilizem declarações de pacote. Se a estrutura do projeto for diferente, os comandos deverão ser ajustados.

## 📖 Como utilizar

Ao iniciar o programa, o usuário encontrará um menu semelhante ao seguinte:

```text
========== SIMULADOR DE ESCALONAMENTO ==========

1 - Criar processo
2 - Listar processos
3 - Remover/encerrar processo
4 - Executar processos (escolher algoritmo)
5 - Comparar todos os algoritmos
6 - Carregar cenario de teste
7 - Limpar todos os processos
0 - Sair

Escolha uma opcao:
```

### Criar processo

Selecione a opção `1` e informe os dados solicitados, incluindo os tempos de chegada e de execução, a prioridade e, opcionalmente, os parâmetros de E/S.

### Listar processos

Selecione a opção `2` para consultar os processos cadastrados, seus tempos, suas prioridades e seus estados.

### Remover processo

Selecione a opção `3` e informe o ID do processo que deseja remover.

### Executar um algoritmo

Selecione a opção `4`, escolha o algoritmo desejado e, se for Round Robin, informe o quantum.

O simulador deverá apresentar os eventos da execução e os resultados calculados.

### Comparar algoritmos

Selecione a opção `5` e informe o quantum utilizado pelo Round Robin. O programa executará os algoritmos disponíveis e apresentará uma tabela comparativa das métricas.

### Carregar cenário de teste

Selecione a opção `6` para escolher um conjunto predefinido de processos.

**Atenção:** carregar um cenário substitui os processos atualmente cadastrados.

### Limpar os processos

Selecione a opção `7` para remover todos os processos da lista e reiniciar a numeração dos IDs.

## 🧪 Testes e análise comparativa

Para avaliar o comportamento dos algoritmos, devem ser utilizados pelo menos três cenários distintos.

### Cenário 1 — Processos com execuções semelhantes

Objetivo: observar como os algoritmos distribuem a CPU quando os processos possuem tempos de execução próximos.

### Cenário 2 — Processos com execuções diferentes

Objetivo: avaliar o impacto de processos curtos e longos nas métricas de turnaround, resposta e espera.

### Cenário 3 — Processos com chegadas diferentes

Objetivo: observar como os algoritmos lidam com processos que entram no sistema em instantes distintos.

### Análise dos resultados

Para cada cenário:

1. Utilize o mesmo conjunto de processos em todos os algoritmos.
2. Registre as métricas obtidas.
3. Compare o turnaround, a resposta e a espera médias.
4. Observe a utilização da CPU, o throughput e as trocas de contexto.
5. Explique quais características dos algoritmos justificam os resultados.

Os resultados devem ser obtidos por meio da execução efetiva do programa. Não se deve presumir que um algoritmo será sempre superior aos demais: o desempenho depende dos processos e dos critérios utilizados.

## 📚 Conceitos estudados

- Gerenciamento de processos.
- Ciclo de vida e estados dos processos.
- Filas de prontos e bloqueados.
- Escalonamento preemptivo e não preemptivo.
- FCFS e Round Robin.
- Quantum de execução.
- Operações de entrada e saída.
- Trocas de contexto.
- Métricas de desempenho.
- Estruturas de dados em Java.
- Programação orientada a objetos.

## 🚀 Possíveis melhorias futuras

- Adicionar novos algoritmos de escalonamento.
- Permitir exportação dos resultados para CSV.
- Criar gráficos comparativos das métricas.
- Implementar histórico das simulações.
- Permitir configurar cenários personalizados.
- Adicionar testes automatizados para validar os algoritmos e as fórmulas das métricas.

## 👨‍💻 Autor

Projeto acadêmico desenvolvido para estudo de Sistemas Operacionais e algoritmos de escalonamento de processos.

**Autor:** Pedro Henrique Sales Rocha Filgueiras

**Instituição:** Unifor

**Disciplina:** Sistemas Operacionais

---

## 📄 Licença

Projeto desenvolvido para fins educacionais. Caso deseje disponibilizá-lo para reutilização por outras pessoas, adicione uma licença apropriada ao repositório.

