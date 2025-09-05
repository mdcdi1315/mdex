

## Data Pack JSON Syntax

This document defines conventions and describes a 
syntax used for how the mod describes the fields 
in a JSON data file.

This is used to understand how is the JSON format for 
a JSON definition file, such as a specified configured feature type.

The syntax looks like JSON but is not.

A typical syntax is like the following:

~~~
object(AnyObjectClass), required
{
    "type": string , required
}
~~~

This is a simple JSON serialized object that decodes as `AnyObjectClass` and has a single required field named `type`, receiving only string values and is required.

In general, the syntax is as follows:
~~~
object(Type), required // <-- This must always exist as is. Only the 'Type' must be the class name of the serialized object.
{ // <-- This is an object definition
    "field-name": [Type] [Constraint-List] // Defines a simple field with a comma-delimited constraint list.
}
~~~

Supported types defined by the syntax and supported:

| Type                        | Syntax                                                                                                                                                                                                              |
|-----------------------------|---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| string                      | A simple JSON string value.                                                                                                                                                                                         |
| int                         | A simple JSON integer value.                                                                                                                                                                                        |
| float , double              | A simple JSON floating-point value.                                                                                                                                                                                 |
| resourcelocation(ClassName) | A Minecraft resource location ID. It's backing type is defined as the `ClassName` parameter. The value of this type is always a string.                                                                             |
| list&lt;NestedType&gt;      | A simple JSON array of the specified type. The `NestedType` must be replaced with another one of the members of this table. Containing multiple and nested arrays is also possible.                                 |
| object(ClassType)           | Another serialized JSON object of the specified class type defined in the `ClassType` parameter. The object's fields can be defined by using {} below the field declaration if deemed necessary by the implementer. |
| &lt;AlreadyDefinedType&gt;  | Specifies another serialized JSON object type. This is like an 'external reference'.                                                                                                                                |

Supported constraints for each field. Multiple constraints must be delimited with commas.

| Constraint Name | Usage                                                                                                                                                                                                                    |
|-----------------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| required        | Specifies that this field is a required field. Also applied for root JSON objects.                                                                                                                                       |
| can_be_empty    | Applied to a list type only. Specifies that the list can contain zero elements.                                                                                                                                          |
| optional        | Defines an optional field, that is, a field that is not required to be included in the JSON object. The constraint `default_value` must have been also defined as well.                                                  |
| default_value   | Defines the default value of an `optional` field. The value depends on the field's type. If the optional field is a JSON object, it's equivalent JSON representation must be used.                                       |
| range           | Defines a numeric range in the form [min , max]. Numbers not into this range fail the serialization. Also can be specified for [integer providers](https://minecraft.wiki/w/Template:Nbt_inherit/int_provider/template). |
| must_have_value | The value of a serialized field must always be the value specified after the equals sign.                                                                                                                                |

Example:
~~~
object(AnyObject), required
{
    "mods": list<string>, optional, default_value=[], can_be_empty
    "type": string, required
    "value": <IntProvider>, required
}
~~~