import com.google.testing.compile.JavaFileObjects;
import com.yheriatovych.reductor.processor.ReductorAnnotationProcessor;
import org.junit.Test;

import javax.tools.JavaFileObject;

import static com.google.common.truth.Truth.assertAbout;
import static com.google.testing.compile.JavaSourceSubjectFactory.javaSource;

public class ActionCreatorValidateReducerTest {
    @Test
    public void testSimpleReducerValidation() {
        JavaFileObject source = JavaFileObjects.forSourceString("test.Foobar", "package test;\n" +
                "\n" +
                "import com.yheriatovych.reductor.Action;\n" +
                "import com.yheriatovych.reductor.Reducer;\n" +
                "import com.yheriatovych.reductor.annotations.ActionCreator;\n" +
                "import com.yheriatovych.reductor.annotations.AutoReducer;\n" +
                "\n" +
                "public class Foobar {\n" +
                "    @ActionCreator\n" +
                "    public interface TestCreator {\n" +
                "        @ActionCreator.Action(\"TEST\")\n" +
                "        Action testAction(int foo, String bar, Object baz);\n" +
                "    }\n" +
                "    \n" +
                "    @AutoReducer\n" +
                "    public static abstract class TestReducer implements Reducer<String> {\n" +
                "        @AutoReducer.Action(value = \"TEST\", from = TestCreator.class)\n" +
                "        String handleTest(String state, int foo, String bar, Object baz) {\n" +
                "            return state;\n" +
                "        }\n" +
                "    }\n" +
                "}");

        assertAbout(javaSource()).that(source)
                .withCompilerOptions("-Xlint:-processing")
                .processedWith(new ReductorAnnotationProcessor())
                .compilesWithoutError();
    }
}
