package inter;
import lexer.*; import symbols.*;

public class Id extends Expr {

	public int offset;     // relative address

	public Id(Word id, Type p, int b) { super(id, p); offset = b; }

//	public String toString() {return "" + op.toString() + offset;}
}
