We are using Java 22

1. Open pgAdmin or download it and create database called "parkup"
2. Clone the repository
3. Open Intellij or download it
4. In src/main/resources/application.properties set your pgAdmin password in the field "spring.datasource.password" 
5. In Intellij go to "File -> Project Structure -> Project Settings"
6. In "Project" choose SDK version 22 or download it by clicking on "+Add SDK" from the dropdown and choose version 22
8. In "Project -> Language level" should be SDK default
9. In "Modules" choose Module SDK version 22 or download it by clicking on "+Add SDK" from the dropdown and choose version 22
10. Run the application and check if the tables have been created in the database

Code style:
1. Do not push directly to the "main" branch, always create a new branch and then merge it into "main" branch with PR so we can have better visibility of history when changing the code
2. Merge PR with 'Squash and merge'
3. Please do not use wildcard imports, so we can have better visibility for what is used. To disable the wildcard import in Intellij go to "File -> Settings -> Editor -> Code Style -> Java", from there choose the "Imports" tab and then set "Class count to use import with *" and "Names count to use static import with *" to 9999
4. Check for not used imports and delete them before committing
5. Check for not used parameters, fields, ... and delete them before committing
6. Use native queries in repository
