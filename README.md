<img src="http://www.riverframework.org/images/river-header.png" /><br/>
[![Project Stats](https://www.openhub.net/p/river-framework/widgets/project_thin_badge.gif)](https://www.openhub.net/p/river-framework)

## A single interface for different NoSQL databases

It is an Object-Document Mapper Framework for NoSQL databases at **development stage**. Here you will find about the development itself: current and next features, known issues, dependencies, binaries, Maven artifacts, demos, and, of course, the source code :-)  You will find a presentation about the project at its [website](http://www.riverframework.org)

## Working on version 0.3

This library is being redesigned in the version 0.3. The original plan of support 
Couchbase, MongoDB, CouchDB, and Neo4J is still standing; but support to IBM Notes will be
stopped, at least at the beginning. I was so in love with IBM Notes, 
that the design was completely focused on it, resolving Notes' particular situations. 
I lose sight of the other NoSQL databases. 

To date (2018-04-15), the version 0.3 is still a Work In Progress. You can follow it in the branch 
[version-0.3.x](https://github.com/mariosotil/river-framework/tree/version-0.3.x). 

The current version 0.2.11's source code is still here, and its binaries are in [Maven Central Repository](https://search.maven.org/#artifactdetails%7Corg.riverframework%7Criver-lotus-domino%7C0.2.11%7Cjar)
 and [OpenNTF](http://www.openntf.org/main.nsf/project.xsp?r=project/River%20Framework/releases/). 
They could still be used to access IBM Notes.

## How does the code look?

Query a IBM Notes database with River Framework looks like this.

```java
//Opening a session
Session session = River.getSession(River.LOTUS_DOMINO, "server", "username", "password");
Database database = session.getDatabase(AddressBook.class, "server", "example.nsf");

// Creating a new person document
database.createDocument(Person.class)
  .setField("Name", "John Doe")
  .setField("Age", 35)
  .generateId()
  .save();

// Searching people with name "John Doe"              
DocumentIterator it = database.search("Name=\"John Doe\"");

// Iterating
for(Document doc: it)   {
  System.out.println("Found " + doc.getId());
  System.out.println("Name: " + doc.getFieldAsString("Name"));
  System.out.println("Age: " + doc.getFieldAsInteger("Age"));
}

session.close();
```

## Features in version 0.2.11

- Compiled for Java 1.6+ 
- Support IBM Notes
- Java Logging API
- Controls six basic elements:
  - Session
  - Database
  - Document
  - Views or collections
  - Document iterator
  - Document indexes and unique IDs
- Has two layers of control:
  - The wrapper layer, that connects directly to the database using its native classes. 
  - The core layer, that provides a unique interface to be able to use any wrapper library written for this framework. 


### About the wrapper for IBM Notes:

- It's **stable**
- Supports only five basic elements to work with a Notes database (session, database, document, view, iterator)
- Anyway, you can still use the native Java library to do things like modify the ACL or work with RichText items.
- Supports local and remote sessions (DIIOP) 
- Auto recycling of Notes objects
- Supports field size > 32K
- Has an object cache
- To reduce impact on replication, if you set a field with a new value, it checks if it is different before change it.
- Good to develop standalone Java programs, Servlets or XPages programs 


### About the wrapper for OpenNTF Domino:

- It's **stable**
- Takes advantage of this great library developed by [OpenNTF](http://www.openntf.org/main.nsf/project.xsp?r=project/OpenNTF%20Domino%20API)
- The auto recycling is managed by OpenNTF Domino API
- Supports field size > 32K
- Good to develop standalone Java programs, Servlets or XPages programs 


## Getting Started

Please, follow the instructions from this [link](https://github.com/mariosotil/river-framework-documentation/blob/master/getting-started.md).


## Demo

Demos as Maven projects for Eclipse, of stand-alone Java programs are in this GitHub [repo](https://github.com/mariosotil/river-framework-demo).


## Download

You can download the binaries as Jar libraries from the [OpenNTF website](http://www.openntf.org/main.nsf/project.xsp?r=project/River%20Framework/releases/). 


## Maven

To load the artifacts from Maven, you can add one of these dependencies to your pom.xml file:

- To use the original lotus.domino package from IBM Notes

```xml
    <dependency>
        <groupId>org.riverframework</groupId>
        <artifactId>river-lotus-domino</artifactId>
        <version>0.2.11</version>
    </dependency>
```

- To use the org.openntf.domino package from OpenNTF

```xml
    <dependency>
        <groupId>org.riverframework</groupId>
        <artifactId>river-org-openntf-domino</artifactId>
        <version>0.2.11</version>
    </dependency>
```

In both cases, you have to add to your classpath the `Notes.jar` library. For the last one, you will need to add the `org.openntf.domino.jar` library too.

## Issues

If you need to report an issue, please use the GitHub [issues](https://github.com/mariosotil/river-framework/issues) page.

## Questions?

mario.sotil [at] gmail.com

### Tags

Java, ODM, NoSQL, Couchbase, MongoDB, IBM Notes, Neo4J, Lotus Notes, SSJS, ORM, Framework

