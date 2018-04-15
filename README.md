<img src="http://www.riverframework.org/images/river-header.png" /><br/>
[![Project Stats](https://www.openhub.net/p/river-framework/widgets/project_thin_badge.gif)](https://www.openhub.net/p/river-framework)

## A single interface for different NoSQL databases

It is an Object-Document Mapper for NoSQL databases in **development stage**. Here you will find about the development itself: current and next features, known issues, dependencies, binaries, maven, demos, and, of course, the source code :-)  You will find a presentation about the project at its [website](http://www.riverframework.org)

## Features (WORK IN PROGRESS)

- Compiled for Java 8+
- Support to Couchbase Lite
- Java Logging API
- Support to five basic elements:
  - Session
  - Database
  - Document
  - Views or collections
  - Document iterator
- Two layers of control:
  - The wrapper layer, that connects directly to the database using its native classes. 
  - The core layer, that provides a unique interface to be able to use any wrapper library written for this framework 

## Getting Started

WIP

## Demo

WIP

## Download

WIP

## Maven

WIP

## How to build

WIP


## Next features

Version 0.4
- Support Couchbase 

Version 0.5
- Support MongoDB

Version 0.6 
- Support CouchDB

Version 0.7
- Support Neo4J without graphs

Version 0.8
- Support graphs for relations between documents (1..\*, \*..\*) 
- Support Neo4J, Couchbase and MongoDB with graphs

Version 0.9
- Support a query language

Version 0.10
- Support transactions
 

Other features
- Logging of changes in the fields of a document
- Control of the data change propagation between related documents (1..\*, \*..\*)

## Changelog

Version 0.3.0
- Moved wrappers (lotus-domino, org-openntf-domino, and com-couchbase-lite) to their own repos
- Stopped to support IBM Notes until finish the redesign of the library 

Version 0.2.11
- Eliminate the dependencies to joda-time and ini4j to make easier the use of the libraries
- Fix this [issue](https://github.com/mariosotil/river-framework/issues/1) about Java security permissions needed
- Fix a problem about convert from string to date and vice versa. The format used is ISO 8601 (yyyy-MM-ddTHH:mm:ss.SSSZ)
- IBM Notes wrapper: fix a search problem. It was changed from Full Text search for Formula search.
- IBM Notes wrapper: Fix a conversion problem from integer to string ("1200.0")
- Other fixes and improvements

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

## Questions?

mario.sotil [at] gmail.com

### Tags

Java, JVM, ODM, NoSQL, Couchbase, Couchbase Lite, MongoDB, CouchDB, Neo4J, IBM Notes, Framework

