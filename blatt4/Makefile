all:
	find . -name "*.java" | javac @/dev/stdin
	
run:
	java Test
	
clean:
	find . -name "*.class" -exec rm {} \;
	find . -name "*~" -exec rm {} \;