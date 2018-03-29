# MetaModel
The `MetaModel` provides meta information and services for classes in an application.

Examples of mata data are things like labels and help text for fields and query definition for lists among many others.

The `MetaModel` also provides automatically generated handler classes to execute methods, get and set field values and add new records to lists among others.

The javadoc for this package is available [here](https://simonemmott.github.io/MetaModel/index.html)

### License

[GNU GENERAL PUBLIC LICENSE v3](http://fsf.org/)

## Basic Example


## Getting Started

Download a jar file containing the latest version or fork this project and install in your IDE

Maven users can add this project using the following additions to the pom.xml file.
```maven
<dependencies>
    ...
    <dependency>
        <groupId>com.k2</groupId>
        <artifactId>MetaModel</artifactId>
        <version>0.1.0</version>
    </dependency>
    ...
</dependencies>
```

## Working With MetaModel

The meta model doesn't of its self do anything, instead it provides data to other classes in a K2 Application.

K2 Applications are composed of several implemented services grouped under an application configuration.

Each service in a K2 Application is comprised of classes that for part of that service and only that service. The classes within a service collaborate to provide the services offered by the service.

Each service implemented by a K2 Application is implemented as an independent stack of 
1. UI Layer
1. Controller Layer
1. Service Layer
1. Data Access Layer
1. Data Layer

The classes that comprised the a K2 Service can be persistent (JPA @Entity or @Embedded) or transient. In all cases however the classes are rich DDD (Domain Driven Design) classes and as such the logic of the
service layer is encapsulated in the classes and their fields and methods.

The meta model reflects the configuration classes of a K2 Application and the classes contained in each service implemented by the application and converts that data into an in memory structure in turn 
providing support data and services to K2 Applications and their implemented services.

### Configuration Classes

The configuration of K2 Applications is contained in two types of class.

1. The Application Configuration Class
1. The Service Configurations Classes

#### The Application Configuration Class

The `Application Configuration` class is any class that has been annotated with the `@MetaApplication` annotation.

Each K2 Application has one and only one `Application Configuration` class. This class provides the root from which the application meta model is reflected.
The *exact* implementation of the class annotated with the `@MetaApplication` annotation is irrelevant as the meta models reflection of the application is based entirely on the values of the `@MetaApplication` annotation.

| Value | Description |
|-------|-------------|
| alias | The alias of the application. This value is used internally to the K2 application when identifying the application configuration |



