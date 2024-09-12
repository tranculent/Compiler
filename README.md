# Compiler

A Java-based compiler project that includes a suite of tools for lexical analysis, syntax analysis, and abstract syntax tree construction. This project is ideal for those interested in understanding the internals of compiler design and implementation.

## Features

- **Lexical Analyzer**: Tokenizes input source code into manageable pieces.
- **Syntax Analyzer**: Parses tokens to validate and construct syntax trees.
- **Abstract Syntax Analyzer**: Analyzes and optimizes abstract syntax representations.
- **Compilation**: End-to-end process to compile code.

## Getting Started

### Prerequisites

- Java 11 or higher
- Maven (optional, for dependency management)

### Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/tranculent/Compiler.git
   cd Compiler
2. Compile the project:
   ```bash
   javac -d bin src/*.java
3. Run the main program:
   ```bash
   java -cp bin Compile

## Usage
  1. Lexical Analysis: Use the LexicalAnalyser class to perform tokenization.
  2. Syntax Analysis: Utilize SyntaxAnalyzer for parsing tokens.
  3. Abstract Syntax: Analyze and manipulate abstract syntax using AbstractSyntaxAnalyser.

## Project Structure
  * src/: Contains source code files.
  * bin/: Compiled Java bytecode.
  * Compile.java: Main entry point for the compiler.

## Contributing
1. Fork the repository.
2. Create a new branch (feature/your-feature).
3. Commit changes (git commit -am 'Add feature').
4. Push to the branch (git push origin feature/your-feature).
5. Open a pull request.

## License
This project is licensed under the MIT License - see the LICENSE file for details.
