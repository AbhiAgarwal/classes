/* Call-By-Name:
 *
 * With call-by-name, we can write a function that looks very
 * close to a custom statement.  For example, we define a
 *  'while' loop below.
 */

const while = function while(name b: bool): (name Undefined) => Undefined  {
    return function (name body: Undefined) {
      return !b ? undefined : (body, while(b)(body))
    }
  };

var i = 0;
while (i < 10) ({
  console.log(i);
  i = i + 1;
  undefined;
});

