# river-framework

## What is the River Framework?

This is an Object-Document Mapper Framework for NoSQL databases in **development stage**. You can read about the project at its [website](http://www.riverframework.org). Here you will find about the development itself: current and next features, known issues, dependencies, binaries, maven, demos, and, of course, the source code :-)


## Features

### The current version (0.2.10) has the following features: 

- Compiled for Java 1.6+ 
- Uses the Java Logging API
- Controls six basic elements:
  - Session
  - Database
  - Document
  - Views or collections
  - Document iterator
  - Document indexes and unique IDs
- Has two layers of control:
  - The wrapper layer, that connects directly to the database using its native classes. 
  - The core layer, that provides a unique interface to be able to use any wrapper library written for this framework 
- Has implemented method chaining
- To reduce impact on replication, if you set a field with a new value, it checks if it is different before do it.


### About the wrapper for IBM Notes:

- Supports only five basic elements to work with a Notes database (session, database, document, view, iterator)
- Anyway, you can still use the native Java library to do things like modify the ACL or work with RichText items.
- Supports local and remote sessions (DIIOP) 
- Auto recycling of Notes objects
- Has an object cache
- Supports field size > 32K
- Good to develop standalone Java programs, Servlets or XPages programs 


### About the wrapper for OpenNTF Domino:

- Takes advantage of this great library developed by [OpenNTF](http://www.openntf.org/main.nsf/project.xsp?r=project/OpenNTF%20Domino%20API)
- The auto recycling is managed by OpenNTF Domino API
- Supports field size > 32K
- Good to develop standalone Java programs, Servlets or XPages programs 


## Demo

A full demo as an Eclipse project of a stand-alone Java program is [here](https://github.com/mariosotil/river-framework-demo).


## Download

You can download the binaries as Jar libraries from the [OpenNTF website](http://www.openntf.org/main.nsf/project.xsp?r=project/River%20Framework/releases/).


## Maven

To load the artifacts from Maven, you can add one of these dependencies to your pom.xml file:

- To use the original lotus.domino package from IBM Notes

```xml
		<dependency>
			<groupId>org.riverframework</groupId>
			<artifactId>river-lotus-domino</artifactId>
			<version>0.2.10</version>
		</dependency>
```

- To use the org.openntf.domino package from OpenNTF

```xml
    <dependency>
      <groupId>org.riverframework</groupId>
      <artifactId>river-org-openntf-domino</artifactId>
      <version>0.2.10</version>
    </dependency>
```

In both cases, you have to add to your classpath the `Notes.jar` library. For the last one, you will need to add the `org.openntf.domino.jar` library too.


## Dependencies

- [joda-time](http://www.joda.org/joda-time/), for parsing strings and convert them to `java.util.Date`
- [ini4j](http://ini4j.sourceforge.net/), for support unit testing and load a credentials file (~/.river-framework/credentials)


## Next features

Version 0.2.11
- Let that any IBM Notes class from the `lotus.domino` library could be wrapped, managed and recycled by the River Framework (ACL, RichText, etc.)
- It's possible that the factory from the IBM Notes wrapper be redesigned to support the last feature
- Eliminate the dependencies to joda-time and ini4j to make easier the use of the libraries
- Fixes and improvements

Version 0.3
- Support MongoDB

Version 0.4
- Support Node4J without graphs

Version 0.5
- Support graphs for relations between documents (1..\*, \*..\*) 
- Support Node4J, IBM Notes and MongoDB with graphs

Version 0.6
- Support a query language (SQL++?)

Version 0.7
- Support transactions
 
Version 0.8
- Support threads

Other features
- Logging of changes in the fields of a document
- Control of the data change propagation between related documents (1..\*, \*..\*)



## Changelog

Version 0.2.10
- Improved the design to work with document indexes
- Supported fields > 32K (IBM Notes and OpenNTF Domino API wrappers)
- Updating the documentation 
- Various fixes and improvements

Version 0.2.9
- ~~Redesigning in three layers~~
- Redesigned the core layer to allows the developer to write logic, rules, etc. for a single object. e.g. a document or a database
- Created a LoggerHelper class to make easier to control the `java.util.logging.Logger` objects
- Optimized the IBM Notes wrapper library
- Temporarily removed the support to Hazelcast wrapper library
- Various fixes 

Version 0.2.8
- Fixes and improvements
- Adding again the support to the `org.openntf.domino` library

Version 0.2.7
- Auto recycling of IBM Notes objects. So far, it works good with a standalone Java program. I still making tests in XPages.
- Temporarily removing the support to the `org.openntf.domino` library, considering that the auto recycling is working on the `lotus.domino` wrapper, and it's necessary to focus efforts in one wrapper at a time. 
- Adding more functionality (create and removing databases, views, etc.)

Version 0.2.6
- As the logical separation was done, physically separate the current Framework in three JAR files: the Core, the `lotus.domino` wrapper and the org.openntf.domino wrapper.
- Publish the JARs in a public Maven repository

Version 0.2.5
- Improvements in the design, wrapping the `lotus.domino` and the `org.openntf.domino` classes with the `org.riverframework.wrapper`classes
- The package `org.riverframework.core` will control the wrapped classes 
- Improvements in the design, making that the wrappers classes be totally separated from the core. So, each wrapper can be loaded on runtime.
- Improvements in DocumentCollection as array
- Redesign the JUnit tests for testing the wrappers for `lotus.domino` and the `org.openntf.domino`, and testing the core with each wrapper
- Improvements in the `org.riverframework.wrapper.lotus.domino` memory management. All the objects are recycled when the session is closed.
- Update the Java example

Version 0.2
- Generation of unique identifiers for the documents
- Opening documents by a unique id
- Credentials file (~/.river-framework/credentials)
- Support to local session using a installed IBM Notes client 
- First example in Java

Version 0.1
- Using IBM Notes as NoSQL server
- Lets you connect to an IBM Notes database and the possibility to access its views and create or modify documents.
- Document collections as arrays
- It has a set of `JUnit` tests


### Tags

Java, ODM, NoSQL, MongoDB, IBM Notes, Node4J, Lotus Notes, SSJS, ORM, Framework

