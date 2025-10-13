  public static void main(String args[]) throws IOException {
    System.out.println("\n\nVerificador semantico simples\n");

    Parser yyparser;
    if (args.length > 0)
    {
      // parse a file
      yyparser = new Parser(new FileReader(args[0]));
    }
    else
    {
      // interactive mode
      System.out.println("[Quit with CTRL-D]");
      System.out.print("Programa de entrada:\n");
      yyparser = new Parser(new InputStreamReader(System.in));
    }

    yyparser.yyparse();
    yyparser.listarTS();
    System.out.print("\n\nFeito!\n");
  }