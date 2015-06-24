# Das Beer Picker

This is a demo project to demonstrate an algorithm for picking the most diverse top-n result for a set of preferences. The algorithm that the project is based on/inspired by is found in this [research paper](https://www.idi.ntnu.no/~noervaag/papers/EDBT2015.pdf)

## History
This project was made for the DD2471 course at Royal Institute of Technology.

## How to run it

### Prerequisites

* Java version
* Maven
* Node
* Npm
* PostgreSQL

### Short info on work flow

The project is seperated into two parts, the web server(node.js) and the algorithm backend application(java). The web server lets the users enter their preferences. Theese gets saved into the database. Thereafter the java application can be run and generate the result, which is saved into the database. The final step is to view the reuslt on from the web server.

### Database

Both applications needs to access the same the database. Before you start, edit the database configuration in the following files:

* [DatabaseConnection.java](https://github.com/Wneh/moderndatabase/blob/master/src/utils/DatabaseConnection.java#L19-L22)
* [config.js](https://github.com/Wneh/moderndatabase/blob/master/web-server/config/config.js#L2)

### 1. Install dependencies

Install the dependencies with maven and npm before you start.

### 2. Start the java application

Start the [java application](https://github.com/Wneh/moderndatabase/blob/master/src/main/Main.java), you get the follwoing promt in your console:

`````
You made it, take control your database now!

Menu:
1: Initialize
2: Run
0: Exit

````

Type `1`to set up the databse tables and insert the needed data. Then let the program stay in idle and wait.

### 3. Start the web server

Start the [web server](https://github.com/Wneh/moderndatabase/blob/master/web-server/app.js) with the following command:

````
node app.js
````

You should get something like this promted in the console:

````
Example app listening at http://0.0.0.0:3000
````

Navigate to the address promted in to you in the console and add some votes.

### 4. Genereate the result







