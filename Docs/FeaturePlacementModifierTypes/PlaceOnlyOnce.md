


## Place only once placement modifier.

This does what it's name implies: places the specified configured feature only 
once in all the biomes/dimensions this feature is referenced to, for a given session.

The first object accessing this modifier will return the position passed to it and all the
other subsequent placement attempts will fail, until the world where this modifier runs is re-opened.

It is useful for cases that you want to generate only once the feature for every Minecraft session.

This placement modifier does not have additional fields; it is just defined.

The name for referencing this placement modifier is `mdex:place_only_once`.

