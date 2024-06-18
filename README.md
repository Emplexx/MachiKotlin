# Machi Koro in Kotlin
A functional CLI implementation of the tabletop game Machi Koro written in Kotlin.
It covers the base game - no Harbour expansion

# Is it complete?
Almost, it implements everything except for the Business Centre because I got lazy

# Is it really functional? 
Maybe
- There is no mutable state, *everything* is immutable
- All functions that are non-deterministic (`Dice.throw*()`) or do side effects (`println()` and `readln()`) are marked with `suspend` (Kotlin's equivalent of `IO`)
- The main game loop is done with recursion 

# TODO?
- [ ] Trading Centre
- [ ] Variable Supply Variant
- [ ] Hybrid Supply Variant
- [ ] More readable interface
- [ ] Player names?
- [ ] Saving and loading?