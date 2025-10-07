

## Place the specified times placement modifier.

This placement modifier works like the Place Only Once placement modifier,
but instead a specified number of times is instead specified.

The Place Only Once placement modifier is thus a shortcut for this modifier,
and specifying the `attempts_for_placement` field as 1.

Placement Modifier Syntax:

~~~
object(PlaceTheSpecifiedTimesPlacementModifier), required, since_mod_version="1.5.0"
{
	"type": resourcelocation(PlacementModifier), required, must_have_value="mdex:place_with_exact_attempts"
	"attempts_for_placement": int, required, range=[0, 32767]
	"discard_decrementing_attempt_probability": float, optional, range=[0, 1], default_value=1
}
~~~


| Field Name                                                                   | Description                                                                                                                                                                         |
|---------------------------------------------------------------|----------------------------------------------------------------------------------------------------------------------------------------------|
| type                                                                            | Defines the type of the placement modifier to use. This must always be the value `mdex:place_with_exact_attempts`. |
| attempts_for_placement                                           | Defines the number of exact placement attempts to do.  |
| discard_decrementing_attempt_probability             | Defines a value whether the exact placement is 'loose', that is, a placement attempt might not decrement the number of remaining attempts if this value is less than 1. Values of zero is like this placement modifier is always passing. |


If in case that you have not noticed it, the name for referencing this placement modifier is `mdex:place_with_exact_attempts`.
