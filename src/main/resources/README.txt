----------------------------------  INFO  --------------------------------------
This is a simple interpreter that lets you create a binary block code and test 
its properties.

Every input to the interpreter except "help" and "exit" has to be in following
format:

>variable.method arg1 arg2 arg3 ...

The "variable" part is name of a block code on which you'd like to apply method
"method". Method arguments are separated by one space in the same line.


SUPPORTED METHODS:

>variable.new  n - instantiation of new block code, accepts exactly one, first
            argument that has to be a natural number [1, 1000000]:
            length of codewords in the block code / block code size "n"

>variable.n - prints the size of the block code / length of its every codeword

>variable.k - prints the number of message bits in one codeword

>variable.sa - prints standard array of the block code (synonym: standardArray

>variable.decode codeword - decodes given codeword and prints decision-making 
            summary for the decoding, accepts exactly one, first argument that
            has to be binary string (only 0s and 1s)

>variable.add cw1[:sym1] cw2[:sym2] - adds arbitrary number of codeword-symbol
            pairs. Arguments are binary strings to which may be appended symbol
            string separated by colon.

>variable.remove cw1 cw2 - removes arbitrary number of codewords represented as
            binary string

>variable.linear - prints "Yes" if the block code is linear, "No" otherwise

>variable.p pg - prints probability of correct decoding of a codeword with
            probability pg (one argument, real number in interval [0, 1])

>variable.t - prints the number of errors in code that can be corrected

>variable.dist - prints minimal distance between words of the code (synonyms:
            distance and d)

>variable.print - prints all codewords in the code

>variable.safety - determines will the standard array include the error vectors
            with weight larger than number of decodable errors (toggle)

>variable.delete - deletes the block code
--------------------------------------------------------------------------------
