package js.util

sealed class State[S, +R](run: S => (S,R)) {
  def apply(s: S): (S, R) = run(s)

  def map[P](f: R => P): State[S,P] = 
    State((s: S) => {
      val (sp, r) = run(s)
      (sp, f(r))
    })
    
  def flatMap[P](f: R => State[S,P]): State[S,P] = 
    State((s: S) => {
      val (sp, r) = run(s)
      f(r)(sp)
    })
}
  
object State {
  def apply[S, R](f: S => (S, R)): State[S, R] = 
    new State(f)
  
  def init[S]: State[S, S] = 
    State(s => (s, s))
  
  def insert[S, R](r: R): State[S, R] = 
    // State(s => (s, r))
    init map (_ => r)
  
  def read[S, R](f: S => R): State[S, R] =
    // State(s => (s, f(s)))
    init[S] map (s => f(s))
    
  def write[S](f: S => S): State[S, Unit] = 
    //State(s => (f(s), ()))
    init[S] flatMap (_ => State(s => (f(s), ())))
}