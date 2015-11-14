FOOTRAN I
=========

[![Build Status](https://travis-ci.org/oreissig/FOOTRAN-I.svg)](https://travis-ci.org/oreissig/FOOTRAN-I)
[![Dependency Status](https://www.versioneye.com/user/projects/564656d6b5b03d0022000862/badge.svg?style=flat)](https://www.versioneye.com/user/projects/564656d6b5b03d0022000862)

A modern implementation of a compiler for the very first version of FORTRAN based on [IBM's official Reference Manual from 1958](http://bitsavers.trailing-edge.com/pdf/ibm/704/C28-6003_704_FORTRAN_Oct58.pdf).

This project is licensed under an ISC license, see LICENSE file.

To Do:
------
- `[X]` basic punch card handling (comments, continuations, statement numbers)
- `[X]` parsing of constants, variables and subscripts
- `[.]` arithmetic formulas and expressions
- `[ ]` function statements and calls
- `[ ]` `GO TO` and friends
- `[ ]` `IF` and friends
- `[ ]` the `DO` loop
- `[ ]` input/output statements
- `[ ]` specification statements
- `[ ]` interpreter
- `[ ]` optimizations
- `[ ]` code generation
