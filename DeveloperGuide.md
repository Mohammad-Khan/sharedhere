# 1. Conventions #

Please follow these set of guidelines to generate code that is understood by everyone.

## 1.1. Coding Conventions ##
### 1.1.1. Class Names ###
For all classes, interfaces and similar entities that you create, prepend their name by SH (or maybe SharedHere, the team should agree on one the two).
### 1.1.2. Styles for Methods and Attributes ###
Follow the Java Coding Conventions. e.g. use camelCase for method and attribute names with methods names being verbs and attribute names being nouns.

## 1.2. Version Control ##
### 1.2.1. Atomic Commits ###
Make sure each of your commits to the mainline repository represents one logically-related code set.

## 1.2.2. Mainline Repository ##
All project owners have push access to the mainline repository. To avoid git conflicts, coordinate with other project owners when you want to push into the mainline.

# 2. Releases #

For a full coverage on releases, please refer to the milestones section of the project checklist document or send an email to the mailing list. At this point only an alpha version of the application is released.

# 3. Communications #

## 3.1. The Mailing List ##
Please use the mailing list for all communications that are related to technical issues such as design, development and QA.

## 3.2. Issue Tracking ##
Report bugs, features and other issues in the issue tracker. The developers can assign issues to themselves and change status as the progress towards resolution.

## 3.3. Meetings ##
The project owners will call up for (in person) meetings as and when necessary.

# 4. How To Contribute #

## 4.1. Where to Start ##
For information on how to prepare your development environment and start developing hacking on SharedHere you can see <a href='http://code.google.com/p/sharedhere/wiki/BeginnersGuide'>Beginner's Guide</a> and <a href='http://code.google.com/p/sharedhere/wiki/ServerGuide'>Server Guide</a>.
Want to contribute? The first place you should check is <a href='http://code.google.com/p/sharedhere/issues/list'>the issue tracker</a>. We maintain a list of open issues most probably with a varying level of difficulty. If you do not find an issue of interest in the issue tracker or if you want to discuss a design issue, please send an email to the mailing list (sharedhere@googlegroups.com).
As a starter please also see the UML diagrams, which will give you a quick overall view of SharedHere.
<ul>
<li><a href='http://sharedhere.googlecode.com/git/UMLDiagrams/images/sharedherePkgDiagram.gif'>Package Diagram</a></li>
<li><a href='http://sharedhere.googlecode.com/git/UMLDiagrams/images/sharedhereClassDiagram.gif'>Class Diagram</a></li>
<li><a href='http://sharedhere.googlecode.com/git/UMLDiagrams/images/sharedhereSequenceUpload.gif'>Sequence Diagram (upload activity)</a></li>
<li><a href='http://sharedhere.googlecode.com/git/UMLDiagrams/images/sharedhereSequenceDownload.gif'>Sequence Diagram (download activity)</a></li>
</ul>

## 4.2. Who Can Contribute ##
SharedHere spans a fairly diverse bunch of technologies, languages and paradigms, which makes virtually any programmer with any level of skill to jump in and contribute. We have room for contributors with the following skills:
<ul>
<li>Android and Java Development</li>
<li>PHP Programming</li>
<li>SQL Scripting (MySQL)</li>
<li>Web services and Integration</li>
</ul>
If you have an idea that you want implemented in SharedHere, you can discuss it with the SharedHere Community on the mailing list. Your ideas are welcome and if they are valid ones will get implemented.

## 4.3. Commit Access ##
To get you need to submit a few patches and/or fix bugs and submit bug-fixes. After the Core Team have reviewed your patches they will decide when to give you commit access. Commit access is the means to push changes into the mainline repository. The next section, 4.4. Submit Patches, teaches you how to submit your patches or bug fixes. To see list of current committers see MAINTAINERS file in the source code.

## 4.4. Submit Patches ##
<ul>
<li>Clone the latest development codebase. More on cloning <a href='http://code.google.com/p/sharedhere/source/checkout'>here</a></li>
<li>Make the changes and or bug fixes that you want.</li>
<li>Commit your changes locally.</li>
<li>Prepare a patch with git.</li>
<li>Submit your patch on the mailing list, preferably paste in body of your email rather than attaching it to it. To know how to make a patch with git <a href='http://andrewprice.me.uk/weblog/entry/generating-patch-emails-with-git'>see here</a></li>
</ul>

## 4.5. Report Bugs or New Features ##
If you find bugs in SharedHere or if you want a new feature to be added into SharedHere, please let us know. You can do so but using <a href='http://code.google.com/p/sharedhere/issues/list'>the issue tracker</a> or you can just send an email to the mailing list (sharedhere@googlegroups.com).