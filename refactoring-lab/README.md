# Lab 6: Refactoring

The aim of this lab is to get some hands-on experience with refactoring.

Refactoring is the process of changing the internal structure of our code to
make it more comprehensible without changing its external behavior. We do this
because we're not the sole consumers of the code we write: we owe it to our
colleagues (our teammates in this case) and our future selves to make our code
easy to change and understand!

## A simple transcript printing program

For this lab, we're going to be dealing with a simple transcript printing
program. The Java file containing all the classes can be found
[here](RefactoringLab.java). It should run using your standard fare Java IDEs
or if you compile and run the file.

The `generateTranscriptForWidth` is responsible for printing transcripts that
look like this:

```
                    John Chan
                     21039408
                      2017F

COMP3111 Software Engineering 4.00 4
COMP3311 Database Management Systems 3.30 3

Semester GPA: 3.70
```

Then again, we might have specific column limits for our printing, so we can
specify these in our program as well. For example, assuming a column limit 20:

```
     John Chan
      21039408
       2017F

COMP3111
    Software Engineering
    4.00 4
COMP3311
    Database Management Systems
    3.30 3

Semester GPA: 3.70
```

Notice that the lines now "wrap" because the class names don't fit on a single
line with their codes.

Of course, before we print the transcripts, we might want to perform a
"dry run" to figure out how large our transcript will turn out. To enable this,
we also implement a `transcriptHeightForWidth` method that determines the
number of lines for a transcript.

## Your task

We're going to try poke and prod at the transcript program, see where the
design falters, and make **incremental** changes to make the program better.
Some patterns can be found at
Martin Fowler's [excellent refactoring page](https://refactoring.com/catalog/),
but I would suggest first trying to identify *why* the code isn't so good and
suggesting simple ways to fix these problems. For example, pulling out
literals as member variables or blocks of code as methods can be excellent
ways to improve your code.

**Do not change the API. The goal of refactoring is to make changes to the
_internal_ structure without affecting the _external_ behavior. So, your clients
should be able to call `transcriptHeightForWidth` with a width and have a height
returned to them; or call `generateTranscriptForWidth` with a width and have
the transcript be printed out.**

### Task 1: Use dashes ("-") instead of spaces (" ") to center-align the header

```
-----John Chan------
------21039408------
-------2017F--------

COMP3111
    Software Engineering
    4.00 4
COMP3311
    Database Management Systems
    3.30 3

Semester GPA: 3.70
```

*   Was anything lacking in the code? Did you find yourself repeating
yourself? Was the code easy to maintain? (These problems are often
referred to as "code smells").
*   Propose and implement a fix to the problem. Why is it better?

### Task 2: Use 2 spaces for indentations, instead of 4.

```
-----John Chan------
------21039408------
-------2017F--------

COMP3111
  Software Engineering
  4.00 4
COMP3311
  Database Management Systems
  3.30 3

Semester GPA: 3.70
```

*   Did you remember to also update the `transcriptHeightForWidth` method?
*   What code smells did you encounter here?
*   Propose and implement a fix to the problem. Why is it better?

### Task 3: Reduce code duplication between the `transcriptHeightForWidth` and `generateTranscriptforWidth` methods

As one might expect, the `transcriptHeightForWidth` and
`generateTranscriptForWidth` classes are structurally similar - after all,
they're describing the same transcript structure.

*   Propose and implement some changes that improve code reuse between the two
    methods. What are some advantages and disadvantages to your approach?
*   Bonus: are there any design patterns that *make sense* with respect to
consolidating these two methods?

## Advice

*   When defining helper method, consider defining them as `private` rather
    than `public` to avoid polluting the public interface.

    ```java
    class SomeExampleClass {
      // DANGER: Now, clients of your class can invoke your helper method even
      // if it's performing some complicated, intermediate step!
      public void someHelperMethod() { ... }

      // BETTER: Instead, it's safer to keep these helpers private so that they
      // can only be called within the class.
      private void someHelperMethod() { ... }
    }
    ```

*   When a helper method doesn't refer to any member variables, it may be worth
    making them `static` methods. This helps to clarify that the helper method
    does not affect any of the class' state.

    ```java
    class SomeExampleClass {
      // AMBIGUOUS: Not entirely clear to a user if the method will modify the
      // internal state of the class.
      private void addValues(int a, int b) { return a + b; }

      // BETTER: Now, it's clear that none of the member variables will be
      // touched because the method is static.
      private static void addValues(int a, int b) { return a + b; }
    }
    ```

*   When pulling out constants, consider declaring them `final` to stress that
    the value will never be changed. Changing their name to be all-caps will
    also help distinguish these from other fields.

    ```java
    class SomeExampleClass {
      // GOOD: All-caps clarifies that the field is a constant.
      public static final double PI = 3.14159;
    }
    ```
