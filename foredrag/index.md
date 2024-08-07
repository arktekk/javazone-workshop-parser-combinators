---
theme: gaia
_class: lead
paginate: true
backgroundColor: #fff
backgroundImage: url('https://marp.app/assets/hero-background.svg')
marp: true
---

![bg left:40% 80%](assets/logo.svg)

# **Parser Combinators**

En parser som spiser parsere

---

# Hva er en parser?

```scala
def parser[I, O](input: I): ParseResult[I, O] = ???
```

<!-- 
En parser er kode som transformerer noe input I til 
noe av output av `ParseResult[O]`.

ParseResult vil inneholde posisjonsdata for inputen, og eventuelle feil.
-->

---

# Hva er en parser?

```scala
def parser[I, O]: I => ParseResult[I, O] = ???
```

<!-- 
Litt forenklet kan man se på det som en funksjon fra I til ParseResult[I, O].
-->

---

# Hva er en parser?

```scala
def parser[O]: String => ParseResult[O] = ???
```

<!-- 
Enda mer forenklet og det vi kommer til å bruke i resten av presentasjonen og 
oppgaveløsningen er at vi tar en input av String og produserer et resultat av 
en type O.
-->


---

# Hva er en parser?

```scala
final case class Parser[O](run: String => ParseResult[O])
```

<!-- 
Det som vi kan gjøre med nå når vi har etablert hva en parser er, så kan vi pakke den inn i en datatype.
Det finnes flere mulige måter å gjøre det på.
Her har vi pakket inn en funksjon inn i en case klasse, men vi kan også lage et interface eller det som i Scala blir kalt et trait.
-->

---

# Hva er en parser?

```scala
trait Parser[O] {
  def apply(input: String): ParseResult[O]
}
```

<!-- 
Det som vi kan gjøre med nå når vi har etablert hva en parser er, så kan vi pakke den inn i en datatype.
Det finnes flere mulige måter å gjøre det på.
Her har vi pakket inn en funksjon inn i en case klasse, men vi kan også lage et interface eller det som i Scala blir kalt et trait.
-->

----
# Parser combinators

<!--
Nå når vi har en liten forståelse av hva en parser er, så kan vi da se på hva parser combinators er.

En parser combinator er en måte å sette sammen parsere av små biter som hver for seg gir mening.
Vi kan se på dette som flere funksjoner som opererer videre på resultatet av forrige funksjonskall.

-->
La oss si at vi har de følgende parserene:

* `val digit: Parser[Char] = ???`
* `val alpha: Parser[Char] = ???`
* `val alphaNum = alpha | digit`

Da kan vi se at vi setter sammen digit og alpha for å lage en ny parser som gjør begge deler.
Hver av disse kan testes for seg selv.

---
# Parser Combinators

<!--
Dersom vi feks titter inni ParseResult, så ser det noenlunde slik ut:

-->

## ParseResult

```scala
enum ParseResult[O] {
    case Success(value: O, rest: Option[String], position: Position)
    case Failure(message: String, position: Position)
}
```


---
# cats-parse

```scala
libraryDependencies += "org.typelevel" % "cats-parse" % "1.0.0"
```

`cats-parse` er substring orientert, så vi ser på biter av en streng, og henter ut informasjon fra den.
Dette betyr at vi setter sammen parsere som matcher biter av strenger til vi når EOF.

---
# Eksempel på parsere

```
val digit = Parser.charIn("1234567890")

val alpha = Parser.charIn(('a' to 'z') ++ ('A' to 'Z'))

val alphanum: Parser[String] = (alpha | digit).rep.string

val string = Parser.string("input")
```


---
### En Scala primer

```scala
//en verdi av typen Int
val value: Int = 1
val value = 1

//En mutabel variabel av typen String
var x: String = "Hello"
x = x + " World"

//metodekall
println("na" * 10 + " Batman")
// val res5: String = nananananananananana Batman

```

---

```scala

//Metode definisjon
def sayHello(who: String): Unit = println(s"Hello $who")
def sayHello(who: String) = println(s"Hello $who")

//metode definisjon med generiske typer
def generic[A](input: A): Unit = println(s"called with ${input}")
generic("World")
generic(1)
generic(1.0)
```

---
```scala
def isEven(x: Int): Boolean = x % 2 == 0
def evenString(x: Int) = if isEven(x) then "even" else "odd"


enum EvenOdd {
    case Even, Odd
}

def even(x: Int) = if isEven(x) then EvenOdd.Even else EvenOdd.Odd

```

---
## case class

```scala
case class Dog(name: String)

val dog = Dog("Fido")

val newDogName = dog.copy(name = "xxxx")

println(dog)
println(newDogName)


```


---
## Algebraiske data typer


```scala
enum EvenOdd {    
    case Even(num: Int)
    case Odd(num: Int)
}


def even(x: Int): EvenOdd = if isEven(x) then EvenOdd.Even(x) else EvenOdd.Odd(x)


```


---

# Et raskt sidesprang

Backus–Naur form (BNF)


---

# Backus–Naur form (BNF)


> In computer science, Backus–Naur form (/ˌbækəs ˈnaʊər/) (BNF or Backus normal form) is a notation used to describe the syntax of programming languages or other formal languages. It was developed by John Backus and Peter Naur. BNF can be described as a metasyntax notation for context-free grammars. [...] BNF can be used to describe document formats, instruction sets, and communication protocols.

[wikipedia](https://en.wikipedia.org/wiki/Backus%E2%80%93Naur_form)

---

# Backus–Naur form (BNF) Variants

> In computer science, extended Backus–Naur form (EBNF) is a family of metasyntax notations, any of which can be used to express a context-free grammar. EBNF is used to make a formal description of a formal language such as a computer programming language.
[wikipedia](https://en.wikipedia.org/wiki/Extended_Backus%E2%80%93Naur_form)

```
digit excluding zero = "1" | "2" | "3" | "4" | "5" | "6" | "7" | "8" | "9" ;
digit                = "0" | digit excluding zero ;
```

---
 
# Backus–Naur form (BNF) Variants

> Augmented Backus–Naur form (ABNF) and Routing Backus–Naur form (RBNF) are extensions commonly used to describe Internet Engineering Task Force (IETF) protocols.
[wikipedia](https://en.wikipedia.org/wiki/Augmented_Backus%E2%80%93Naur_form)

```
rule = definition ; comment CR LF
``` 

<!-- Vi kommer til å se på dette litt nærmere når vi skal se på oppgaveløsning. 
For de som er kjent med RFCer så er dette brukt i stort sett alle.
--->