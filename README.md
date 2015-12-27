FOOTRAN I
=========

[![Build Status](https://travis-ci.org/oreissig/FOOTRAN-I.svg)](https://travis-ci.org/oreissig/FOOTRAN-I)
[![Dependency Status](https://www.versioneye.com/user/projects/564656d6b5b03d0022000862/badge.svg?style=flat)](https://www.versioneye.com/user/projects/564656d6b5b03d0022000862)

A modern implementation of a compiler for the very first version of FORTRAN based on [IBM's official Reference Manual from 1956](http://www.fortran.com/FortranForTheIBM704.pdf).

![Fortran Programmer's Reference Manual](https://upload.wikimedia.org/wikipedia/commons/thumb/0/07/Fortran_acs_cover.jpeg/469px-Fortran_acs_cover.jpeg)

This project is licensed under an ISC license, see LICENSE file.

To Do:
------
- `[X]` basic punch card handling (comments, continuations, statement numbers)
- `[X]` parsing of constants, variables and subscripts
- `[X]` arithmetic formulas and simple expressions
- `[X]` complex arithmetic expressions (`+-*/`)
- `[X]` control statements
- `[ ]` input/output statements
- `[X]` specification statements
- `[ ]` interpreter
- `[ ]` optimizations
- `[ ]` code generation

Strech goals:
-------------
- `[ ]` function statements (have been added in a later revision of the reference)
