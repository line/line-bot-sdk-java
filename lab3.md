# Lab 3 - Chatbot with SQL Database

## Mission

* Create a database on heroku.
* Access the database locally.
* Access the database on heroku.
* Create a unit test for your database Engine.
* Make your bot connected to the Database.


## Things to read prior to the lab

1. Offline-tutorial 1: Using Github
2. Offline-tutorial 2: Conversion from C++ to Java
3. Offline-tutorial 3: A Brief Introduction to DBMS and PostgreSQL

## Description

In this rather inefficient and insecure to use a static file to maintain your keywords and responses. A better way to do it is to employ a database system. Before going to this lab, you need to read the offline tutorial about database and install suitable software on your machine.

In this lab you are going to add a PostgreSQL database to your chatbot. The database is also hosted on heroku (technically it is hosted somewhere else but you don't need to worry about it). You will be using command line tools to create a database table and insert data. You should create a class inherit `DatabaseEngine` that connects to the PostgreSQL database and do the searching. 

## Installation

You are recommended to install the following software in your machine. This is available at lab `L:\apps\comp3111\pgsql\bin`.

* [PostgreSQL Client 9.5.7 or above](https://www.postgresql.org/download/)

> Note: we are not running PostgreSQL server in our machine but you need a command line client to access the database. You may try some third party psql clients but we have not tested it.

> To test if your installation is complete, type `psql` in terminal.


## Creating a database on heroku



Follow the steps below to create a PostgreSQL on heroku.

1. Go to [heroku dashboard](https://dashboard.heroku.com/) and select your app. 
2. Click Resource and search for `Heroku Postgres`.

![Create DB](docs/img/lab3/addDB.png)

3. Click to add a `Heroku Postgres` data resource. Please be reminded to select a free plan.
4. Try to locate your newly added data resource on the webpage and select `View Credential`.

![Credential](docs/img/lab3/viewCredential.png)

5. You should find the details of your credential


![Credential](docs/img/lab3/credential.png)

You will need your `URI` and `Heroku CLI` later.

After these steps, you have already created a Database and registered a user and password already. Referring to the offline tutorial, you have completed Section 3 already.





## Operating the database in command line mode.

### For laptop users

Open your terminal and paste the `Heroku CLI` you have copied previously. This will command the Heroku to connect to the database. You need to install the `Heroku CLI` to do it.

```
# Replace the xxxxx and yyyyy with your Heroku CLI
c:\myProjectFolder\>heroku pg:psql postgresql-xxxxxxx-xxxxx --app yyyyyy-yyyyyy-yyyyyy 
```

> :exclamation: Contingency: in case the Heroku CLI does not work, you can use the URI to connect directly with your psql.
> ``` psql YOUR_URI  ```


### For people who are using lab machines

Our lab has blocked the port access so you would not be able to connect to the database from your desktop. We recommend the following.

1. Use the `Edge browser` to login to your Heroku dashboard.
2. Click to your app (not the database)
3. Click `More` button at your top-right-corner
4. Click `Run Console`
5. type `bash` next to the box `heroku run` and type run.
6. type `psql YOUR_URI` 

You shall be able to login to your database like
```
~ $ psql postgres://wzmmmrtguvgtwa:53c08d0ea19aa736008251c20516b6615421428e8@ec2-54-163-252-55.compute-1.amazonaws.com:5432/dba0cfojvl                                                                               
psql (9.6.4, server 9.6.1)    
SSL connection (protocol: TLSv1.2, cipher: ECDHE-RSA-AES256-GCM-SHA384, bits: 256, compression: off) 
Type "help" for help.

dba0cfojvl=>
```


### TODO
1. Create a Table in the database that contains two columns `keyword` and `response`.
2. Insert 5 pairs of keywords and response into the table. Later when the user enter a keyword in LINE the bot should reply the corresponding response.

Referring the instruction in the `offline tutorial - database Section 4` to do the task using command line SQL.

or

Referring the instruction in the `Tutorial Slide T3.pdf` to do it with ORM.


## Working with your BOT

It is very similar to Section 5 of the offline tutorial that you need to create a Connection, a PreparedStatement, execute the query, and obtain the result. The file `SQLDatabaseEngine.java` is a skeleton code that contains the private method `Connection getConnection()`. With that you can construct your `public String search(String)` method. You need to refer to its parent class `DatabaseEngine` about how to return value and throw exception in `search(String)`.

> Don't forget to change the constructor of `KitchenSinkController` so that `SQLDatabaseEngine` will replace `DatabaseEngine`.

## Unit Test

Of course you need to perform Unit Test on your SQLDatabaseEngine. Edit the file `KitchenSinkTester.java` and replace the following line as below. It is to inform the Tester to test SQLDatabaseEngine instead.
```
//@SpringBootTest(classes = { KitchenSinkTester.class, DatabaseEngine.class })
@SpringBootTest(classes = { KitchenSinkTester.class, SQLDatabaseEngine.class })
```

When you deploy the code on heroku, the `URI` can be obtained from the environment variable `DATABASE_URL`. When you perform local test you need to specify the `URI` in your environment variable in your gradle configuration file. Gradle is a dependence management software like makefile, but it is more advanced. Open `sample-spring-boot-kitchensink/build.gradle` in your Eclipse STS. you should see something like

```
apply plugin: 'org.springframework.boot'

dependencies {
    compile project(':line-bot-spring-boot')
    compile group: 'postgresql', name: 'postgresql', version: '9.0-801.jdbc4'
}

test {
    environment "DATABASE_URL", "PUT_YOUR_URI_HERE" 
}
``` 

The first line says this project is a spring boot project where our project is building on this framework. The dependencies block specifies that the line-bot-spring-boot project needs to be built in order to build this KitchenSink. Also it requires the package `postgresql` with a specific version. During the compliation gradle will download the postgresql package from a public repository. The test block states that when the task `test` is execute, it sets the environment variable "DATABASE_URL" as your URI. You need to paste the URI you copied from web earlier to here. 

> # Lab machine Users
> Our lab blocks the port 5432 so you would not be able to connect to the remote database. Please try this part at home.

# TODO Tasks and Demo

1. Create a database on heroku and insert data into it.
2. Complete the SQLDatabaseEngine so that it responses like Lab 2 except the result is stated in the database. (For COMP3111H student partial match should be implemented.)
3. Test SQLDatabaseEngine locally and pass all test cases.
4. Push it to Heroku.

## For COMP3111H student

5. Store the number of hits of keyword into the Database. Append this number in your response. You may assume there is only one keyword per message.

## DEMO

1. Show us your phone with an input that matches with the database
~~2. Show us the build report that you have passed your test locally.~~


After you have completed this task, raise your hand and demo it to your TA. Please understand that there are so many students in the room and we have limited manpower. Pick a seat closer to the screen to have an earlier demo.

Note: This is an individual task and you should perform this task on your own.


