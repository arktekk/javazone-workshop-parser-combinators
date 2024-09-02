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
// sløfyer typen, type inference

// metodekall
println("na" * 10 + " Batman")
// val res5: String = nananananananananana Batman

```
<!--
For de observante så kan man se at vi har operator overloading.
Dette er ikke en ikke mye brukt feature av scala, men tok det med her
for å vise at det finnes.
-->


---

```scala 3

// Metode definisjon
def sayHello(who: String): Unit = println(s"Hello $who")
def sayHello(who: String) = println(s"Hello $who")
// sløfyer returtypen, type inference

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
## Algebraiske datatyper


```scala 3
enum EvenOdd {    
    case Even(num: Int)
    case Odd(num: Int)
}


def even(x: Int): EvenOdd = if isEven(x) then EvenOdd.Even(x) else EvenOdd.Odd(x)


```

---
## case class

```scala 3
case class Dog(name: String)
// Tilsvarer data class i Kotlin - Record types i Java
// men med superkrefter

val dog = Dog("Fido")

println(dog)
//printer ut Dog(Fido)

```

---
## tuple

```scala 3
// tilsavarer Pair i kotlin, men kan ha uendelig aritet
val tuple: (String, Int) = ("Don't Panic", 42)

println(tuple)
//printer ut (Don't, Panic, 42)

```

