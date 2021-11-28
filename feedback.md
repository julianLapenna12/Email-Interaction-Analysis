# mp2 Feedback

## Grade: A

| Compilation | Timeout | Duration |
|:-----------:|:-------:|:--------:|
|YesYesYesYes|NoNoNoNo|186.84|

## Comments
Some minor style issues (why was countNodes functionality compressed into one line?).  Comments are OK (but still too few) overall, but excessive in some areas (we don't need full specs for every constant), and lacking in other areas (entire methods like BFS have no comments to guide reader).  RI is a good start but not comprehensive, leaving out several key concrete variables and not a deep enough analysis of acceptable values.  Please read more on AFs as the ones written show a general misunderstanding of what an AF actually is/does.  Specs are very good for the most part - just try to be a bit more explicit with domains/ranges (such as returning -1 for NthMostActiveUser) and better overall descriptions of the method's functionality.  EmailUser class has many issues but and wasn't coded to the same standard as other classes.
## Test Results 
### cpen221.mp2.Task1DWTests
| Test Status | Count |
| ----------- | ----- |
|tests|9|
|skipped|0|
|failures|0|
|errors|0|
### cpen221.mp2.Task1UDWTests
| Test Status | Count |
| ----------- | ----- |
|tests|10|
|skipped|0|
|failures|0|
|errors|0|
### cpen221.mp2.Task2DWTests
| Test Status | Count |
| ----------- | ----- |
|tests|15|
|skipped|0|
|failures|1|
|errors|0|
#### Failed Tests
1. `testNthMostActiveUserAllSenders() (org.opentest4j.AssertionFailedError: expected: <2> but was: <1>)`
### cpen221.mp2.Task2UDWTests
| Test Status | Count |
| ----------- | ----- |
|tests|11|
|skipped|0|
|failures|0|
|errors|0|
### cpen221.mp2.Task3DWTests
| Test Status | Count |
| ----------- | ----- |
|tests|12|
|skipped|0|
|failures|2|
|errors|0|
#### Failed Tests
1. `testBFSUserDoesNotExist() (org.opentest4j.AssertionFailedError: expected: <null> but was: <[12]>)`
1. `testDFSUserDoesNotExist() (org.opentest4j.AssertionFailedError: expected: <null> but was: <[15]>)`
### cpen221.mp2.Task3UDWTests
| Test Status | Count |
| ----------- | ----- |
|tests|8|
|skipped|0|
|failures|0|
|errors|0|
### cpen221.mp2.Task4DWTests
| Test Status | Count |
| ----------- | ----- |
|tests|6|
|skipped|0|
|failures|1|
|errors|0|
#### Failed Tests
1. `testMaxedBreachedUserCount6() (org.opentest4j.AssertionFailedError: execution timed out after 150000 ms)`

## Code Quality
```
DWInteractionGraph.java:3:	Avoid unused imports such as 'com.sun.source.tree.Tree'
DWInteractionGraph.java:27:	Possible God Class (WMC=98, ATFD=39, TCC=25.974%)
DWInteractionGraph.java:27:	The class 'DWInteractionGraph' has a total cyclomatic complexity of 98 (highest 9).
DWInteractionGraph.java:27:	This class has too many methods, consider refactoring it.
DWInteractionGraph.java:31:	Found non-transient, non-static member. Please mark as transient or provide accessors.
DWInteractionGraph.java:42:	Found non-transient, non-static member. Please mark as transient or provide accessors.
DWInteractionGraph.java:47:	Found non-transient, non-static member. Please mark as transient or provide accessors.
DWInteractionGraph.java:49:	Avoid using implementation types like 'HashSet'; use the interface instead
DWInteractionGraph.java:50:	Avoid using implementation types like 'HashSet'; use the interface instead
DWInteractionGraph.java:50:	Found non-transient, non-static member. Please mark as transient or provide accessors.
DWInteractionGraph.java:55:	Avoid using implementation types like 'TreeSet'; use the interface instead
DWInteractionGraph.java:55:	Found non-transient, non-static member. Please mark as transient or provide accessors.
DWInteractionGraph.java:66:	Avoid using implementation types like 'TreeSet'; use the interface instead
DWInteractionGraph.java:66:	Found non-transient, non-static member. Please mark as transient or provide accessors.
DWInteractionGraph.java:77:	Found non-transient, non-static member. Please mark as transient or provide accessors.
DWInteractionGraph.java:77:	This final field could be made static
DWInteractionGraph.java:82:	Found non-transient, non-static member. Please mark as transient or provide accessors.
DWInteractionGraph.java:82:	This final field could be made static
DWInteractionGraph.java:87:	Found non-transient, non-static member. Please mark as transient or provide accessors.
DWInteractionGraph.java:87:	This final field could be made static
DWInteractionGraph.java:92:	Found non-transient, non-static member. Please mark as transient or provide accessors.
DWInteractionGraph.java:92:	This final field could be made static
DWInteractionGraph.java:97:	Found non-transient, non-static member. Please mark as transient or provide accessors.
DWInteractionGraph.java:97:	This final field could be made static
DWInteractionGraph.java:111:	Found 'DU'-anomaly for variable 'idsToCheck' (lines '111'-'130').
DWInteractionGraph.java:112:	This for loop can be replaced by a foreach loop
DWInteractionGraph.java:113:	Avoid using Literals in Conditional Statements
DWInteractionGraph.java:113:	Potential violation of Law of Demeter (method chain calls)
DWInteractionGraph.java:114:	Avoid throwing raw exception types.
DWInteractionGraph.java:120:	This for loop can be replaced by a foreach loop
DWInteractionGraph.java:122:	Avoid throwing raw exception types.
DWInteractionGraph.java:127:	Avoid throwing raw exception types.
DWInteractionGraph.java:146:	Ensure that resources like this FileReader object are closed after use
DWInteractionGraph.java:153:	System.out.println is used
DWInteractionGraph.java:175:	Consider using varargs for methods or constructors which take an array the last parameter.
DWInteractionGraph.java:197:	Consider using varargs for methods or constructors which take an array the last parameter.
DWInteractionGraph.java:258:	Potential violation of Law of Demeter (method chain calls)
DWInteractionGraph.java:261:	Potential violation of Law of Demeter (method chain calls)
DWInteractionGraph.java:261:	Potential violation of Law of Demeter (method chain calls)
DWInteractionGraph.java:293:	Potential violation of Law of Demeter (method chain calls)
DWInteractionGraph.java:294:	Potential violation of Law of Demeter (method chain calls)
DWInteractionGraph.java:294:	Potential violation of Law of Demeter (method chain calls)
DWInteractionGraph.java:299:	Potential violation of Law of Demeter (method chain calls)
DWInteractionGraph.java:323:	Avoid using implementation types like 'ArrayList'; use the interface instead
DWInteractionGraph.java:325:	Use one line for each declaration, it enhances code readability.
DWInteractionGraph.java:329:	Found 'DD'-anomaly for variable 'counter' (lines '329'-'342').
DWInteractionGraph.java:329:	Found 'DU'-anomaly for variable 'counter' (lines '329'-'352').
DWInteractionGraph.java:333:	Avoid reassigning the loop control variable 'email'
DWInteractionGraph.java:333:	Potential violation of Law of Demeter (object not created locally)
DWInteractionGraph.java:336:	Potential violation of Law of Demeter (object not created locally)
DWInteractionGraph.java:336:	Potential violation of Law of Demeter (object not created locally)
DWInteractionGraph.java:337:	Potential violation of Law of Demeter (object not created locally)
DWInteractionGraph.java:338:	Avoid reassigning the loop control variable 'email'
DWInteractionGraph.java:338:	Found 'DU'-anomaly for variable 'email' (lines '338'-'352').
DWInteractionGraph.java:338:	Potential violation of Law of Demeter (object not created locally)
DWInteractionGraph.java:340:	Found 'DD'-anomaly for variable 'counter' (lines '340'-'342').
DWInteractionGraph.java:342:	Found 'DD'-anomaly for variable 'counter' (lines '342'-'329').
DWInteractionGraph.java:342:	Found 'DD'-anomaly for variable 'counter' (lines '342'-'342').
DWInteractionGraph.java:342:	Found 'DU'-anomaly for variable 'counter' (lines '342'-'352').
DWInteractionGraph.java:380:	Potential violation of Law of Demeter (method chain calls)
DWInteractionGraph.java:392:	Potential violation of Law of Demeter (method chain calls)
DWInteractionGraph.java:410:	Avoid using implementation types like 'ArrayList'; use the interface instead
DWInteractionGraph.java:425:	Consider using varargs for methods or constructors which take an array the last parameter.
DWInteractionGraph.java:426:	Found 'DU'-anomaly for variable 'lowBound' (lines '426'-'443').
DWInteractionGraph.java:427:	Found 'DU'-anomaly for variable 'highBound' (lines '427'-'443').
DWInteractionGraph.java:428:	Found 'DD'-anomaly for variable 'timeMetrics' (lines '428'-'433').
DWInteractionGraph.java:428:	Found 'DD'-anomaly for variable 'timeMetrics' (lines '428'-'440').
DWInteractionGraph.java:433:	Found 'DD'-anomaly for variable 'timeMetrics' (lines '433'-'433').
DWInteractionGraph.java:433:	Found 'DD'-anomaly for variable 'timeMetrics' (lines '433'-'440').
DWInteractionGraph.java:440:	Found 'DD'-anomaly for variable 'timeMetrics' (lines '440'-'441').
DWInteractionGraph.java:456:	Found 'DU'-anomaly for variable 'interactions' (lines '456'-'471').
DWInteractionGraph.java:457:	Found 'DD'-anomaly for variable 'userMetric' (lines '457'-'465').
DWInteractionGraph.java:457:	Found 'DU'-anomaly for variable 'userMetric' (lines '457'-'471').
DWInteractionGraph.java:465:	Found 'DD'-anomaly for variable 'userMetric' (lines '465'-'467').
DWInteractionGraph.java:467:	Found 'DD'-anomaly for variable 'userMetric' (lines '467'-'469').
DWInteractionGraph.java:486:	Potential violation of Law of Demeter (method chain calls)
DWInteractionGraph.java:501:	Potential violation of Law of Demeter (object not created locally)
DWInteractionGraph.java:506:	Potential violation of Law of Demeter (method chain calls)
DWInteractionGraph.java:506:	Potential violation of Law of Demeter (method chain calls)
DWInteractionGraph.java:521:	Found 'DU'-anomaly for variable 'history' (lines '521'-'529').
DWInteractionGraph.java:543:	Potential violation of Law of Demeter (object not created locally)
DWInteractionGraph.java:568:	Potential violation of Law of Demeter (method chain calls)
DWInteractionGraph.java:570:	Potential violation of Law of Demeter (method chain calls)
DWInteractionGraph.java:588:	Avoid using Literals in Conditional Statements
DWInteractionGraph.java:599:	Potential violation of Law of Demeter (object not created locally)
DWInteractionGraph.java:602:	Potential violation of Law of Demeter (object not created locally)
DWInteractionGraph.java:606:	Potential violation of Law of Demeter (object not created locally)
DWInteractionGraph.java:626:	This for loop can be replaced by a foreach loop
DWInteractionGraph.java:634:	Potential violation of Law of Demeter (method chain calls)
DWInteractionGraph.java:675:	Found 'DU'-anomaly for variable 'searchMap' (lines '675'-'691').
DWInteractionGraph.java:683:	These nested if statements could be combined
DWInteractionGraph.java:707:	Potential violation of Law of Demeter (method chain calls)
DWInteractionGraph.java:708:	Potential violation of Law of Demeter (method chain calls)
DWInteractionGraph.java:712:	Potential violation of Law of Demeter (object not created locally)
DWInteractionGraph.java:713:	Potential violation of Law of Demeter (object not created locally)
DWInteractionGraph.java:732:	Found 'DU'-anomaly for variable 'searchMap' (lines '732'-'748').
EmailUser.java:12:	Found non-transient, non-static member. Please mark as transient or provide accessors.
EmailUser.java:13:	Found non-transient, non-static member. Please mark as transient or provide accessors.
EmailUser.java:14:	Avoid using implementation types like 'HashSet'; use the interface instead
EmailUser.java:14:	Found non-transient, non-static member. Please mark as transient or provide accessors.
UDWInteractionGraph.java:23:	Possible God Class (WMC=85, ATFD=24, TCC=27.368%)
UDWInteractionGraph.java:23:	The class 'UDWInteractionGraph' has a total cyclomatic complexity of 85 (highest 9).
UDWInteractionGraph.java:23:	This class has too many methods, consider refactoring it.
UDWInteractionGraph.java:27:	Avoid using implementation types like 'ArrayList'; use the interface instead
UDWInteractionGraph.java:27:	Found non-transient, non-static member. Please mark as transient or provide accessors.
UDWInteractionGraph.java:36:	Avoid using implementation types like 'ArrayList'; use the interface instead
UDWInteractionGraph.java:36:	Found non-transient, non-static member. Please mark as transient or provide accessors.
UDWInteractionGraph.java:36:	Private field 'orderedNodes' could be made final; it is only initialized in the declaration or constructor.
UDWInteractionGraph.java:41:	Avoid using implementation types like 'ArrayList'; use the interface instead
UDWInteractionGraph.java:41:	Found non-transient, non-static member. Please mark as transient or provide accessors.
UDWInteractionGraph.java:41:	Private field 'interactions' could be made final; it is only initialized in the declaration or constructor.
UDWInteractionGraph.java:44:	Avoid using implementation types like 'HashSet'; use the interface instead
UDWInteractionGraph.java:44:	Found non-transient, non-static member. Please mark as transient or provide accessors.
UDWInteractionGraph.java:44:	Private field 'ids' could be made final; it is only initialized in the declaration or constructor.
UDWInteractionGraph.java:48:	Avoid using implementation types like 'HashMap'; use the interface instead
UDWInteractionGraph.java:48:	Found non-transient, non-static member. Please mark as transient or provide accessors.
UDWInteractionGraph.java:48:	Private field 'userIndex' could be made final; it is only initialized in the declaration or constructor.
UDWInteractionGraph.java:53:	Found non-transient, non-static member. Please mark as transient or provide accessors.
UDWInteractionGraph.java:56:	Found non-transient, non-static member. Please mark as transient or provide accessors.
UDWInteractionGraph.java:58:	Found non-transient, non-static member. Please mark as transient or provide accessors.
UDWInteractionGraph.java:58:	This final field could be made static
UDWInteractionGraph.java:59:	Found non-transient, non-static member. Please mark as transient or provide accessors.
UDWInteractionGraph.java:59:	This final field could be made static
UDWInteractionGraph.java:60:	Found non-transient, non-static member. Please mark as transient or provide accessors.
UDWInteractionGraph.java:60:	This final field could be made static
UDWInteractionGraph.java:70:	This for loop can be replaced by a foreach loop
UDWInteractionGraph.java:71:	Avoid throwing raw exception types.
UDWInteractionGraph.java:71:	Avoid using Literals in Conditional Statements
UDWInteractionGraph.java:71:	Potential violation of Law of Demeter (method chain calls)
UDWInteractionGraph.java:74:	Found 'DD'-anomaly for variable 'edges' (lines '74'-'77').
UDWInteractionGraph.java:77:	Found 'DD'-anomaly for variable 'edges' (lines '77'-'77').
UDWInteractionGraph.java:81:	Avoid throwing raw exception types.
UDWInteractionGraph.java:106:	Ensure that resources like this FileReader object are closed after use
UDWInteractionGraph.java:112:	System.out.println is used
UDWInteractionGraph.java:133:	Consider using varargs for methods or constructors which take an array the last parameter.
UDWInteractionGraph.java:149:	Consider using varargs for methods or constructors which take an array the last parameter.
UDWInteractionGraph.java:247:	Avoid using implementation types like 'ArrayList'; use the interface instead
UDWInteractionGraph.java:247:	Avoid using implementation types like 'ArrayList'; use the interface instead
UDWInteractionGraph.java:249:	Use one line for each declaration, it enhances code readability.
UDWInteractionGraph.java:253:	Found 'DD'-anomaly for variable 'counter' (lines '253'-'266').
UDWInteractionGraph.java:253:	Found 'DU'-anomaly for variable 'counter' (lines '253'-'280').
UDWInteractionGraph.java:257:	Avoid reassigning the loop control variable 'email'
UDWInteractionGraph.java:257:	Potential violation of Law of Demeter (object not created locally)
UDWInteractionGraph.java:260:	Potential violation of Law of Demeter (object not created locally)
UDWInteractionGraph.java:260:	Potential violation of Law of Demeter (object not created locally)
UDWInteractionGraph.java:261:	Potential violation of Law of Demeter (object not created locally)
UDWInteractionGraph.java:262:	Avoid reassigning the loop control variable 'email'
UDWInteractionGraph.java:262:	Found 'DU'-anomaly for variable 'email' (lines '262'-'280').
UDWInteractionGraph.java:262:	Potential violation of Law of Demeter (object not created locally)
UDWInteractionGraph.java:264:	Found 'DD'-anomaly for variable 'counter' (lines '264'-'266').
UDWInteractionGraph.java:266:	Found 'DD'-anomaly for variable 'counter' (lines '266'-'253').
UDWInteractionGraph.java:266:	Found 'DD'-anomaly for variable 'counter' (lines '266'-'266').
UDWInteractionGraph.java:266:	Found 'DU'-anomaly for variable 'counter' (lines '266'-'280').
UDWInteractionGraph.java:292:	Avoid using implementation types like 'ArrayList'; use the interface instead
UDWInteractionGraph.java:302:	Found 'DD'-anomaly for variable 'dataForUser' (lines '302'-'310').
UDWInteractionGraph.java:302:	Found 'DD'-anomaly for variable 'dataForUser' (lines '302'-'315').
UDWInteractionGraph.java:302:	Found 'DD'-anomaly for variable 'dataForUser' (lines '302'-'318').
UDWInteractionGraph.java:310:	Found 'DD'-anomaly for variable 'dataForUser' (lines '310'-'310').
UDWInteractionGraph.java:310:	Found 'DD'-anomaly for variable 'dataForUser' (lines '310'-'315').
UDWInteractionGraph.java:310:	Found 'DD'-anomaly for variable 'dataForUser' (lines '310'-'318').
UDWInteractionGraph.java:315:	Found 'DD'-anomaly for variable 'dataForUser' (lines '315'-'310').
UDWInteractionGraph.java:315:	Found 'DD'-anomaly for variable 'dataForUser' (lines '315'-'315').
UDWInteractionGraph.java:315:	Found 'DD'-anomaly for variable 'dataForUser' (lines '315'-'318').
UDWInteractionGraph.java:318:	Found 'DD'-anomaly for variable 'dataForUser' (lines '318'-'310').
UDWInteractionGraph.java:318:	Found 'DD'-anomaly for variable 'dataForUser' (lines '318'-'315').
UDWInteractionGraph.java:318:	Found 'DD'-anomaly for variable 'dataForUser' (lines '318'-'318').
UDWInteractionGraph.java:335:	Consider using varargs for methods or constructors which take an array the last parameter.
UDWInteractionGraph.java:339:	Found 'DD'-anomaly for variable 'activityReport' (lines '339'-'340').
UDWInteractionGraph.java:340:	Found 'DD'-anomaly for variable 'activityReport' (lines '340'-'341').
UDWInteractionGraph.java:340:	Potential violation of Law of Demeter (static property access)
UDWInteractionGraph.java:341:	Potential violation of Law of Demeter (static property access)
UDWInteractionGraph.java:355:	Found 'DD'-anomaly for variable 'userInformation' (lines '355'-'357').
UDWInteractionGraph.java:357:	Found 'DD'-anomaly for variable 'userInformation' (lines '357'-'358').
UDWInteractionGraph.java:368:	Found 'DD'-anomaly for variable 'count' (lines '368'-'374').
UDWInteractionGraph.java:371:	This for loop can be replaced by a foreach loop
UDWInteractionGraph.java:374:	Found 'DD'-anomaly for variable 'count' (lines '374'-'374').
UDWInteractionGraph.java:403:	Avoid using Literals in Conditional Statements
UDWInteractionGraph.java:420:	Avoid using implementation types like 'HashMap'; use the interface instead
UDWInteractionGraph.java:434:	Avoid using implementation types like 'HashMap'; use the interface instead
UDWInteractionGraph.java:449:	This for loop can be replaced by a foreach loop
UDWInteractionGraph.java:462:	Avoid using implementation types like 'HashMap'; use the interface instead
UDWInteractionGraph.java:464:	Found 'DD'-anomaly for variable 'maxVal' (lines '464'-'464').
UDWInteractionGraph.java:464:	Found 'DU'-anomaly for variable 'maxVal' (lines '464'-'479').
UDWInteractionGraph.java:465:	Found 'DD'-anomaly for variable 'maxKey' (lines '465'-'474').
UDWInteractionGraph.java:473:	Found 'DD'-anomaly for variable 'maxVal' (lines '473'-'464').
UDWInteractionGraph.java:473:	Found 'DU'-anomaly for variable 'maxVal' (lines '473'-'479').
UDWInteractionGraph.java:474:	Found 'DD'-anomaly for variable 'maxKey' (lines '474'-'474').
UDWInteractionGraph.java:512:	Consider using varargs for methods or constructors which take an array the last parameter.
UDWInteractionGraph.java:513:	Found 'DD'-anomaly for variable 'nodes' (lines '513'-'514').
UDWInteractionGraph.java:514:	Found 'DD'-anomaly for variable 'nodes' (lines '514'-'514').
UDWInteractionGraph.java:514:	Substitute calls to size() == 0 (or size() != 0, size() > 0, size() < 1) with calls to isEmpty()
UDWInteractionGraph.java:532:	Consider using varargs for methods or constructors which take an array the last parameter.
UDWInteractionGraph.java:549:	These nested if statements could be combined
UDWInteractionGraph.java:574:	Found 'DD'-anomaly for variable 'componentSets' (lines '574'-'577').
UDWInteractionGraph.java:577:	Found 'DD'-anomaly for variable 'componentSets' (lines '577'-'577').
UDWInteractionGraph.java:601:	System.out.println is used
UDWInteractionGraph.java:605:	System.out.println is used
LoosePackageCoupling	-	No packages or classes specified
```

## Test Coverage
### UDWInteractionGraph
| Metric | Coverage |
| ------ | -------- |
|LINE_MISSED|3|
|LINE_COVERED|194|
|BRANCH_MISSED|3|
|BRANCH_COVERED|111|
### DWInteractionGraph
| Metric | Coverage |
| ------ | -------- |
|LINE_MISSED|5|
|LINE_COVERED|278|
|BRANCH_MISSED|4|
|BRANCH_COVERED|140|

## Checkstyle Results
### `DWInteractionGraph.java`
| Line | Column | Message |
| ---- | ------ | ------- |
| 3 | 8 | `Unused import - com.sun.source.tree.Tree.` |
| 114 | 92 | `'+' should be on a new line.` |
| 122 | 94 | `'+' should be on a new line.` |
| 127 | 97 | `'+' should be on a new line.` |
| 177 |  | `Line is longer than 100 characters (found 101).` |
| 177 | 28 | `Name 'DW' must match pattern '^([A-Z][0-9]*|[a-z][a-zA-Z0-9]*)$'.` |
| 269 |  | `@return tag should be present and have description.` |
| 269 | 88 | `'emailData' hides a field.` |
| 323 | 51 | `'emails' hides a field.` |
| 333 | 47 | `';' is not followed by whitespace.` |
| 357 |  | `Line is longer than 100 characters (found 113).` |
| 358 |  | `Line is longer than 100 characters (found 130).` |
| 359 |  | `Line is longer than 100 characters (found 116).` |
| 361 | 50 | `'emails' hides a field.` |
| 377 | 44 | `'emails' hides a field.` |
| 425 | 18 | `Name 'ReportActivityInTimeWindow' must match pattern '^[a-z][a-zA-Z0-9]*$'.` |
| 455 | 18 | `Name 'ReportOnUser' must match pattern '^[a-z][a-zA-Z0-9]*$'.` |
| 587 | 16 | `Name 'NthMostActiveUser' must match pattern '^[a-z][a-zA-Z0-9]*$'.` |
| 621 | 26 | `Name 'BFS' must match pattern '^[a-z][a-zA-Z0-9]*$'.` |
| 654 | 26 | `Name 'DFS' must match pattern '^[a-z][a-zA-Z0-9]*$'.` |
| 697 |  | `Line is longer than 100 characters (found 178).` |
| 698 |  | `Line is longer than 100 characters (found 163).` |
| 699 |  | `Line is longer than 100 characters (found 102).` |
| 701 |  | `Line is longer than 100 characters (found 182).` |
| 702 |  | `Line is longer than 100 characters (found 124).` |
| 704 | 16 | `Name 'MaxBreachedUserCount' must match pattern '^[a-z][a-zA-Z0-9]*$'.` |
| 710 | 69 | `'60' is a magic number.` |
| 710 | 74 | `'60' is a magic number.` |
| 723 |  | `Line is longer than 100 characters (found 125).` |
| 724 |  | `Line is longer than 100 characters (found 110).` |
| 725 |  | `Line is longer than 100 characters (found 111).` |
| 726 |  | `Line is longer than 100 characters (found 149).` |
| 727 |  | `Line is longer than 100 characters (found 121).` |
| 728 |  | `Line is longer than 100 characters (found 145).` |
### `EmailUser.java`
| Line | Column | Message |
| ---- | ------ | ------- |
| 7 |  | `Line is longer than 100 characters (found 105).` |
| 8 |  | `Line is longer than 100 characters (found 108).` |
| 16 | 5 | `Redundant 'public' modifier.` |
| 16 | 29 | `'{' is not preceded with whitespace.` |
| 20 | 5 | `Redundant 'public' modifier.` |
| 20 | 60 | `'{' is not preceded with whitespace.` |
| 22 | 9 | `'if' is not followed by whitespace.` |
| 22 | 21 | `'(' is followed by whitespace.` |
| 22 | 46 | `'{' is not preceded with whitespace.` |
| 25 | 9 | `'}' at column 9 should be on the same line as the next part of a multi-block statement (one that directly contains multiple blocks: if/else-if/else, do/while or try/catch/finally).` |
| 26 | 9 | `'else' is not followed by whitespace.` |
| 26 | 13 | `'{' is not preceded with whitespace.` |
| 32 | 41 | `'{' is not preceded with whitespace.` |
| 37 | 41 | `'{' is not preceded with whitespace.` |
| 42 | 29 | `'{' is not preceded with whitespace.` |
| 46 | 32 | `'{' is not preceded with whitespace.` |
| 50 | 36 | `'{' is not preceded with whitespace.` |
| 54 | 23 | `'{' is not preceded with whitespace.` |
| 60 | 9 | `'if' construct must use '{}'s.` |
| 61 | 9 | `'if' is not followed by whitespace.` |
| 64 | 9 | `'}' at column 9 should be on the same line as the next part of a multi-block statement (one that directly contains multiple blocks: if/else-if/else, do/while or try/catch/finally).` |
### `SendOrReceive.java`
| Line | Column | Message |
| ---- | ------ | ------- |
### `UDWInteractionGraph.java`
| Line | Column | Message |
| ---- | ------ | ------- |
| 14 |  | `Line is longer than 100 characters (found 102).` |
| 15 |  | `Line is longer than 100 characters (found 101).` |
| 20 |  | `Line is longer than 100 characters (found 109).` |
| 71 |  | `Line is longer than 100 characters (found 102).` |
| 71 | 13 | `'if' construct must use '{}'s.` |
| 77 | 17 | `'if' construct must use '{}'s.` |
| 81 |  | `Line is longer than 100 characters (found 101).` |
| 81 | 9 | `'if' construct must use '{}'s.` |
| 81 | 101 | `'+' should be on a new line.` |
| 107 |  | `Line is longer than 100 characters (found 103).` |
| 152 | 17 | `Name 'LOWER_TIME' must match pattern '^([A-Z][0-9]*|[a-z][a-zA-Z0-9]*)$'.` |
| 153 | 17 | `Name 'UPPER_TIME' must match pattern '^([A-Z][0-9]*|[a-z][a-zA-Z0-9]*)$'.` |
| 182 | 79 | `'||' should be on a new line.` |
| 232 | 9 | `'if' construct must use '{}'s.` |
| 257 | 47 | `';' is not followed by whitespace.` |
| 292 | 18 | `Name 'MapEmailData' must match pattern '^[a-z][a-zA-Z0-9]*$'.` |
| 335 | 18 | `Name 'ReportActivityInTimeWindow' must match pattern '^[a-z][a-zA-Z0-9]*$'.` |
| 354 | 18 | `Name 'ReportOnUser' must match pattern '^[a-z][a-zA-Z0-9]*$'.` |
| 369 | 9 | `'if' construct must use '{}'s.` |
| 390 | 9 | `'if' construct must use '{}'s.` |
| 401 | 16 | `Name 'NthMostActiveUser' must match pattern '^[a-z][a-zA-Z0-9]*$'.` |
| 403 | 9 | `'if' construct must use '{}'s.` |
| 420 | 57 | `',' is not followed by whitespace.` |
| 422 | 13 | `'if' construct must use '{}'s.` |
| 462 | 56 | `Expected @param tag for 'interactionCounts'.` |
| 487 | 16 | `Name 'NumberOfComponents' must match pattern '^[a-z][a-zA-Z0-9]*$'.` |
| 514 | 9 | `'for' construct must use '{}'s.` |
| 514 | 50 | `'if' construct must use '{}'s.` |
| 598 | 20 | `Name 'PathExists' must match pattern '^[a-z][a-zA-Z0-9]*$'.` |
| 600 |  | `Line is longer than 100 characters (found 117).` |

