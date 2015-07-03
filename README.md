# river-framework

## What is the River Framework?

This is an Application Framework for NoSQL databases in an **EARLY development stage**. To date (July 02, 2015) the Framework is capable to access Lotus Notes databases using the native Java libraries (`lotus.domino`) or the OpenNTF libraries (`org.openntf.domino`).

Right now, I'm improving the Lotus Notes wrapper and redesigning the Framework itself. The next steps are finishing the development of the Lotus Notes wrapper, creating graph relations, developing workflow supporting and wrappers for other NoSQL databases like MongoDB. 

Until the version 0.2.8, the code written with this framework looks like this:

```java
//Opening a session
Session session = River.getSession(River.LOTUS_DOMINO, "server", "username", "password");
Database database = session.getDatabase(DefaultDatabase.class, "server", "example.nsf");
    
// Creating some person
database.createDocument(Person.class)
  .generateId()
  .setField("Name", "John Doe")
  .setField("Age", 35)
  .save();
    
// Searching people with surname "Doe"				
DocumentIterator it = database.search("Doe");

for(Document doc: it)	{
  System.out.println("Found " + doc.getId());
  System.out.println("Name: " + doc.getFieldAsString("Name"));
  System.out.println("Age: " + doc.getFieldAsInteger("Age"));
}

session.close();
```

Considering that it's quite possible that I will forget to write updates, the current status of the project, source code, Maven configuration, demos, everything, will be in its GitHub repository ==> https://github.com/mariosotil/river-framework 

## Maven

To load the artifacts from Maven, you can add these dependencies to your pom.xml file:

- To use the original lotus.domino package from IBM Notes

```xml
		<dependency>
			<groupId>org.riverframework</groupId>
			<artifactId>river-lotus-domino</artifactId>
			<version>0.2.8</version>
		</dependency>
```

- To use the org.openntf.domino package from OpenNTF

```xml
    <dependency>
      <groupId>org.riverframework</groupId>
      <artifactId>river-org-openntf-domino</artifactId>
      <version>0.2.8</version>
    </dependency>
```

In both cases, you have to add to your classpath the Notes.jar library, and only for the last one, the org.openntf.domino.jar library.


## Features

So far, this framework has the following features: 

- Designed to provide a unique interface to connect to NoSQL databases 
- Simplifies the writting of code 
- Uses the Java Logging API
- Controls five basic elements:
  - The session
  - The database
  - The document
  - The view or document index
  - The document iterator
- Has two layers of control:
  - The wrapper layer, that connects directly to the database using its native classes. 
  - The core layer, that connects to the objects from the wrapper layer and provides a unique interface for be able to use any wrapper library written for this framework 
- Supports indexes and unique id for documents


About the wrapper for IBM Notes:

- Supports the elements to work with a Notes Database
- Auto recycling the Notes objects
- Good to develop standalone Java programs, Servlets or XPages programs 
- So far, it does not supports RichText items


About the wrapper for OpenNTF Domino:

- Takes advantage of this great library
- The auto recycling is managed by OpenNTF Domino
- Provides a good reference to measure the performance ot the IBM Notes wrapper

  
## What I'm working on now?

- Version 0.2.9
  - Redesigning in three layers:
    - The wrapper layer
    - The core layer
    - The extended layer, that allows the developer to write logic, rules, etc. for a single object. e.g. a document or a database
  - Moving the support for Ids and indexes to the extended layer
  - Writing a LoggerHelper class to make easier to control the `java.util.logging.Logger` objects
  - Optimizing the Lotus Notes wrapper library
  - Experimenting with the IBM Notes auto recycling
  - Experimenting with Lotus Notes objects caching
  - Temporarily removing the support to Hazelcast wrapper library
  - Updating the documention using Javadoc
  - Various fixes 

  
## What is in the ToDo list?

There are a lot of features that I will add to this framework:

- Support to relations between documents using graphs, 1..\* or \*..\*
- Support to workflow applications development
- Connections to Mongo, CouchDB, Node4J, Hazelcast, etc. 


## Why am I creating this Framework?

Over the last decade, I have been working as an IBM Notes workflow application Developer. But after developing similar applications over and over, I felt ready for a change and this is how I took the initiative to develop a Workflow Framework in LotusScript, the native IBM Notes programming language. The goal of the framework was to simplify the development, grouping the similar tasks together, and letting me focus on the differences between applications. It was really useful and fun to work on!

One year ago, considering that the LotusScript framework was fully designed by me, I got the idea to publish this work as an open source project to share it with other IBM Notes Developers that would find it useful too. In that purpose, and because I was not authorized to use the work I had done at my current job, I started to design and write a new one in Java in my spare time, at home. The design is new and will incorporate all the features that I would like to have. And given that this framework will be an improved version of the current one in LotusScript, this is for the best!

I expected that this work be useful for you. I'll be looking forward for your ideas, comments or questions at mario.sotil@gmail.com


## Next features

Version 0.2.10
- Creating Java and Lotus Notes examples

Version 0.3
- Relation between documents (1..\*, \*..\*) using graphs
- Examples in Java, SSJS, XPages and LotusScript
  
Version 0.4
- Connection to other NoSQL servers: MongoDB and CouchDB
  
Next versions
- Logging of changes in the fields of a document
- Control of the data change propagation between related documents (1..\*, \*..\*)
- Administration tools (ie. change the state of a document)
- Connections to other NoSQL servers like CouchDB, Hazelcast, etc.
- Thread support
- The support of workflow development with the features that are needed for the task:
  - Control of the states of the document
  - Control of the responsibles on each state
  - Control of the permissions to read on each state
  - Control of the document security (reader, editor, etc.) on each state
  - Document protection if there is an error during a change of state
  - Notification deadlines for each state
  - Logging of state changes
  - Configuration of the notification mails sent on each state change
  - Implementation of a Script to configure a workflow


## Version change log

Version 0.2.8
- Fixes and improvements
- Adding again the support to the org.openntf.domino library

Version 0.2.7
- Auto recycling of IBM Notes objects. So far, it works good with a standalone Java program. I still making tests in XPages.
- Temporarily removing the support to the org.openntf.domino library, considering that the auto recycling is working on the lotus.domino wrapper, and it's necessary to focus efforts in one wrapper at a time. 
- Adding more functionality (create and removing databases, views, etc.)

Version 0.2.6
- As the logical separation was done, physically separate the current Framework in three JAR files: the Core, the lotus.domino wrapper and the org.openntf.domino wrapper.
- Publish the JARs in a public Maven repository

Version 0.2.5
- ~~Rolling back from the `org.openntf.domino` package to `lotus.domino`~~
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
- ~~Using the `org.openntf.domino` package instead `lotus.domino`~~
- Support to local session using a installed IBM Notes client 
- First example in Java

Version 0.1
- Using IBM Notes as NoSQL server
- Lets you connect to an IBM Notes database and the possibility to access its views and create or modify documents.
- Document collections as arrays
- It has a set of `JUnit` tests


### Tags

Java, Server-side JavaScript, IBM Notes, Lotus Notes, XPages, LotusScript, Workflow




