package class2

object worksheet {
  println("Welcome to the Scala worksheet")       //> Welcome to the Scala worksheet

	
	def area(r: Double) = {
		val pi = 3.14159
		def square(x: Double) = x * x
		
		pi * square(r)
	}                                         //> area: (r: Double)Double

	area(2)                                   //> res0: Double = 12.56636
	
}