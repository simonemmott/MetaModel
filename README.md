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

The meta model for an application is created by reflecting the `Application Configuration` class of the application.

The code below shows an example of creating a reflection of an applications configuration class.

```java
private MetaModel metaModel = MetaModel.reflect(AppConfig.class);
```

### Configuration Classes

The configuration of K2 Applications is contained in two types of class.

1. The Application Configuration Class
1. The Service Configurations Classes

#### The Application Configuration Class

The `Application Configuration` class is any class that has been annotated with the `@MetaApplication` annotation.

Each K2 Application has one and only one `Application Configuration` class. This class provides the root from which the application meta model is reflected.
The *exact* implementation of the class annotated with the `@MetaApplication` annotation is irrelevant as the meta models reflection of the application is based entirely on the values of the `@MetaApplication` annotation.

The table below lists the parameters of the `@MetaApplication` annotation.

| Value        | Description |
|--------------|-------------|
| alias        | The alias of the application. This value is used internally to the K2 application when identifying the application configuration |
| title        | The title of the application. By default this value is displayed on the application splash page |
| description  | The description of the application. The application description is available through the `About` menu item |
| version      | The version of the application expressed as a `@MetaVersion` annotation |
| organisation | The organisation supplying the application |
| website      | The website of the organisation supplying the application |
| services     | The services implemented by this application expressed as an array of classes where each class is a `Service Configuration` class |

The code below shows an example of an `Application Configuration` class.

```java
package example.app;

import com.k2.MetaModel.annotations.MetaApplication;
import com.k2.MetaModel.annotations.MetaVersion;

import example.app.serviceA.ServiceAConfig;
import example.app.serviceB.ServiceBConfig;

@MetaApplication(
		alias="testApplication",
		title="Test Application",
		description="This is a dummy application for testing the generation of the metamodel",
		version=@MetaVersion(major=1, minor=2, point=3, build=1234),
		organisation="k2.com",
		website="http://www.k2.com",
		services= {ServiceAConfig.class, ServiceBConfig.class}
		)
public class AppConfig {}
```

#### The Service Configuration Classes

The `Service Configuration` classes define the configuration of each service implemented by the application.

A `Service Configuration` class is any class annotated with the `@MetaServce` annotation. Each service has one and only one service configuration class.
The service configuration class is referenced from the `Application Configuation` class's value for `services` in the `@MetaApplication` annotation.
The *exact* implementation of the class annotated with the `@MetaService` annotation is irrelevant as the service meta model is extracted from the `@MetaService` annotation.

The table below lists the parameters of the `@MetaService` annotation.

| Value             | Description |
|-------------------|-------------|
| alias             | The alias of the service. This value is used internally to the K2 application when identifying the service configuration |
| title             | The title of the Service. By default this value is displayed on the Services home page |
| description       | The description of the service. The service by default displayed on the Services home page within the application |
| version           | The version of the service expressed as a `@MetaVersion` annotation |
| modelPackageNames | An array of stings identifying the packages containing the classes defined by this service. The contents of these packages
and their sub-packages are scanned for service classes |

The java source code below shows an example of a `Service Configuration` class.

```java
package example.app.serviceA;

import com.k2.MetaModel.annotations.MetaService;
import com.k2.MetaModel.annotations.MetaVersion;

@MetaService(
		alias="serviceA",
		title="Service A",
		description="This is test service A",
		version=@MetaVersion(major=0, minor=1, point=2), 
		modelPackageNames = { "example.app.serviceA.model" }
		)
public class ServiceAConfig { }
```

### Service Classes

The service classes define the functionality of the service. Service classes can be persistent or transient. Persistent classes are annotated with JPA annotations and persisted in the services database. Transient classes do not persist but can be saved temporarily into the users session and are lost entirely when the users session ends.

Service classes are annotated with several `@Meta*` annotations and the values from these annotations and the classes fields and methods etc are reflected
into the meta models meta type for the class.

The table below lists the `@Meta*` annotations that are used to ascribe the meta data to the service classes for reflection into the meta model.

