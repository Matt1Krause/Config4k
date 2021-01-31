# Design
## Mentality
- This framework does not aim to be yet another serialization library. This fact should reflect in its overall design.
One consequence is the absence of a serialization feature, only reading of configuration data is supported. 
- The framework should support blocking and non-blocking(suspending) use cases.
- The framework should emphasize type safety, e.g. sealed classes when possible
- The framework may only contain what is necessary, special integrations and 
  configuration format support are provided in separate modules.
## property loaders
### General
Property loaders are the core component of this framework. 
They allow reading of data through appropriate functions (see [below](#access-methods)). 
This data is referencable through a path, which is name convention agnostic, e.g. 
that the same path can be used for a json file in which names are in snake case and
system properties where names are in upper snake case. 
Data access is not their only functionality: They can also be used for 
routing, providing fallback options and caching path elements.  
### access methods
The access methods of the property loader should return primitive values when possible
without autoboxing and unnecessary object creation. The expected value should also be 
stated directly: if you want a string, ask for a string, all while keeping type safety.
The aforementioned object creation has its use cases, primarily because it allows 
general data access without care if the data is primitive data or composite data. 
This may be especially useful in the context of generic configuration objects.
 