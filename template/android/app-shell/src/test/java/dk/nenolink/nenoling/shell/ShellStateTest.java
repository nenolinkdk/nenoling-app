package dk.nenolink.nenoling.shell;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ShellStateTest {
    @Test
    public void completeLearningFlowPreservesContext() {
        ShellState state = new ShellState();
        assertEquals(ShellState.Screen.MODULES, state.screen);

        state.lessons("level1");
        assertEquals(ShellState.Screen.LESSONS, state.screen);
        assertEquals("level1", state.moduleId);

        state.lesson("level1", "lesson3");
        assertEquals(ShellState.Screen.LESSON, state.screen);
        assertEquals("lesson3", state.lessonId);

        state.item(4);
        assertEquals(ShellState.Screen.ITEM, state.screen);
        assertEquals(4, state.itemIndex);

        state.quiz();
        assertEquals(ShellState.Screen.QUIZ, state.screen);
        assertEquals(0, state.questionIndex);

        state.quizQuestion(2);
        assertEquals(2, state.questionIndex);
        assertEquals("level1", state.moduleId);
        assertEquals("lesson3", state.lessonId);

        state.quizResult();
        assertEquals(ShellState.Screen.QUIZ_RESULT, state.screen);
        assertEquals("lesson3", state.lessonId);
    }

    @Test
    public void backNavigationCanRestoreLessonAndModuleLevels() {
        ShellState state = new ShellState();
        state.lesson("level2", "lesson7");
        state.item(9);

        state.lesson("level2", "lesson7");
        assertEquals(ShellState.Screen.LESSON, state.screen);
        assertEquals(0, state.itemIndex);

        state.lessons("level2");
        assertEquals(ShellState.Screen.LESSONS, state.screen);
        assertEquals("level2", state.moduleId);
        assertEquals("", state.lessonId);

        state.modules();
        assertEquals(ShellState.Screen.MODULES, state.screen);
        assertEquals("", state.moduleId);
        assertEquals("", state.lessonId);
    }

    @Test
    public void enteringNewLessonResetsItemAndQuizPosition() {
        ShellState state = new ShellState();
        state.lesson("level1", "lesson1");
        state.item(8);
        state.quizQuestion(2);

        state.lesson("level1", "lesson2");
        assertEquals(0, state.itemIndex);
        assertEquals(0, state.questionIndex);
        assertEquals("lesson2", state.lessonId);
    }

    @Test
    public void negativePositionsAreClampedToZero() {
        ShellState state = new ShellState();
        state.item(-4);
        assertEquals(0, state.itemIndex);
        state.quizQuestion(-3);
        assertEquals(0, state.questionIndex);
    }

    @Test
    public void resourcesAreSeparateWithoutDestroyingLearningContext() {
        ShellState state = new ShellState();
        state.lesson("level3", "lesson5");
        state.resources();
        assertEquals(ShellState.Screen.RESOURCES, state.screen);
        assertEquals("level3", state.moduleId);
        assertEquals("lesson5", state.lessonId);
    }
}
