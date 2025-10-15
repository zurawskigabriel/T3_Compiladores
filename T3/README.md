# T3 Compiladores - Análise Semântica

## Grupo t3-06:
- Felipe Conzatti Frison
- Lucas Schwartz dos Santos
- Gabriel Zurawski de Souza

## Enunciado da Tarefa

**Tarefa 3 - Análise semântica**
- Atividade individual ou em grupos de até 4 alunos
- Prazo: até o dia da prova P2, até as 23:59h
- Definição dos grupos e sala de entrega no card TDE

### Objetivo
Alterar o código exemplo visto em aula (versão com arrays) para realizar **verificação semântica de structs**.

### Requisitos
1. **Reconhecer a declaração de structs** e gerar uma tabela de símbolos. Exemplo de saída em T3/corretoStructSaida.png
2. **Verificar se o programa está semanticamente correto** ou identificar erros semânticos. Exemplo de saída T3/erradoStructSaida.png

## 🔧 Como Compilar

### Pré-requisitos
- Java instalado
- JFlex.jar (incluído)
- yacc.exe ou yacc.linux (incluídos)

Para compilar:
```bash
make
```

### Possíveis problemas:
Se encontrar erro com `byaccj`, edite o Makefile:
```makefile
BYACCJ = ./yacc.linux -tv -J  # Para Linux/WSL
# ou
BYACCJ = ./yacc.exe -tv -J    # Para Windows
```

## 🚀 Como Executar

Dentro de qualquer um dos diretórios:

### Compilar e executar:
```bash
make run
```

### Apenas compilar:
```bash
make build
```

### Limpar arquivos gerados:
```bash
make clean
```

### Testar com arquivos de exemplo:
```bash
java Parser < CasosDeTeste/corretoStruct.txt
java Parser < CasosDeTeste/erroStruct.txt
```

## Observações

Fora o que havia sido pedido no enunciado, foi também necessário alterar o flex e o yacc, pois o tipo float e o tipo string (que estavam no [caso de teste](CasosDeTeste/corretoStruct.txt) fornecido) não eram suportados no código base. O flex foi alterado e agora ele está mapeando o tipo float para double. Já no yacc foi adicionado o novo tipo base estático string.