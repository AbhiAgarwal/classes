package js.util

import java.io.File
import java.io.FileNotFoundException


abstract class JsApp extends App {
  def processFile(file: File)
  def init(): Unit = ()
  
  case class Config(debug: Boolean = false, 
       files: List[File] = Nil)
  
  val usage = """JakartaScript interpreter 2.0
    Usage: run [options] [<file>...]
      
      -d  | --debug
            Print debug messages
      -h  | --help
            prints this usage text
      <file>...
            JakartaScript files to be interpreted
    """
  
  val config = ((Some(Config()): Option[Config]) /: args) {
    case (Some(c), "-d") => Some(c.copy(debug = true))
    case (Some(c), "--debug") => Some(c.copy(debug = true))
    case (Some(c), "-h") => None
    case (Some(c), "--help") => None
    case (Some(c), f) => Some(c.copy(files = c.files :+ new File(f)))
    case (None, _) => None
  } getOrElse {
    println(usage)
    System.exit(1)
    Config()
  }
     
  var debug: Boolean = config.debug
    
  var optFile: Option[File] = None 
  
  def handle[T](default: => T)(e: => T): T = 
    try e catch {
      case ex: JsException =>
        val fileName = optFile map (_.getName()) getOrElse "[eval]" 
        println(s"$fileName:$ex")
        default
      case ex: FileNotFoundException =>
        optFile match {
          case Some(f) =>
            println("Error: cannot find module '" + f.getCanonicalPath + "'")
            default
          case None =>  
            ex.printStackTrace(System.out)
            default
        }
      case ex: Throwable =>
        ex.printStackTrace(System.out)
        default
    }
  
  def fail(): Nothing = scala.sys.exit(1)
  
  init()
  for (f: File <- config.files) {
    optFile = Some(f)
    handle(fail())(processFile(f))
  }
}