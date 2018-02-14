# CPL-Java
This is the repository for the course project in Concepts of Programming Languages. The goal was to create an interpreter for a subset of the Lua programming language. The requirements and specification of the subset are in the Docs folder. 

This program consists of 3 parts:
1. The Scanner, which takes in the lua file and determines what category each token falls under
2. The Parser, which turns the list of tokens into a list of statements, and ensures that each statement that is opened eventually is closed (such as an if statement). 
3. The Interpeter, it takes the output of the parser and executes the code, prints output, etc. 
