# Report for assignment 3

This is a template for your report. You are free to modify it as needed.
It is not required to use markdown for your report either, but the report
has to be delivered in a standard, cross-platform format.

## Project

Name: guava  

URL: https://github.com/iriediese/guava  

Java set of core libraries, including some new collection types and utlities.  

## Onboarding experience

Did it build and run as documented?  
The project we originally picked was json-iteraotr/java: https://github.com/json-iterator/java  
It did build, but a couple of tests needed adjustments.  
Specifically, there were issues related to some array objects that needed to be fixed.  
The reason we had to switch to a different project was the overwhelming amount of java reflection.  
The entire json parser and api even injected code and changed its flow during runtime, at certain points.  
There were some unit tests, but they were mostly superficial and did not address the high-complexity functions.  
Upon reaching the point where we needed to manually measure coverage for these functions, we decided to find another project to work on.  

Then, we found google/guava, which offers a bit more variety.  
The code base is much larger, so there are more (high-complexity) functions to choose from.  
The code builds and runs, and the tests pass as well.  
The documentation suggests importing it as a maven / gradle dependency,  
but we compiled and tested it as it was.  
We then measured its coverage using jacoco, and results showed 89% (class), 88% (method), 88% (line), and 86% (branch).
    
## Complexity

1. What are your results for five complex functions?  
   * Did all methods (tools vs. manual count) get the same result?
   * With the preceding number specifying the cyclomatic complexity, the 10 high-complexity functions we have found are:  
      21    Utf8::isWellFormedSlowPath@137-194@./base/Utf8.java  
      21    AbstractFuture<V>::get@410-509@./util/concurrent/AbstractFuture.java  
      23    InetAddresses::textToNumericFormatV6@232-300@./net/InetAddresses.java  
      17    GeneralRange<T>::intersect@175-220@./collect/GeneralRange.java  
      16    TreeMultiset<E>::AvlNode<E>::setCount@716-762@./TreeMultiset.java  
      16    Murmur3_128HashFunction::Murmur3_128Hasher::processRemaining@124-166@./hash/Murmur3_128HashFunction.java  
      11    TreeMultiset<E>::AvlNode<E>::remove@665-714@./TreeMultiset.java  
      12    RegularImmutableSortedSet<E>::containsAll@104-153@./RegularImmutableSortedSet.java  
      11    RegularImmutableSortedSet<E>::equals@170-204@./RegularImmutableSortedSet.java  
      12    Multisets::union@397-454@./Multisets.java  
   * Then, we calculated the coverage for the following 5:  
      21    Utf8::isWellFormedSlowPath@137-194@./base/Utf8.java  
      23    InetAddresses::textToNumericFormatV6@232-300@./net/InetAddresses.java  
      17    GeneralRange<T>::intersect@175-220@./collect/GeneralRange.java  
      16    TreeMultiset<E>::AvlNode<E>::setCount@716-762@./TreeMultiset.java  
      11    TreeMultiset<E>::AvlNode<E>::remove@665-714@./TreeMultiset.java  
   * We measured complexity using lizard, MetricsReloaded, and checkstyle, as well as manual counting. They each yielded different (yet very similar / off by 1-2) results.  
   * Are the results clear?  
   * They mostly seem accurate, yes. Even when results differ, the values are still close enough, and they also mostly followed our exepectations (when doing a manual count).  
2. Are the functions just complex, or also long?  
   * Most of the functions that we have found to be complex, were also long (around 70 LOC). However, there were a few exceptions (roughly a fifth of those we found) that were shorter (around 40 LOC).  
3. What is the purpose of the functions?  
   * Utf8::isWellFormedSlowPath checks whether a byte array slice is a well-formed UTF-8 byte sequence.  
   * textToNumericFormatV6 gets a string with an IPv6 address, and then translates it to a numeric format.  
   * GeneralRange<T>::intersect returns the intersection of the two ranges, or an empty range if their intersection is empty.  
   * TreeMultiset<E>::AvlNode<E>::setCount sets the count of an element to the given value.   
   * TreeMultiset<E>::AvlNode<E>::remove removes an AvlNode from a TreeMultiset, and returns it.  
4. Are exceptions taken into account in the given measurements?  
   * Yes, they are. Every time an exception is thrown, the test that triggered it only covered the code up until and including the exception part. The results from all test cases are then aggregated so that they show everything that was covered.  
5. Is the documentation clear w.r.t. all the possible outcomes?  
   * The provided tests do not actually always test the function we measure the coverage for. Instead, that function is called by a different function that is under test. As such, the initial tests mostly lack any information about the measured functions. On the other hand, the tests we wrote as part of the assignment do have clear documentation regarding the possible outcomes.  

## Refactoring

Plan for refactoring complex code:  
For Utf8::isWellFormedSlowPath, there is a very obvious while(true) loop that can be avoided. Additionally, some bitwise operations included within the function can be extracted into separate methods, which can then be called as part of the main body, to reduce code length and make everything more clear.  
In textToNumericFormatV6, the procedure counting the delimiters can be split into separate methods, then called from the main one. This would reduce the cyclomatic complexity of the function itself, but would not have any other impact overall. There are no ways to improve the logic of the function that we can see.  
In GeneralRange<T>::intersect, the checks for the lower and upper bounds can also be separated into different functions.  
Regarding TreeMultiset<E>::AvlNode<E>::setCount, it can be made iterative. This might make it a bit faster, and potentially clearer.  
Finally, TreeMultiset<E>::AvlNode<E>::remove cannot really be changed much. Some if statements could be combined to reduce the code length, but that would not improve much.  

