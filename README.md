<img src="http://www.riverframework.org/images/river-header.png" /><br/>
[![Project Stats](https://www.openhub.net/p/river-framework/widgets/project_thin_badge.gif)](https://www.openhub.net/p/river-framework)

## A single interface for different NoSQL databases

It is an Object-Document Mapper Framework for NoSQL databases at **development stage**. Here you will find about the development itself: current and next features, known issues, dependencies, binaries, Maven artifacts, demos, and, of course, the source code :-)  You will find a presentation about the project at its [website](http://www.riverframework.org)

## Working on version 0.3

This library is being redesigned in the version 0.3. The original plan of support Couchbase, MongoDB, 
Neo4J, etc. is still standing; but the library will be compiled for Java 8.  I was so in love with IBM Notes, 
that the design was completely focused on it, so I lose sight of the other databases. That's why this new version 
will not support IBM Notes, at least at the beginning.

To date (2018-04-15), the version 0.3 is still a Work In Progress. You can follow it in the branch 
[version-0.3.x](https://github.com/mariosotil/river-framework/tree/version-0.3.x)

## Features in version 0.2.11

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
- Has an object cache
- To reduce impact on replication, if you set a field with a new value, it checks if it is different before change it.


### About the wrapper for IBM Notes:

- It's **stable**
- Supports only five basic elements to work with a Notes database (session, database, document, view, iterator)
- Anyway, you can still use the native Java library to do things like modify the ACL or work with RichText items.
- Supports local and remote sessions (DIIOP) 
- Auto recycling of Notes objects
- Supports field size > 32K
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

