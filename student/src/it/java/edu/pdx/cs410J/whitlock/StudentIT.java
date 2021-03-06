package edu.pdx.cs410J.whitlock;

import edu.pdx.cs410J.InvokeMainTestCase;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.StringContains.containsString;

/**
 * Integration tests for the <code>Student</code> class's main method.
 * These tests extend <code>InvokeMainTestCase</code> which allows them
 * to easily invoke the <code>main</code> method of <code>Student</code>.
 */
public class StudentIT extends InvokeMainTestCase {
  @Test
  public void invokingMainWithNoArgumentsHasExitCodeOf1() {
    InvokeMainTestCase.MainMethodResult result = invokeMain(Student.class);
    assertThat(result.getExitCode(), equalTo(1));
  }

  @Test
  public void invokingMainWithNoArgumentsPrintsMissingArgumentsToStandardError() {
    InvokeMainTestCase.MainMethodResult result = invokeMain(Student.class);
    assertThat(result.getErr(), containsString("Missing command line arguments"));
  }

  @Test
  public void missingGenderPrintsError() {
    MainMethodResult result = invokeMain(Student.class, "Student");
    assertThat(result.getErr(), containsString("Missing gender"));
    assertThat(result.getExitCode(), equalTo(1));
  }

  @Test
  public void missingGPAPrintsError() {
    MainMethodResult result = invokeMain(Student.class, "Student", "Doesn't matter");
    assertThat(result.getErr(), containsString("Missing GPA"));
    assertThat(result.getExitCode(), equalTo(1));
  }

  @Test
  public void studentTakingNoClassIsOkay() {
    MainMethodResult result = invokeMain(Student.class, "Student", "female", "1.23");
    assertThat(result.getOut(), containsString("Student has a GPA of 1.23 and is taking 0 classes."));
    assertThat(result.getExitCode(), equalTo(0));
  }

  @Test
  public void invalidGPAPrintsError() {
    String gpa = "BADGPA";
    MainMethodResult result = invokeMain(Student.class, "Student", "female", gpa);
    assertThat(result.getErr(), containsString("Invalid GPA: " + gpa));
    assertThat(result.getExitCode(), equalTo(1));
  }

  @Test
  public void studentTakingOneClass() {
    MainMethodResult result = invokeMain(Student.class, "Student", "female", "1.23", "Java");
    assertThat(result.getOut(), containsString("Student has a GPA of 1.23 and is taking 1 class: Java."));
    assertThat(result.getExitCode(), equalTo(0));
  }

  @Test
  public void invalidGenderPrintsError() {
    String gender = "BADGENDER";
    MainMethodResult result = invokeMain(Student.class, "Student", gender, "3.45");
    assertThat(result.getErr(), containsString("Invalid Gender: " + gender));
    assertThat(result.getExitCode(), equalTo(1));
  }

  @Test
  public void endToEndIntegrationTest() {
    MainMethodResult result = invokeMain(Student.class, "Dave", "male", "3.64", "Algorithms", "Operating Systems", "Java");
    String expected = "Dave has a GPA of 3.64 and is taking 3 classes: Algorithms, Operating " +
      "Systems, and Java. He says \"This class is too much work\".";
    assertThat(result.getOut(), containsString(expected));
    assertThat(result.getExitCode(), equalTo(0));
  }

}
