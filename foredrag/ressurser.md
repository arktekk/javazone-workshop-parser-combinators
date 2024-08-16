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

Ressurser

---
# cats.parse.Rfc5234

```
object Rfc5234 {
  /** A-Z and a-z */
  val alpha: Parser[Char] = ...
  
  /** Internet standard newline */
  val crlf: Parser[Unit] = ...

  /** white space (space or horizontal tab) */
  val wsp: Parser[Unit] = ...
  ...
}
```
---
# cats.parse.Numbers

```
object Numbers {
  /** a single base 10 digit */
  val digit: Parser[Char] = ...

  /** A string matching the json specification for numbers. from:
    * https://tools.ietf.org/html/rfc4627
    */
  val jsonNumber: Parser[String] = ...
  ...
}
```

---

# Augmented Backus–Naur Form (ABNF)
[wikipedia](https://en.wikipedia.org/wiki/Augmented_Backus%E2%80%93Naur_form)

---
### cats-parse cheat sheet
```scala
val a: Parser[A] = ???
val b: Parser[B] = ???
```
| Kombinator | Beskrivelse                                    | Resultat |                                                               
|:----------:|:-----------------------------------------------|:---------|
|   a \| b   | parse `a` eller `b`                            | ?        |
|   a ~ b    | parse `a` og så `b`                            | (A, B)   |
|   a *> b   | parse `a` og så `b`, behold resultatet til `b` | B        |
|   a <* b   | parse `a` og så `b`, behold resultatet til `a` | A        |

---

|                 Kombinator                 | Beskrivelse                                        | Resultat        |                                                               
|:------------------------------------------:|:---------------------------------------------------|:----------------|
|  a.rep<br/>a.rep(min)<br/>a.rep(min,max)   | repeter parser `a` minst en gang                   | NonEmptyList[A] |              
| a.rep0<br/>a.rep0(min)<br/>a.rep0(min,max) | repeter parser `a` 0 eller flere ganger            | List[A]         |
|                    a.?                     | parse `a` eller ingenting                          | Option[A]       |
|                  a.as(1)                   | parse `a` og erstatt resultatet med 1              | Int             |

---
|                            Kombinator                            | Beskrivelse                            | Resultat        |                                                               
|:----------------------------------------------------------------:|:---------------------------------------|:----------------|
|  a.repSep(sep)<br/>a.repSep(min,sep)<br/>a.repSep(min,max,sep)   | parser a,a,a,a<br/>min > 0, min >= max | NonEmptyList[A] | 
| a.repSep0(sep)<br/>a.repSep0(min,sep)<br/>a.repSep0(min,max,sep) | samme som repSep, men min >= 0         | List[A]         |
|                        a.surroundedBy(b)                         | parser "bab"                           | A               |
|                         a.between(b, c)                          | parser "bac"                           | A               |

---

|                     Kombinator                      | Beskrivelse                                                                                    | Resultat |                                                               
|:---------------------------------------------------:|:-----------------------------------------------------------------------------------------------|:---------|
|               a.map(f)<br/>f: A => C                | parse `a`, transformer resultatet vha funksjonen f                                             | C        |
| a.mapFilter(f)<br/>f:&nbsp;A&nbsp;=>&nbsp;Option[C] | parse `a`, transformer resultatet vha funksjonen f. Parseren feiler hvis resultatet er None    | C        |      
|                      a.repSep                       | 
- 
