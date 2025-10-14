public class TS_entry
{
   private String id;
   private ClasseID classe;
   private TS_entry tipo;

   private int nroElementos;
   private TS_entry tipoBase;

   // tabela para campos da struct
   private TabSimb campos;  // null se não for struct

   // construtor para arrays
   public TS_entry(String umId, TS_entry umTipo, ClasseID umaClasse)
   {
      this(umId, umTipo, umaClasse, 0, null);
   }

   public TS_entry(String umId, TS_entry umTipo, ClasseID umaClasse, int elems, TS_entry tp)
   {
      id = umId;
      tipo = umTipo;
      classe = umaClasse;
      nroElementos = elems;
      tipoBase = tp;

      // Se for uma struct, cria tabela para seus campos
      if (umaClasse == ClasseID.NomeStruct)
         campos = new TabSimb();
   }

   public String getId()
   {
      return id;
   }

   public TS_entry getTipo()
   {
      return tipo;
   }

   public TS_entry getTipoBase()
   {
      return tipoBase;
   }

   public String toString()
   {
      StringBuilder aux = new StringBuilder("");

      aux.append("Id: ");
      aux.append(String.format("%-10s", id));

      aux.append("\tClasse: ");
      aux.append(classe);
      aux.append("\tTipo: ");
      aux.append(tipo2str(this.tipo));

      return aux.toString();
   }

   public String getTipoStr()
   {
      return tipo2str(this);
   }

   public TabSimb getCampos()
   {
      return campos;
   }

   public String tipo2str(TS_entry tipo)
   {
      if (tipo == null)  return "null";
      else if (tipo==Parser.Tp_INT)    return "int";
      else if (tipo==Parser.Tp_BOOL)   return "boolean";
      else if (tipo==Parser.Tp_DOUBLE) return "double";
      else if (tipo.getTipo() != null) return String.format("array(%d,%s)", tipo.nroElementos, tipo2str(tipo.tipoBase));
      else if (tipo==Parser.Tp_ERRO)   return  "_erro_";
      else                             return "erro/tp";
   }
}






