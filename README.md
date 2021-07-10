# ucf-spa

**_University College Fryslân - Student-Project Assignment_**

This application supports the assignment of students to projects in two scenarios: capstone projects and living lab projects.

## About the Application

The assignment of students is solved as a mixed-integer problem (MIP), where the optimization goal is to minimize the total cost of assigning workers (i.e., students) to tasks (i.e., projects).

The cost of assigning a student to a project can be reduced based on students' fondness for particular projects, as well as supervisors' preference to supervisor particular projects (only in the capstone scenario).

The optimization characteristics and constraints in each scenario are as follow.

### Capstone Projects

- Characteristics
  - Students rank projects based on fondness (e.g., 1 -> first choice).
  - Ranking reduces the student-project cost (1 -> reduces the most).
  - Each project has a supervisor.
  - Supervisors may offer multiple projects.
  - Supervisor can mark some projects as preferred.
  - Preferred projects result in a small cost reduction for all students.

- Constraints
  - Each student must be assigned to exactly one project.
  - Each project can be assigned to one student at most.
  - Each supervisor can supervise two projects at most (can be optionally disabled).

### Living Lab Projects

- Characteristics
  - Students rank projects based on fondness (e.g., 1 -> first choice).
  - Ranking reduces the student-project cost (1 -> reduces the most).
  - Each project has a host organization.

- Constraints
  - Each student must be assigned to exactly one project.
  - Each project can be assigned to a range of students (min and max defined per project).

## Software Requirements

The applications works with machine **Windows**, **MacOS** or **Linux** operating systems.

To run the application, [**Java**](https://java.com/en/download/help/download_options.html) (version 8 or greater) must be preinstalled in the machine.

## Use Instructions

1. Make sure you are using one of the supported operating systems and have Java installed (see requirements).
1. Download the latest version of the application from the [releases page](https://github.com/feitosa-daniel/ucf-spa/releases).
1. Open the application.
1. Open an input spreadsheet with the appropriate format for you scenario (see format below).
1. Click on "Assign" to see the suggested assignments.
1. While manual adjustments are desired, change individual assignments manually, fix the assignment and click on "Assign" again.
1. Click on "Export" to save the final student-project assignments.

### Input File Format

The application supports Excel (`.xlsx` and `.xls`) and CSV spreadsheet files.

Examples of valid spreadsheets can be found [here](src/test/resources).

#### **Spreadsheet Format for Capstone Assignments**

Spreadsheets for capstone projects assignment should look like the following.

|projects |supervisor    |preferred|Student 1|Student 2|Student 3|
|---------|--------------|---------|---------|---------|---------|
|Project A|Supervisor One|         |    1    |         |         |
|Project B|Supervisor One|    y    |         |         |    1    |
|Project C|Supervisor One|         |         |    1    |    2    |
|Project D|Supervisor Two|         |    2    |         |    3    |
|Project E|Supervisor Two|         |    3    |    2    |         |
|Project F|Supervisor Two|    y    |         |    3    |         |

- **Project names** go in the **first column**, under the header.
- **Supervisor names** go in the **second column**, under the header.
- The **preference of supervisors** are marked with `y`, `yes` or `1` in the **third column** (letter case does not matter).
- **Student names** go in the **first row**, after "preferred".
- **Students rankings** go in the **column of the respective students**.

#### **Spreadsheet Format for Living Lab Assignments**

Spreadsheets for living lab projects assignment should look like the following.

|projects |organization  |min-students|max-students|Student 1|Student 2|Student 3|Student 4|Student 5|
|---------|--------------|------------|------------|---------|---------|---------|---------|---------|
|Project A|Organization 1|      1     |      4     |    1    |    1    |         |    1    |         | 
|Project B|Organization 2|      1     |      4     |    2    |    2    |    1    |         |    3    | 
|Project C|Organization 3|      1     |      4     |    3    |    3    |    2    |    3    |    1    | 
|Project D|Organization 4|      2     |      4     |         |         |    3    |    2    |    2    | 

- **Project names** go in the **first column**, under the header.
- **Organization names** go in the **second column**, under the header.
- The **minimum required number of students** in the projects go in the **third column**, under the header.
- The **maximum allowed number of students** in the projects go in the **fourth column**, under the header.
- **Student names* go in the **first row**, after "max-students".
- **Students rankings** go in the **column of the respective students**.

## Development

### Development Environment

You can use any text editor, code editor or IDE of your liking.

For better integration with the project automation, use a code editor or IDE that supports [Gradle](https://gradle.org/).

The user interface was created using the [Apache Netbeans](https://netbeans.apache.org/) IDE. So, if you want to modify the UI, I suggest using this IDE.

You will also need a [Java Development Kit (JDK)](https://java.com/en/download/help/develop.html) version 8 or above (this is different from a regular JRE---Java Runtime Environment).

### Main Tasks

To build the production `.jar` file:
```shell
gradle shadowJar
```

To test the application:
```shell
gradle test
```

### Submiting PRs and Issues

This software is meant to attend the use cases of University College Fryslân.

So, changes and issues will be handled with that in mind, which means that:
- changes will only be accepted if they do not interfere with the primary purposes described in this document; and
- issues will be prioritized based on the mentioned primary purposes.

That said, if you want to make changes that remove its current functionality, you are welcome to create your own fork (please keep the license in mind)! =)

### License and Reuse

This project is distributed under [GNU GPLv3](LICENSE), which (in overly simplified terms) lets you do almost anything, except distributing closed source versions of this software.

## Credits

This project uses the following libraries/frameworks/technologies:
- [Gradle](https://gradle.org/) (with Kotlin DSL) for project tasks automation.
- [Google OR-Tools](https://developers.google.com/optimization) to solve the MIP optmization.
- [FormDev's FlatLaf](https://www.formdev.com/flatlaf/) for the User Interface look and feel.
- [Apache Netbeans](https://netbeans.apache.org/) for User Interface design.
- [Apache POI](https://poi.apache.org/) to parse XLS and XLSX spreadsheets.
- [Apache Commons CSV](https://commons.apache.org/proper/commons-csv/) to parse CSV files.
- [Apache Commons IO](http://commons.apache.org/proper/commons-io/) for file utilities.
- [JUnit 5 and JUnit Jupiter](https://junit.org/junit5) for testing.
