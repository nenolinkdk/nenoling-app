package dk.nenolink.nenoling.shell;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
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
    public void displayedAnswerOrderIsStableUntilNextQuestion() {
        QuizSession session = new QuizSession(quiz(), new Random(3));
        List<Answer> first = session.displayedAnswers();
        assertSame(first, session.displayedAnswers());
        Answer correct = correct(first);
        assertTrue(session.answer(correct));
        assertSame(first, session.displayedAnswers());
        assertEquals(1, session.score());
        assertTrue(session.next());
        assertEquals(2, session.questionNumber());
        assertFalse(session.answered());
    }

    @Test
    public void questionOrderIsDeterministicForSeedAndStableDuringAttempt() {
        QuizSession first = new QuizSession(quiz(), new Random(7));
        QuizSession second = new QuizSession(quiz(), new Random(7));

        assertEquals(first.question().id, second.question().id);
        String currentId = first.question().id;
        first.displayedAnswers();
        first.displayedAnswers();
        assertEquals(currentId, first.question().id);
    }

    @Test
    public void differentAttemptSeedsCanProduceDifferentQuestionOrder() {
        QuizSession first = new QuizSession(quiz(), new Random(1));
        QuizSession second = new QuizSession(quiz(), new Random(2));
        assertNotEquals(questionSequence(first), questionSequence(second));
    }

    @Test
    public void scoringDependsOnAnswerIdentityNotDisplayPosition() {
        for (int seed = 0; seed < 12; seed++) {
            QuizSession session = new QuizSession(singleQuestionQuiz(), new Random(seed));
            List<Answer> displayed = session.displayedAnswers();
            Answer correct = correct(displayed);
            assertTrue(session.answer(correct));
            assertEquals(1, session.score());
        }
    }

    @Test
    public void duplicateAnswerSubmissionDoesNotIncreaseScore() {
        QuizSession session = new QuizSession(singleQuestionQuiz(), new Random(4));
        Answer correct = correct(session.displayedAnswers());
        assertTrue(session.answer(correct));
        assertTrue(session.answer(correct));
        assertEquals(1, session.score());
    }

    @Test
    public void cannotAdvanceBeforeAnswering() {
        QuizSession session = new QuizSession(quiz(), new Random(5));
        String current = session.question().id;
        assertFalse(session.next());
        assertEquals(current, session.question().id);
        assertEquals(1, session.questionNumber());
    }

    private String questionSequence(QuizSession session) {
        StringBuilder ids = new StringBuilder();
        while (true) {
            if (ids.length() > 0) ids.append(',');
            ids.append(session.question().id);
            session.answer(correct(session.displayedAnswers()));
            if (!session.next()) return ids.toString();
        }
    }

    private Answer correct(List<Answer> answers) {
        for (Answer answer : answers) if (answer.correct) return answer;
        throw new AssertionError("test question has no correct answer");
    }

    private Quiz quiz() {
        return new Quiz("quiz.test", new TextPair("Quiz", "Quiz"), Arrays.asList(
                question("q1", 1), question("q2", 2), question("q3", 3), question("q4", 4), question("q5", 5)));
    }

    private Quiz singleQuestionQuiz() {
        return new Quiz("quiz.single", new TextPair("Quiz", "Quiz"), Collections.singletonList(question("q1", 1)));
    }

    private Question question(String id, int order) {
        return new Question(id, order, "single-choice", "support",
                new TextPair("Prompt " + id, "Prompt " + id), Arrays.asList(
                new Answer(id + ".a", new TextPair("A", "A"), true),
                new Answer(id + ".b", new TextPair("B", "B"), false),
                new Answer(id + ".c", new TextPair("C", "C"), false)),
                new TextPair("Explanation", "Explanation"), Collections.emptyList());
    }
}
