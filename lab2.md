# Lab 2 - Chatbot with static file Database

## Mission

* Manipulate two repositories using git.
* Fix the bug in DatabaseEngine.java.
* Add a new feature to your bot.
* Test your code locally using Eclipse STS.
* Deploy your code onto Heroku using command line git.

## Things to read prior to the lab

1. Offline-tutorial 1: Using Github
2. Offline-tutorial 2: Conversion from C++ to Java

## Description

In this lab you will taste how to develop a Java program on a server by bug fixing. We have purposely injected a bug into the `DatabaseEngine.java`. Follow the guideline below and fix the bug so that the bot will response a customed text to a pre-defined input. The input-response definition is stored in a static file `/sample-spring-boot-kitchensink/resources/static/database.txt`. 

During the debugging process, you will be using Eclipse STS to compile the code locally so that it is syntax-error free. After then you will deploy it on Heroku and try it with your line client.

Despite you may not have forked and cloned the project after completing Lab 1, it is required to do it as you are going to make changes. Besides, you are working on two remote repositories (git / heroku) at the same time.

## Registration

You need a github account.

## Installation

You are recommended to install the following software in your machine. They are also avaliable at the lab machine. For `Heroku CLI`, `Eclipse STS with Buildship 2.0` are stored at `L:\apps\comp3111`.

* [Java SDK 1.8 64-bits](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
* [Eclipse STS 3.8.4 or above](https://spring.io/tools/sts/all) -- On Windows the executable is located at `L:\apps\COMP3111\STS_64\sts-bundle\sts-3.8.4.RELEASE\STS.exe`
* [Gradle (STS) 3.8.x+1.0.x](https://marketplace.eclipse.org/content/gradle-ide-pack) -- You should install this in Eclipse STS by clicking `Help` -> `Eclipse Marketplace..` -> Type `Gradle IDE Pack` in the search box and install. You need to reboot your STS after install. 
* [git command line tools (Latest)](https://git-scm.com/book/en/v2/Getting-Started-Installing-Git) -- build in on macOS and most Linux Distribution
* [Heroku CLI](https://devcenter.heroku.com/articles/heroku-cli)

## Cloning the repository and Starts Eclipse STS

You need to fork the git project from our github webpage. Then you need to clone the project in your local repository. 
**Do not clone our project directly or you would not be able to save your work on github.** Open a terminal and type
```
git clone https://github.com/YOUR_GITHUB_ID/Line-chatbot-for-COMP3111 
```


Follow the steps below to open your project.

1. Launch `Eclipse STS`
2. Select a workspace which is preferable the parent directory of where your github project is located.
3. Click `File` from the menu -> `Open Projects from File System..` and a dialog titled `Import Projects from File System or Archive` will be prompted.
4. Click `Directory` and select your project folder cloned from github and click `Finish`.
5. In the `Package Explorer` panel or `Project Explorer` panel you shall see some projects, with errors. Right click and select `Configure` -> `Convert to Gradle (STS) Project`. The errors should go away. 
6. Right click the project in `Package Explorer` panal or `Project Explorer` panel and select `Gradle (STS)` -> `Task Quick Launcher`, type `build` and press enter. This will attempt to build and test your project locally.

> In case you cannot see Package Explorer or Project Explorer, you can find it from `View` of the menu.

### Contingency

In case you can't start the Eclipse STS or can't use it to compile the project, you can read and edit the java file using any editor (e.g. notepad). Then you can compile your code locally using the following command in the terminal.

```
gradlew build 
```

If you are follow the steps correctly, the build will be success but the test will be failed. The STS console should log:

```
....
:sample-spring-boot-kitchensink:testClasses
:sample-spring-boot-kitchensink:test

com.example.bot.spring.KitchenSinkTester > testFound FAILED
    java.lang.NullPointerException at KitchenSinkTester.java:75
2017-08-31 10:03:46.897  INFO 108388 --- [       Thread-4] o.s.w.c.s.GenericWebApplicationContext   : Closing org.springframework.web.context.support.GenericWebApplicationContext@1dc9f7de: startup date [Thu Aug 31 10:03:46 CST 2017]; root of context hierarchy

2 tests completed, 1 failed
:sample-spring-boot-kitchensink:test FAILED

FAILURE: Build failed with an exception.

* What went wrong:
Execution failed for task ':sample-spring-boot-kitchensink:test'.
> There were failing tests. See the report at: file:///c:/temp/git-line-bot/sample-spring-boot-kitchensink/build/reports/tests/test/index.html

* Try:
Run with --stacktrace option to get the stack trace. Run with --info or --debug option to get more log output.

BUILD FAILED
....
```

> You can open the file `sample-spring-boot-kitchensink/build/reports/tests/test/index.html` to start your debug task.

## Upload to your Heroku

Next you are deploying it to Heroku and testing it with your mobile phone. Heroku supports git. What you need to do is to add your Heroku repository as your *remote repository* and push the project to it. **Logically** it looks like below:

1. git add remote heroku HEROKU\_URL # add your heroku git to your remote repository
2. git commit -am "after fixing the bug" # commit
3. git push heroku master # push to heroku
4. Let heroku handle the rest

For some reasons it takes some extra effort to login to heroku with command line. Before you can upload or even communicate with heroku, do the following with your heroku CLI. At lab, you can use `L:\apps\comp3111\heroku\bin\heroku.exe` 
to replace the command heroku below

```
> heroku login
# it prompts for username and password
# ...

> heroku auth:token
850xxxxb-bxx3-4xx5-axx6-xxxxxxxxxx83
```

Next, when you are prompted with username and password again, type the word `blank` in the username and paste the token obtained from `heroku auth:token` in the password.

You need to associate your git folder to the Heroku project repository created in Lab 1. Assume your Heroku project website is `https://git.heroku.com/red-waters-31111.git`. In your git folder type
```
git remote add heroku https://git.heroku.com/red-waters-31111.git
```
This will add an remote repository to your git folder.

> Hint. To switch a folder in terminal, you type `cd your_path`. For example, you want to change to `c:\temp\mygitfolder`, you type `cd c:\temp\mygitfolder`. If you want to access your E drive for example, you should type `e:` in the terminal. For macOS and linux, it is very similar except the folder separate symbol is `/`.


You are doing debugging and testing at the moment, you don't want to corrupt the project. Thus, you should also create a branch `test` by
```
git checkout -b test
```

After you have fixed the Java bug and you want to test it on heroku, type
```
git commit -am "Test #1"    
git push -f heroku test:master # this means push your test branch on your local repository to heroku:master
```

Heroku will build the project automatically. You are advised to look at your app Log on Heroku web site. Test your bot using the LINE client.

When you are all done so that your local branch test contain a good copy, you need to merge it with the master
```
git checkout master
git merge test
git push -f origin master # push this to github
```


# TODO Tasks and Demo

1. You need to fix the Java bug as mentioned above.
2. Test your code locally that generates no error.
3. Follow the code in KitchenSinkTester.java create another test case.
4. Deploy your repository to heroku.

## For COMP3111H student

5. **Partial Match:** You should modify the program so that the text containing a keyword would be considered as a match. For instance, `Do you know what comes after abc?` should replies `kevinw says def`. In case a sentence contains more than one keywords, do what ever you want.
6. Create a test case for this feature.

After you have completed this task, raise your hand and demo it to your TA. Please understand that there are so many students in the room and we have limited manpower. Pick a seat closer to the screen to have an earlier demo.

Note: This is an individual task and you should perform this task on your own.


