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
# Motivasjon

Hvorfor er vi her?

<!--

Hvor mange her har brukt regulære utrykk?

Flere har brukt Regulære utrykk for å hente ut data fra 
strenger. Det vi skal se på i denne workshoppen er et alternativ til dette.
En vits som ofte fortelles i forbindelse med regulære utrykk er:

Jeg hadde et problem som jeg valgte å løse med regulære utrykk, nå har jeg to problemer.

Grunnen til man ønsker å bruke parser combinators er at man lager små programmer som
hver for seg er testbare.

Når man setter de sammen så får man et større program som også er testbart.

Vi kommer til å gå gjennom vanlige teknikker som man kan ta tilbake til prosjektene sine.

For at vi skal kunne snakke om hvordan parser-combinators ser ut i Scala3 som vi skal 
bruke i dag, så trenger vi å gå litt gjennom basic Scala syntaks.

-->

---
### Agenda (før pausen)

- litt om scala
- litt om parser combinators
- oppgaver

---
### Agenda (etter pausen)

- json parser i plenum
  - arbeidsflyt  
  - backtrack
  - withContext
- oppgaver
  - json-pointer
  - toml
  
---

### En Scala primer

```scala 3
// en verdi av typen Int
val value: Int = 1
val value = 1

// metodekall
println("na" * 10 + " Batman")
// val res5: String = nananananananananana Batman

```

---

```scala 3

// Metode definisjon
def sayHello(who: String): Unit = println(s"Hello $who")
def sayHello(who: String) = println(s"Hello $who")

// metode definisjon med generiske typer
def generic[A](input: A): Unit = println(s"called with ${input}")
generic("World")
generic(1)
generic(1.0)
```

---
```scala 3
// siste utrykk returneres
def isEven(x: Int): Boolean = x % 2 == 0
// I Scala er if et utrykk
def evenString(x: Int) = if isEven(x) then "even" else "odd"


enum EvenOdd {
    case Even, Odd
}

def even(x: Int) = if isEven(x) then EvenOdd.Even else EvenOdd.Odd

```

---
## case class

```scala 3
case class Dog(name: String)

val dog = Dog("Fido")

println(dog)

```


---
## Algebraiske datatyper


```scala 3
enum EvenOdd {    
    case Even(num: Int)
    case Odd(num: Int)
}


def even(x: Int): EvenOdd = if isEven(x) then EvenOdd.Even(x) else EvenOdd.Odd(x)


```

---
# Hva er en parser?
> A parser is a software component that takes input data (typically text) and builds a data structure – often some kind of parse tree, abstract syntax tree or other hierarchical structure, giving a structural representation of the input while checking for correct syntax. 
[wikipedia](https://en.wikipedia.org/wiki/Parsing#Parser)

---

# Hva er en parser?

```scala 3
def parser[I, O](input: I): ParseResult[I, O] = ???
```

<!-- 
En parser er kode som transformerer noe input I til 
noe av output av `ParseResult[I, O]`.

ParseResult vil inneholde posisjonsdata for inputen, og eventuelle feil.
-->

---

# Hva er en parser?

```scala 3
def parser[I, O]: I => ParseResult[I, O] = ???
```

<!-- 
Litt forenklet kan man se på det som en funksjon fra I til ParseResult[I, O].
-->

---

# Hva er en parser?

```scala 3
def parser[O]: String => ParseResult[O] = ???
```

<!-- 
Enda mer forenklet og det vi kommer til å bruke i resten av presentasjonen og 
oppgaveløsningen er at vi tar en input av String og produserer et resultat av 
en type O.
-->


---

# Hva er en parser?

```scala 3
final case class Parser[O](run: String => ParseResult[O])
```

<!-- 
Det som vi kan gjøre med nå når vi har etablert hva en parser er, så kan vi pakke den inn i en datatype.
Det finnes flere mulige måter å gjøre det på.
Her har vi pakket inn en funksjon inn i en case klasse, men vi kan også lage et interface eller det som i Scala blir kalt et trait.
-->

---

# Hva er en parser?

```scala 3
trait Parser[O] {
  def parse(input: String): ParseResult[O]
}
```

<!-- 
Det som vi kan gjøre med nå når vi har etablert hva en parser er, så kan vi pakke den inn i en datatype.
Det finnes flere mulige måter å gjøre det på.
Her har vi pakket inn en funksjon inn i en case klasse, men vi kan også lage et interface eller det som i Scala blir kalt et trait.
-->


--- 
# Cats Parse

I cats-parse som vi kommer til å bruke her, så ser signaturen slik ut.

```scala 3

sealed trait Parser[A] extends Parser0[A] {
  def parse(str: String): Either[Parser.Error, A] = ???
}
```


`cats-parse` er substring orientert, så vi ser på biter av en streng, og henter ut informasjon fra den.
Dette betyr at vi setter sammen parsere som matcher biter av strenger til vi når EOI (End of Input).


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
* `val alphaNum: Parser[Char] = alpha | digit` leses som: alpha *eller* digit

<!-- 
Da kan vi se at vi setter sammen digit og alpha for å lage en ny parser som gjør begge deler.
Hver av disse kan testes for seg selv.
-->


---
# Eksempel på parsere

```scala 3
val digit = Parser.charIn("1234567890")

val alpha = Parser.charIn(('a' to 'z') ++ ('A' to 'Z'))

// leses som: 1 eller flere alphaNum chars lagret unna som en streng 
val alphanum: Parser[String] = (alpha | digit).rep.string

// leses som: alpha påfølgende av 0 eller flere alphaNum chars lagret unna som en streng 
val identifier = (alpha ~ alphaNum.rep0).string


val string = Parser.string("input")
```

---
# Oppgaver