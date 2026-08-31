package dk.nenolink.nenoling.content;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;

import dk.nenolink.nenoling.content.ContentModels.Answer;
import dk.nenolink.nenoling.content.ContentModels.Question;
import dk.nenolink.nenoling.content.ContentModels.TextPair;

public class QuizIntegrityTest {
    @Test
    public void displayRoleShowsOneLanguageOnly() throws Exception {
        Answer answer = new Answer("answer.one", new TextPair("conditions", "betingelser"), true);
        Question question = new Question("question.one", 1, "single-choice", "support",
                new TextPair("Meaning?", "Betydning?"), Arrays.asList(answer,
                new Answer("answer.two", new TextPair("turnover", "omsætning"), false)),
                new TextPair("Explanation", "Forklaring"), Collections.emptyList());
        String shown = QuizIntegrity.displayedAnswerText(question, answer);
        assertEquals("conditions", shown);
        assertFalse(QuizIntegrity.leaksBothRoles(answer.text, shown));
    }
}
