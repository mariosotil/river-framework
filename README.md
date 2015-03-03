# river-framework

## What is the River Framework?

Hi, 

This is a Workflow Application Framework in a **VERY EARLY development stage**. This first version only has the following features: 

- Version 0.1
  - It is using IBM Notes as NoSQL server
  - It lets you connect to an IBM Notes database and the possibility to access its views and create or modify documents.
  - It has a set of JUnit tests

## What I'm working on now?

- Version 0.2
  - Generation of unique identifiers for the documents
  - Jar file 
  - First examples in Java, Javascript, and XPages

## What is in the ToDo list?

There are a lot of features that I will add to this framework:

- Version 0.3
  - Relation between documents (1..\*, \*..\*) using graphs
  - A new set of examples in Java, JavaScript, XPages and LotusScript
- Version 0.4
  - Use org.openntf.domino.\* libraries instead lotus.domino.\*
- Next versions
  - Logging of changes in the fields of a document
  - Control of the data change propagation between related documents (1..\*, \*..\*)
  - Document collections: array, list, iterator
  - Administration tools (ie. change the state of a document)
  - Connections to another NoSQL servers like CouchDB, MongoDB, etc.
  - Thread support
  - The main goal of the project: the support of workflow development with the features that are needed for the task:
    - Control of the states of the document
    - Control of the responsables on each state
    - Control of the permissions to read on each state
    - Control of the document security (reader, editor, etc.) on each state
    - Document protection if there is an error during a change of state
    - Notification deadlines for each state
    - Logging of state changes
    - Configuration of the notification mails sent on each state change
    - Implementation of a Script to configure a workflow
  

## Why am I creating this Framework?

Over the last years (many years!), I have been working as IBM Notes workflow application developer at my current job. And as so often happens, after some time I started to feel really bored developing similar applications once and again. So, as own-initiative, I started to develop a Workflow Framework in LotusScript, the native IBM Notes programming language. The goal of the framework was simplify the development grouping the similar tasks together, letting me focus on the differences between applications. It was really useful and fun to work on! 

Well, one year ago, considering that the LotusScript framework was designed completely by me, it seemed like a good idea to publish that work as an open source project and share it with another IBM Notes developers that maybe can find it useful too. I asked to my employer but, for contractual issues, I couldn't publish the source code written in working hours. It's understandable. So, since October 2014, I started to design and write a new one in Java *in my spare time, in my home*  (in case my boss is reading this :-). The design is new and will incorporate all the features that I would like to have, but I couldn't add because LotusScript language limitations. Considering that this framework will be really better than the current one in LotusScript, we all win, especially my boss :-) 

I expected that this work be useful for you. I'll be looking forward for your ideas, comments or questions at mario.sotil@gmail.com


### Tags

Java, JavaScript, IBM Notes, Lotus Notes, XPages, LotusScript, Workflow




