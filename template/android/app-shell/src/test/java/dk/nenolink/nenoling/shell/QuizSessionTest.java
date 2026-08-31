package dk.nenolink.nenoling.shell;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import dk.nenolink.nenoling.content.ContentModels.Answer;
import dk.nenolink.nenoling.content.ContentModels.Question;
import dk.nenolink.nenoling.content.ContentModels.Quiz;
import dk.nenolink.nenoling.content.ContentModels.TextPair;

public class QuizSessionTest {
    @Test
    public void displayedOrderIsStableUntilNextQuestion() {
        QuizSession session = new QuizSession(quiz(), new Random(3));
        List<Answer> first = session.displayedAnswers();
        assertSame(first, session.displayedAnswers());
        Answer correct = null;
        for (Answer answer : first) if (answer.correct) correct = answer;
        assertTrue(session.answer(correct));
        assertEquals(1, session.score());
        assertTrue(session.next());
        assertEquals(2, session.questionNumber());
        assertFalse(session.answered());
    }

    private Quiz quiz() {
        return new Quiz("quiz.test", new TextPair("Quiz", "Quiz"), Arrays.asList(question("q1"), question("q2")));
    }

    private Question question(String id) {
        return new Question(id, "q1".equals(id) ? 1 : 2, "single-choice", "support",
                new TextPair("Prompt", "Prompt"), Arrays.asList(
                new Answer(id + ".a", new TextPair("A", "A"), true),
                new Answer(id + ".b", new TextPair("B", "B"), false),
                new Answer(id + ".c", new TextPair("C", "C"), false)),
                new TextPair("Explanation", "Explanation"), Collections.emptyList());
    }
}
