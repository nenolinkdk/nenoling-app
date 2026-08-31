package dk.nenolink.nenoling.content;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import dk.nenolink.nenoling.content.ContentModels.Answer;
import dk.nenolink.nenoling.content.ContentModels.TextPair;

public class AnswerOrderTest {
    @Test
    public void shufflePreservesIdentityAndCorrectFlag() {
        List<Answer> stored = sample();
        List<Answer> displayed = AnswerOrder.shuffleAnswers(stored, new Random(1));
        assertEquals(3, displayed.size());
        assertEquals(ids(stored), ids(displayed));
        assertNotSame(stored, displayed);
        assertEquals("answer.a", correctId(displayed));
    }

    @Test
    public void scoringUsesCorrectFlagNotPosition() {
        List<Answer> displayed = AnswerOrder.shuffleAnswers(sample(), new Random(42));
        int correct = AnswerOrder.indexOfCorrect(displayed);
        assertTrue(correct >= 0 && correct < 3);
        assertTrue(AnswerOrder.scoresCorrect(displayed.get(correct)));
        for (int i = 0; i < displayed.size(); i++) {
            if (i != correct) assertFalse(AnswerOrder.scoresCorrect(displayed.get(i)));
        }
    }

    @Test
    public void correctAnswerCanAppearInEveryPosition() {
        boolean[] seen = new boolean[3];
        for (int seed = 0; seed < 200; seed++) {
            List<Answer> displayed = AnswerOrder.shuffleAnswers(sample(), new Random(seed));
            seen[AnswerOrder.indexOfCorrect(displayed)] = true;
        }
        assertTrue(seen[0]);
        assertTrue(seen[1]);
        assertTrue(seen[2]);
    }

    private static List<Answer> sample() {
        return new ArrayList<>(Arrays.asList(
                new Answer("answer.a", new TextPair("one", "et"), true),
                new Answer("answer.b", new TextPair("two", "to"), false),
                new Answer("answer.c", new TextPair("three", "tre"), false)));
    }

    private static Set<String> ids(List<Answer> answers) {
        Set<String> ids = new HashSet<>();
        for (Answer answer : answers) ids.add(answer.id);
        return ids;
    }

    private static String correctId(List<Answer> answers) {
        for (Answer answer : answers) if (answer.correct) return answer.id;
        return null;
    }
}