| Value             | Description |
|-------------------|-------------|
| @MetaVersion      | The version of this service class |
| @MetaType         | Meta data items common to all data types in K2 Applications. All service classes must be annotated with this annotation in order to be identified when reflecting the service class |
| @MetaClass        | This annotation identifies that the annotated type is a service class and defines the meta data applicatble to all types of class |
| @MetaInterface    | This annotation identifies that the annotated type is a service interface and define the meta data applicable to interfaces |
| @MetaSubType      | This annotation identifies a type as a sub type (enumeration). Sub types in K2 applications are define on the class for which they define a sub typing of that class. |
| @MetaSubTypeValue | This annotation defines meta data about a sub types values |
| @MetaEntity       | This annotation defines the meta data about a persistable class that can be externally referenced. |
| @MetaEmbeddable   | This annotation defines the meta data about a persistable class that cannot be externally referenced. |
| @MetaTransient    | This annotation defines the meta data about a non-persistable class that can saved to the users session. |
| @MetaOwningSet    | For simplicities sake K2 Applications restrict the use of cascading persisting collections to sets of persistable classes. This annotations a set that is used to contain/own the data within it. Such sets are set to JPA cascade all. 
| @MetaField        | This annotation is used to identify a field of a service class. Service class fields can be presented to the user automatically though the K2 UI. This annotation defines the meta data that can be ascribed to all fields. |
| @MetaTypeField    | This annotation identifies a field as a specifying a sub type value and define the meta data applicable to sub type fields |
| @MetaLinkField    | This annotation identifies a field as a link field and defines the meta data applicable to link fields |

#### @MetaVersion

The Table below lists the values than can be assigned to the `@MetaVersion` annotation

| Value | Description |
|-------|-------------|
| major | The major version number |
| minor | The minor version number |
| point | The point version number |
| build | The build number |

#### @MetaType

The Table below lists the values than can be assigned to the `@MetaType` annotation

| Value       | Description |
|-------------|-------------|
| alias       | The default alias to use when referring to instances of this type. |
| title       | The default title to to display when referring to an instance of this type |
| description | The description of this type to display when showing contextual help for this type |

#### @MetaClass

The Table below lists the values than can be assigned to the `@MetaClass` annotation

| Value | Description |
|-------|-------------|
|  |  |

#### @MetaInterface

The Table below lists the values than can be assigned to the `@MetaInterface` annotation

| Value | Description |
|-------|-------------|
|  |  |

#### @MetaSubType

The Table below lists the values than can be assigned to the `@MetaSubType` annotation

| Value | Description |
|-------|-------------|
|  |  |

#### @MetaSubTypeValue

The Table below lists the values than can be assigned to the `@MetaSubTypeValue` annotation

| Value | Description |
|-------|-------------|
| title | The title of the sub type value to display when showing the value of a sub type or allowing selection of a sub type value |
| description | The description of a sub type value to display when showing contextual help for this sub type value |

#### @MetaEntity

The Table below lists the values than can be assigned to the `@MetaEntity` annotation

| Value | Description |
|-------|-------------|
|  |  |

#### @MetaEmbeddable

The Table below lists the values than can be assigned to the `@MetaEmbeddable` annotation

| Value | Description |
|-------|-------------|
|  |  |

#### @MetaTransient

The Table below lists the values than can be assigned to the `@MetaTransient` annotation

| Value | Description |
|-------|-------------|
|  |  |

#### @MetaOwningSet

The Table below lists the values than can be assigned to the `@MetaOwningSet` annotation

| Value       | Description |
|-------------|-------------|
| owningClass | The class containing the owning set |
| name        | The field name of the owning set |

#### @MetaField

The Table below lists the values than can be assigned to the `@MetaField` annotation

| Value       | Description |
|-------------|-------------|
| alias       | The java name of the field and default alias of variables that reference this field  |
| title       | The title of the field to be show an the field label through the UI and when identifying this field in contextual help |
| description | The description of the field to be shown in contextual help |
| required    | Whether or not this field is required thought the UI by default or not | 
| enabled     | Whether this field should be included in the UI by default or not |

#### @MetaTypeField

The Table below lists the values than can be assigned to the `@MetaTypeField` annotation

| Value | Description |
|-------|-------------|
|  |  |

#### @MetaLinkField

The Table below lists the values than can be assigned to the `@MetaLinkField` annotation

| Value | Description |
|-------|-------------|
|  |  |
























