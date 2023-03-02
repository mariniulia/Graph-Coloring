JFLAGS = -g;
JC = javac
.SUFIXES: .java .class

.java.class:
    $(JC) $(JFLAGS) $*.java

    #CLASSES = \
        #Greedy.java \
        #Backtracking.java \
        #Graph.java \
    #default: classes

    build:
	classes

    run-p1:
        java Greedy

    run-p2:
        java Backtracking

    run-best:
        java Greedy

    clean:
        $(RM)*.class
    # classes: $(CLASSES:.java=.class)
