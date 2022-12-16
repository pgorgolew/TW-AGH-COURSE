### HOW TO RUN

1. go to **TutorChecker** directory
2. run `mvn compile exec:java -D exec.mainClass=pl.edu.agh.macwozni.matrixtw.Generator -D exec.args="<matrix_szie> <path_to_GaussElimination_direcotry>\inTutor.txt <path_to_GaussElimination_direcotry>\outTutor.txt"`
3. go to **GaussElimination** directory
4. run `mvn compile exec:java -D exec.mainClass=pl.agh.edu.tw.Main -D exec.args="inTutor.txt"`
5. go to **TutorChecker** directory
6. run `mvn compile exec:java -D exec.mainClass=pl.edu.agh.macwozni.matrixtw.Checker -D exec.args="<path_to_GaussElimination_direcotry>\inTutor.txt <path_to_GaussElimination_direcotry>\out.txt"`