# river-framework

## What is the River Framework?

Hi, 

This is a Workflow Application Framework in an **EARLY development stage**. So far, the code written with this framework looks like this:

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

## Maven

To load the artifacts from Maven, you can add this dependency to your pom.xml file:

- To use it with the original lotus.domino package from IBM Notes

```xml
		<dependency>
			<groupId>org.riverframework</groupId>
			<artifactId>river-lotus-domino</artifactId>
			<version>0.2.7</version>
		</dependency>
```


## Features

So far, this framework has the following features: 

- Is designed to provide a unique interface to connect to NoSQL databases 
- Simplifies the writting of code 
- Controls five basic elements:
  - The session
  - The database
  - The document
  - The view or document index
  - The document iterator
- Supports indexes and unique id for documents


About the wrapper for IBM Notes:

- Supports the elements to work with a Notes Database
- Auto recycling the Notes objects
- Good to develop standalone Java or XPages programs 
- So far, it does not supports RichText items

  
## What I'm working on now?

- Version 0.2.8
  - Experimenting with the IBM Notes auto recycling
  - Finishing the wrapper and JUnit tests for a NoSQL in-memory database (Hazelcast)
  - Creating new Java examples
  
## What is in the ToDo list?

There are a lot of features that I will add to this framework:

- Support to relations between documents using graphs
- Connections to Mongo, CouchDB, Node4J, Hazelcast, etc. 
- Support tothe development workflow applications: states, responsibles, security, emails, etc.

## Why am I creating this Framework?

Over the last decade, I have been working as an IBM Notes workflow application Developer. But after developing similar applications over and over, I felt ready for a change and this is how I took the initiative to develop a Workflow Framework in LotusScript, the native IBM Notes programming language. The goal of the framework was to simplify the development, grouping the similar tasks together, and letting me focus on the differences between applications. It was really useful and fun to work on!

One year ago, considering that the LotusScript framework was fully designed by me, I got the idea to publish this work as an open source project to share it with other IBM Notes Developers that would find it useful too. In that purpose, and because I was not authorized to use the work I had done at my current job, I started to design and write a new one in Java in my spare time, at home. The design is new and will incorporate all the features that I would like to have. And given that this framework will be an improved version of the current one in LotusScript, this is for the best!

I expected that this work be useful for you. I'll be looking forward for your ideas, comments or questions at mario.sotil@gmail.com


## Next versions

- Version 0.3
  - Relation between documents (1..\*, \*..\*) using graphs
  - Examples in Java, SSJS, XPages and LotusScript
  
- Version 0.4
  - Connection to other NoSQL servers: MongoDB and CouchDB
  
- Next versions
  - Logging of changes in the fields of a document
  - Control of the data change propagation between related documents (1..\*, \*..\*)
  - Administration tools (ie. change the state of a document)
  - Connections to other NoSQL servers like CouchDB, etc.
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

So far, this framework has the following features: 

- Version 0.2.7
  - Auto recycling of IBM Notes objects. So far, it works good with a standalone Java program. I still making tests in XPages.
  - Temporarily removing the support to the org.openntf.domino library, considering that the auto recycling is working on the lotus.domino wrapper, and it's necessary to focus efforts in one wrapper at a time. 
  - Adding more functionality (create and removing databases, views, etc.)

- Version 0.2.6
  - As the logical separation was done, physically separate the current Framework in three JAR files: the Core, the lotus.domino wrapper and the org.openntf.domino wrapper.
  - Publish the JARs in a public Maven repository

- Version 0.2.5
  - ~~Rolling back from the `org.openntf.domino` package to `lotus.domino`~~
  - Improvements in the design, wrapping the `lotus.domino` and the `org.openntf.domino` classes with the `org.riverframework.wrapper`classes
  - The package `org.riverframework.core` will control the wrapped classes 
  - Improvements in the design, making that the wrappers classes be totally separated from the core. So, each wrapper can be loaded on runtime.
  - Improvements in DocumentCollection as array
  - Redesign the JUnit tests for testing the wrappers for `lotus.domino` and the `org.openntf.domino`, and testing the core with each wrapper
  - Improvements in the `org.riverframework.wrapper.lotus.domino` memory management. All the objects are recycled when the session is closed.
  - Update the Java example

- Version 0.2
  - Generation of unique identifiers for the documents
  - Opening documents by a unique id
  - Credentials file (~/.river-framework/credentials)
  - ~~Using the `org.openntf.domino` package instead `lotus.domino`~~
  - Support to local session using a installed IBM Notes client 
  - First example in Java

- Version 0.1
  - Using IBM Notes as NoSQL server
  - Lets you connect to an IBM Notes database and the possibility to access its views and create or modify documents.
  - Document collections as arrays
  - It has a set of `JUnit` tests


### Tags

Java, Server-side JavaScript, IBM Notes, Lotus Notes, XPages, LotusScript, Workflow