Estimated impact of refactoring (lower CC, but other drawbacks?).  
Besides lower CC, drawbacks include the possiblity that refactoring might increase coupling and complexity in other parts of the program. Additionally, refactoring takes time that is not worth spending just to optimise the project a little bit. Optimisation can also be excessive and break functionality in some cases.  

Carried out refactoring (optional, P+):  
In practice, the two functions we refactored were InetAddresses::textToNumericFormatV6 from ./net/InetAddresses.java, and ToDoubleRounder::roundToDouble from ./math/ToDoubleRounder.java. We reduced the complexity for the former from 23 to 10, and from 34 to 2 for the latter. 
They can both be found in the refactor branch.

## Coverage

### Tools

Document your experience in using a "new"/different coverage tool.  
We also used jacoco to measure coverage.

How well was the tool documented? Was it possible/easy/difficult to
integrate it with your build environment?  
The tool was heavily documented, but we found many conflicting sources of information. We integrated the tool with maven, which led to some unpredictable issues related to how the pom.xml files were written by upstream (variable conflicts in creating logs). We figured it out after a while, and managed to measure the coverage for the whole project. The coverage results showed 89% (class), 88% (method), 88% (line), and 86% (branch), in the beginning.

### Your own coverage tool

Show a patch (or link to a branch) that shows the instrumented code to
gather coverage measurements.
This can be found in the develop branch: https://github.com/iriediese/guava/commits/develop   
Together with https://github.com/iriediese/guava/commit/0787d8aec84314a2b45f277d883f4bc857d5c970 the last four pull requests show the usage of our measuring tool

What kinds of constructs does your tool support, and how accurate is
its output?  
Our tool uses arrays to measure the covered branches, as explained below.

### Evaluation

1. How detailed is your coverage measurement?
For every branch, we set a flag to true if the branch is crossed, and false otherwise. Our tool makes an array out of all flags, and by looking at the array, one can tell which branches were covered, and which branches were not.
The percentage can then be seen from the array itself.

2. What are the limitations of your own tool?
Our tool uses files to record intermediary coverage measurements, during the runtime of a test suite. In other words, assume test 1 is run. We record the coverage of test 1 in a file. Then, when test 2 is run, we merge its results into that same file, and so on. If tests are very long, this process might become a bit slow at times, but in our case it was quick enough. 

3. Are the results of your tool consistent with existing coverage tools?
Fortunately, the functions we measured the coverage for all had 100% coverage, according to our tool. This makes sense, since there is a very high number of tests (hundreds) that call each one as part of other tested functions. In the end, from around 20 nodes a method had, on average, they were all covered. This is, indeed, consistent with our results from jacoco.

## Coverage improvement

Theodor:  
Functions: equals (0% to 92%), contains (0% to 100%), loadFinalizer (0% to 33%), newArrayBlockingQueue(0% to 100%).  
Branch: https://github.com/iriediese/guava/commits/tedi_test  

Ioana:  
Functions: count (0% to 100%), toString (0% to 100%), toLowerCase(0% to 100%), toUpperCase (0% to 100%), newLinkedBlockingQueue(0% to 100%)  
Branch: https://github.com/iriediese/guava/commits/ioana_test  

Alex:  
Functions: DiscreteDomain.LongDomain::distance (0% to 75%), minValue (0% to 100%), maxValue(0% to 100%), next (0% to 100%), previous (0% to 100%)  
Branch: https://github.com/iriediese/guava/commits/alex-additional-tests  

Johan:  
Functions: createTable (43% to 100%), tableClear (41% to 100%), tableSet (41% to 100%), tableGet (39% to 100%)  
Branch: https://github.com/iriediese/guava/commits/CompactHashing_tests  

Joaquin:   
Functions: reset (0% to 100%), sumThenReset (0% to 35%), size (0% to 100%), isEmpty (0% to 100%)   
Branch: https://github.com/iriediese/guava/commits/jbq_tast2  


## P+ criteria:

We adhere to criteria 1, 2, and 3 as specified in the assignment description.  
Specifically, each group member wrote 4 new tests that improve coverage.  
We used issues and systematic commit messages to manage our project.  
We have refactored two functions, reducing their complexity by at least 35%.   

## Self-assessment: Way of working

We used 2-day sprints to get functionality split, then done individually. We did this for each different part of the project. For example, the coverage tool was written in parallel by each one of us on their chosen function, and the code was peer-reviewed and merged afterwards, during a meeting. The tests were written the same way. Finally, refactoring and documentation were done over a longer time span, both individually (refactoring by Alex and Ioana, report by Theodor), and together during meetings. Overall, contribution was roughly equal, and each team member was pro-active, eager to help wherever needed. This was sometimes the case when writing tests.

Current state according to the Essence standard:
We constantly used checklists to discern what was done, in progress, or left to do
We held meetings to split tasks, gather results, and provide help wherever necessary.
The tasks were small, simple, and designed to achieve the highest possible impact.
To the largest possible extent, we have tried to make use of the 7 alphas, as specified in the standard.
For example, we fulfiled our requirements using a previously-discussed standard, so that our individual solutions worked similarly.

Was the self-assessment unanimous? Any doubts about certain items?
Yes. No doubts.

How have you improved so far?
We have gotten quicker at using git, and have also got more familiar with maven and its related unpleasantries.

Where is the potential for improvement?
Picking a project that is both good enough, and yet offers room for improvement is quite difficult.
Furthermore, anyone would benefit from getting better at coding, regardless of their experience. 

## Overall experience

What are your main take-aways from this project? What did you learn?
We have learnt that projects can be very well done, sometimes allowing for little room for improvement.

Is there something special you want to mention here?
No.
