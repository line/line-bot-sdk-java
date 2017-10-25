import java.lang.StringBuilder;

/**
 * A representation of the result from a class.
 *
 * In this context, {@link #gradePoints()} refers to the grade points corresponding to the assigned
 * grade, e.g. a B corresponds to a grade point of 3.0.
 */
class ClassResult {
    private final String classCode;
    private final String className;
    private final double gradePoints;
    private final int credits;

    public ClassResult(
            String classCode, String className, double gradePoints, int credits) {
        this.classCode = classCode;
        this.className = className;
        this.gradePoints = gradePoints;
        this.credits = credits;
    }

    public String classCode() { return classCode; }
    public String className() { return className; }
    public double gradePoints() { return gradePoints; }
    public int credits() { return credits; }
}

/**
 * A representation of a transcript for a single semester.
 *
 * The {@link generateTranscriptForWidth} method prints the transcript to stdout. To determine the
 * number of lines the transcript will take before printing, {@link transcriptHeightForWidth} may
 * be used.
 */
class Transcript {
    private final String studentName;
    private final String studentId;
    private final String semesterName;
    private final ClassResult[] classResults;

    public Transcript(
            String studentName,
            String studentId,
            String semesterName,
            ClassResult[] classResults) {
        this.studentName = studentName;
        this.studentId = studentId;
        this.semesterName = semesterName;
        this.classResults = classResults;
    }

    public String studentName() { return studentName; }
    public String studentId() { return studentId; }
    public String semesterName() { return semesterName; }
    public ClassResult[] classResults() { return classResults; }

    /**
     * Prints the transcript to stdout.
     *
     * We design the transcript with the following format:
     * 1. First, the student name, ID, and semester name are printed on separate lines and centered
     *    relative to the provided width.
     * 2. Next, each class result is printed on a separate line. In the case that a line will
     *    exceed the width limit, the word is shifted to the next line with four-space indentation:
     *    
     *        COMP3111
     *            Software Engineering
     *            4.00 4
     *
     * 3. Finally, the semester GPA is printed.
     *
     * @param width The preferred width for each line, although this may not be respected if an
     *              individual field is too long.
     */
    public void generateTranscriptForWidth(int width) {
        StringBuilder transcriptBuilder = new StringBuilder();

        // By finding the remaining half of the spaces, we center align the text.
        int spacesBeforeName = (width - studentName.length()) / 2;
        for (int i = 0; i < spacesBeforeName; ++i) {
            transcriptBuilder.append(" ");
        }
        transcriptBuilder.append(studentName);
        transcriptBuilder.append("\n");

        int spacesBeforeId = (width - studentId.length()) / 2;
        for (int i = 0; i < spacesBeforeId; ++i) {
            transcriptBuilder.append(" ");
        }
        transcriptBuilder.append(studentId);
        transcriptBuilder.append("\n");

        int spacesBeforeSemester = (width - semesterName.length()) / 2;
        for (int i = 0; i < spacesBeforeSemester; ++i) {
            transcriptBuilder.append(" ");
        }
        transcriptBuilder.append(semesterName);
        transcriptBuilder.append("\n");

        // Newline between header and transcript body.
        transcriptBuilder.append("\n");

        double totalGradePoints = 0.0;
        int totalCredits = 0;
        for (ClassResult result : classResults) {
            // Accumulate the total grade points for later display.
            totalGradePoints += result.gradePoints() * result.credits();
            totalCredits += result.credits();

            int currentLength = result.classCode().length();
            transcriptBuilder.append(result.classCode());

            // We add 1 to account for the space between the two fields, if on the same line.
            if (currentLength + 1 + result.className().length() > width) {
                // The current length is 4 because of the spaces added for indentation.
                transcriptBuilder.append("\n    ");
                currentLength = 4;
            } else {
                transcriptBuilder.append(" ");
                currentLength++;
            }
            
            transcriptBuilder.append(result.className());
            currentLength += result.className().length();

            String gradePointsString = String.format("%.2f", result.gradePoints());
            if (currentLength + 1 + gradePointsString.length() > width) {
                transcriptBuilder.append("\n    ");
                currentLength = 4;
            } else {
                transcriptBuilder.append(" ");
                currentLength++;
            }
            
            transcriptBuilder.append(gradePointsString);
            currentLength += gradePointsString.length();

            String creditsString = String.format("%d", result.credits());
            if (currentLength + 1 + creditsString.length() > width) {
                transcriptBuilder.append("\n    ");
                currentLength = 4;
            } else {
                transcriptBuilder.append(" ");
                currentLength++;
            }
            
            transcriptBuilder.append(creditsString);
            currentLength += creditsString.length();

            transcriptBuilder.append("\n");
        }
        
        // Newline between the transcript and the summary.
        transcriptBuilder.append("\n");

        transcriptBuilder.append(
                String.format("Semester GPA: %.2f", totalGradePoints / totalCredits));

        System.out.println(transcriptBuilder.toString());
    }

    /**
     * Returns the total number of lines required to display the transcript.
     *
     * For more information about the transcript format, see {@link generateTranscriptForWidth}.
     *
     * @param width The preferred width for each line.
     * @return The total number of lines required to display the transcript with respect to the
     *         {@code width} argument.
     */
    public int transcriptHeightForWidth(int width) {
        // The header consists of studentName, studentId, semesterName, and separating newline.
        int totalLines = 4;
        
        for (ClassResult result : classResults) {
            int currentLength = result.classCode().length();
            if (currentLength + 1 + result.className().length() > width) {
                totalLines++;

                // The current length is 4 after a newline is emitted to account for the
                // indentation on the new line.
                currentLength = 4;
            } else {
                currentLength++;
            }
            
            currentLength += result.className().length();

            String gradePointsString = String.format("%.2f", result.gradePoints());
            if (currentLength + 1 + gradePointsString.length() > width) {
                totalLines++;
                currentLength = 4;
            } else {
                currentLength++;
            }
            
            currentLength += gradePointsString.length();

            String creditsString = String.format("%d", result.credits());
            if (currentLength + 1 + creditsString.length() > width) {
                totalLines++;
                currentLength = 4;
            } else {
                currentLength++;
            }

            currentLength += creditsString.length();
            totalLines++;
        }
        
        // The footer consists of the line separating the transcript body and the line indicating
        // the semester GPA.
        totalLines += 2;
        return totalLines;
    }
}

public class RefactoringLab {
    public static void main(String[] args) {
        ClassResult[] classResults = new ClassResult[] {
            new ClassResult("COMP3111", "Software Engineering", 4.0, 4),
            new ClassResult("COMP3311", "Database Management Systems", 3.3, 3),
        };
        Transcript transcript = new Transcript("John Chan", "21039408", "2017F", classResults);
        transcript.generateTranscriptForWidth(20);
        System.out.println("Total lines: " + transcript.transcriptHeightForWidth(20));
    }
}