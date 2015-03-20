# river-framework

## What is the River Framework?

Hi, 

This is a Workflow Application Framework in a **VERY EARLY development stage**. This version only has the following features: 

- Version 0.1
  - It is using IBM Notes as NoSQL server
  - It lets you connect to an IBM Notes database and the possibility to access its views and create or modify documents.
  - Document collections: array, list, iterator
  - It has a set of JUnit tests

- Version 0.2
  - Generation of unique identifiers for the documents
  - Opening documents by a unique id
  - Use org.openntf.domino.\* libraries instead lotus.domino.\*

## What I'm working on now?

- Version 0.2
  - First examples in Java, SSJS, and XPages

## What is in the ToDo list?

There are a lot of features that I will add to this framework:

- Version 0.3
  - Relation between documents (1..\*, \*..\*) using graphs
  - Creation of a IBM Notes objects pool
  - A new set of examples in Java, SSJSJ, XPages and LotusScript
- Version 0.4
  - Connections to other NoSQL server (to de defined in the next weeks)
- Next versions
  - Logging of changes in the fields of a document
  - Control of the data change propagation between related documents (1..\*, \*..\*)
  - Administration tools (ie. change the state of a document)
  - Connections to other NoSQL servers like CouchDB, MongoDB, etc.
  - Thread support
  - The main goal of the project: the support of workflow development with the features that are needed for the task:
    - Control of the states of the document
    - Control of the responsibles on each state
    - Control of the permissions to read on each state
    - Control of the document security (reader, editor, etc.) on each state
    - Document protection if there is an error during a change of state
    - Notification deadlines for each state
    - Logging of state changes
    - Configuration of the notification mails sent on each state change
    - Implementation of a Script to configure a workflow
  

## Why am I creating this Framework?

Over the last decade, I have been working as an IBM Notes workflow application Developer. But after developing similar applications over and over, I felt ready for a change and this is how I took the initiative to develop a Workflow Framework in LotusScript, the native IBM Notes programming language. The goal of the framework was to simplify the development, grouping the similar tasks together, and letting me focus on the differences between applications. It was really useful and fun to work on!

One year ago, considering that the LotusScript framework was fully designed by me, I got the idea to publish this work as an open source project to share it with other IBM Notes Developers that would find it useful too. In that purpose, and because I was not authorized to use the work I had done at my current job, I started to design and write a new one in Java in my spare time, at home. The design is new and will incorporate all the features that I would like to have. And given that this framework will be an improved version of the current one in LotusScript, this is for the best!

I expected that this work be useful for you. I'll be looking forward for your ideas, comments or questions at mario.sotil@gmail.com


### Tags

Java, JavaScript, IBM Notes, Lotus Notes, XPages, LotusScript, Workflow




