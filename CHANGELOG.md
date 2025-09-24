Now releasing 1.4.0:

-> The mineshaft's placement height can be now configured, if deemed necessary
-> The block state providers have a code rewrite. A new block state provider was added mimicking the ore feature targets field and is called Rule Test Based Block State Provider.

Bugfixes and performance optimizations were done, such as:

-> Codecs provided through codec utilities do no longer perform a second class creation to provide checking services, instead one class instance is created at run-time now.
-> The block state objects now do faster execute because the property entries are retrieved through key-value pairs, thus less CPU overload is required to fetch properties.
-> Some registries that were still using reflection to get several type objects is now replaced with their instance fields respectively.
-> The rotated block state provider will no longer crash the game when any block declared through the state field does not have the axis property, instead it will now appropriately fail compilation.
-> The loot table/specific appender processors now try to initialize the random source they get to ensure that the results that they produce are valid. Might change structure loot table generation by a bit, but I do not consider it a breaking change.
-> Rule test objects were not using effectively the Distributed Compilation Object logic. Now, they do.