package class1

object worksheet {;import org.scalaide.worksheet.runtime.library.WorksheetSupport._; def main(args: Array[String])=$execute{;$skip(78); 
  println("Welcome to the Scala worksheet");$skip(102); 

	
	def area(r: Double) = {
		val pi = 3.14159
		def square(x: Double) = x * x
		
		pi * square(r)
	};System.out.println("""area: (r: Double)Double""");$skip(10); val res$0 = 

	area(2);System.out.println("""res0: Double = """ + $show(res$0))}
	
}
