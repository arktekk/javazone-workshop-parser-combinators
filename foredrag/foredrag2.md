---
theme: gaia
_class: lead
paginate: true
backgroundColor: #fff
backgroundImage: url('https://marp.app/assets/hero-background.svg')
marp: true
---

![bg left:40% 80%](assets/logo.svg)

# **Parser Combinators del 2**

En parser som spiser parsere

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
rule = definition / alternate definition ; comment CR LF
``` 

<!-- Vi kommer til å se på dette litt nærmere når vi skal se på oppgaveløsning. 
For de som er kjent med RFCer så er dette brukt i stort sett alle.
--->

---

# ABNF eksempel - JSON Pointer
[RFC6901](https://www.rfc-editor.org/rfc/rfc6901)

Dette her kommer igjen som en oppgave

```
      json-pointer    = *( "/" reference-token )
      reference-token = *( unescaped / escaped )
      unescaped       = %x00-2E / %x30-7D / %x7F-10FFFF
         ; %x2F ('/') and %x7E ('~') are excluded from 'unescaped'
      escaped         = "~" ( "0" / "1" )
        ; representing '~' and '/', respectively
```

