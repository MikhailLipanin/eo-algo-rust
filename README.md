<img alt="logo" src="https://www.objectionary.com/cactus.svg" height="100px" />

[![EO principles respected here](https://www.elegantobjects.org/badge.svg)](https://www.elegantobjects.org)
[![DevOps By Rultor.com](http://www.rultor.com/b/objectionary/eo)](http://www.rultor.com/p/objectionary/eo)
[![We recommend IntelliJ IDEA](https://www.elegantobjects.org/intellij-idea.svg)](https://www.jetbrains.com/idea/)

[![mvn-linux](https://github.com/MikhailLipanin/eo-algo-rust/actions/workflows/build.yml/badge.svg)](https://github.com/MikhailLipanin/eo-algo-rust/blob/master/.github/workflows/build.yml)
[![PDD status](http://www.0pdd.com/svg?name=MikhailLipanin/eo-algo-rust)](http://www.0pdd.com/p?name=MikhailLipanin/eo-algo-rust)
[![Hits-of-Code](https://hitsofcode.com/github/MikhailLipanin/eo-algo-rust?branch=master)](https://hitsofcode.com/github/MikhailLipanin/eo-algo-rust/view?branch=master)
[![Lines of code](https://img.shields.io/tokei/lines/github/MikhailLipanin/eo-algo-rust)](https://img.shields.io/tokei/lines/github/MikhailLipanin/eo-algo-rust)
[![License](https://img.shields.io/badge/license-MIT-green.svg)](https://github.com/MikhailLipanin/eo-algo-rust/blob/master/LICENSE)

Takes as an input a program in [EO](https://www.eolang.org/) and converts it to
a semantically equivalent program with included `rust` objects, which will be
separately executed via [Rust](https://www.rust-lang.org/).

## Usage

`TODO`

## Example

Consider the following EO-code:

```
[] > file
  [] > eof /bool
  [] > next /string
file > f
memory 0 > a
goto
  [g]
    seq > @
      at.
        QQ.txt.sscanf
          "%d"
          f.next
        0
      if.
        (t.mod 3).eq 0
        a.write
          a.plus
            t.mul t
        g.backward
      if.
        f.eof
        g.forward a
        g.backward
QQ.io.stdout
  QQ.txt.sprintf
    "a = %d"
    a
```

Here, the `goto` object can be optimized via the object `rust`:

```
goto
  [g]
    rust
      """
      pub fn f(&mut uni: Universe, v: u32) {
        let t = uni.da("Φ.f.next.Δ")
          .as_string().parse::<i32>()?;
        if t % 3 == 0 {
          let mut a = uni.da("Φ.a.Δ").as_int();
          a = a + t;
          let write = u.copy("Φ.a.write");
          let a0 = u.add();
          uni.bind(write, a0, "𝛼0");
          uni.put_int(a0, a);
          uni.da(format!("𝑣{write}"));
        } else {
          uni.da(format!("𝑣{v}.𝛼0.backward"));
        }
        let eof = uni.da("Φ.f.eof.Δ").as_bool();
        if eof {
          let f = u.copy("𝑣{v}.𝛼0.forward");
          uni.bind(f, 0, "𝛼0/Φ.a");
          uni.da(format!("𝑣{f}")!);
        } else {
          uni.da(format!("𝑣{v}.𝛼0.backward")!);
        }
      }
      """
```

## How to Contribute

You will need JDK 11+ and Maven 3.8+. Clone the repo and run the build like
this:

```
$ mvn clean install -Pqulice
```