---
# Hva er en parser?
> A parser is a software component that takes input data (typically text) and builds a data structure – often some kind of parse tree, abstract syntax tree or other hierarchical structure, giving a structural representation of the input while checking for correct syntax. 
[wikipedia](https://en.wikipedia.org/wiki/Parsing#Parser)

<!-- 
Hvor mange her har brukt parsere før?

Det finnes mange måter å representere parsere på, i de følgende slidene kommer 
vi  til å se litt mer på hvordan dette kan representeres i Scala.
Til slutt skal vi vise noen eksempler.

Men først et sidespor med noen flere definisjoner.

-->

---
# Backus–Naur form (BNF)


> In computer science, Backus–Naur form (/ˌbækəs ˈnaʊər/) (BNF or Backus normal form) is a notation used to describe the syntax of programming languages or other formal languages. It was developed by John Backus and Peter Naur. BNF can be described as a metasyntax notation for context-free grammars. [...] BNF can be used to describe document formats, instruction sets, and communication protocols.

[wikipedia](https://en.wikipedia.org/wiki/Backus%E2%80%93Naur_form)

<!--
Dette er et språk for å formelt beskrive parsere, De finnes i to vanlige former.
-->

---

# Backus–Naur form (BNF) Variants

> In computer science, extended Backus–Naur form (EBNF) is a family of metasyntax notations, any of which can be used to express a context-free grammar. EBNF is used to make a formal description of a formal language such as a computer programming language.

[wikipedia](https://en.wikipedia.org/wiki/Extended_Backus%E2%80%93Naur_form)

```
digit excluding zero = "1" | "2" | "3" | "4" | "5" | "6" | "7" | "8" | "9" ;
digit                = "0" | digit excluding zero ;
```

<!--
Dette er en vanlig måte å beskriver parsere på, spesielt i programminggspråk.
Det siste oppgavesettet kommer til å bruke dette.
-->


---

# Backus–Naur form (BNF) Variants

> Augmented Backus–Naur form (ABNF) and Routing Backus–Naur form (RBNF) are extensions commonly used to describe Internet Engineering Task Force (IETF) protocols.

[wikipedia](https://en.wikipedia.org/wiki/Augmented_Backus%E2%80%93Naur_form)

```
rule = definition / alternate definition ; comment CR LF
``` 

<!-- 
Denne varianten av BNF er den vi skal bruke mest i denne workshoppen.
Oppgavesett 2, pluss i deler av oppgavesett 1.

For de som er kjent med RFCer så er dette brukt i stort sett alle.
--->

---

# ABNF eksempel - JSON Pointer
[RFC6901](https://www.rfc-editor.org/rfc/rfc6901)

Dette her kommer igjen som en oppgave i oppgavesett 2

```
      json-pointer    = *( "/" reference-token )
      reference-token = *( unescaped / escaped )
      unescaped       = %x00-2E / %x30-7D / %x7F-10FFFF
         ; %x2F ('/') and %x7E ('~') are excluded from 'unescaped'
      escaped         = "~" ( "0" / "1" )
        ; representing '~' and '/', respectively
```

<!-- 
Dette er et eksempel på full parser basert på ABNF.
VI har lagt ved en PDF i foredragsfolderen i prosjektet. 
Den pdfen forklarer de forskjellige bestandddelene av syntaksen.

Nå når vi har sett litt på hvordan formelt beskrive en parser, la oss se på hvordan
dette kan se ut i Scala.
--->

---

# Hva er en parser?

```scala 3
def parser[I, O](input: I): ParseResult[I, O] = ???
```

<!-- 
En parser er kode som transformerer noe input av typen I til 
noe av output av typen `ParseResult[I, O]`.

ParseResult vil inneholde posisjonsdata for inputen, og eventuelle feil.
-->

---

# Hva er en parser?

```scala 3
def parser[I, O]: I => ParseResult[I, O] = ???
```

<!-- 
Litt forenklet kan man se på det som en funksjon fra I til ParseResult[I, O].
Denne er ekvivalent med med definisjonen på forrige slide. 
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
Det som er viktig her er at vi kan for enkelhetensskyld på en streng som en collection av karakterer og
det er det vi håndterer.

-->


---

# Hva er en parser?

```scala 3
final case class Parser[O](run: String => ParseResult[O])
```

<!-- 
Det som vi kan gjøre med nå når vi har etablert hva en parser er, så kan vi pakke den inn i en datatype.
Det finnes flere mulige måter å gjøre det på.
Her har vi pakket inn en funksjon inn i en case klasse.
-->

---

# Hva er en parser?

```scala 3
trait Parser[O] {
  def parse(input: String): ParseResult[O]
}
```

<!-- 
Her er den samme definisjonen som på forrige slide, men bruker et trait i stedet.
Med en gang vi har en datatype, så kan vi definere operasjoner på den.
Disse operasjonene blir typisk kalt kombinatorer, derav navnet.
-->

--- 
# Cats Parse

```scala 3

sealed trait Parser[A] extends Parser0[A] {
  def parse(str: String): Either[Parser.Error, A] = ???
}
```

<!-- 
I cats-parse som vi kommer til å bruke her, så ser signaturen slik ut.
`cats-parse` er substring orientert, så vi ser på biter av en streng, og henter ut informasjon fra den.
Dette betyr at vi setter sammen parsere som matcher biter av strenger til vi når EOI (End of Input).
-->

---
## Kombinatorer

```scala 3

  def map[B](f: A => B): Parser[B] = ???
  def as[B](const: B): Parser[B] = map(_ => const)
  
  // forkast output fra min parser og bruk denne. 
  def *>[B](p: Parser[B]): Parser[B] = ???
  
  // forkastg output fra denne parseren og bruk min
  def <*[B](p: Parser[B]): Parser[A] = ???
  
  // parse også dette, og returner resultatet av min og denne i tuppel.
  def ~[B](p: Parser[B]): Parser[(A, B)] = ???

```

<!-- 
Her har vi definert flere operasjoner som hver av seg produserer en ny parser er kombinert

La oss ta for oss noen av disse:

map er den enkleste her. Den transformerer resultatet av parseren til noe annet.
as er egentlig bare et kall til map der vi ignorerer input til funksjonen.

De tre neste operasjonene er symboltunge, og er de som er brukt i cats-parse som
vi skal bruke i oppgaveløsningen. Ikke få panikk av dette. 
Se i ressurser.md filen i foredrag folderen for mer hjelp.
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