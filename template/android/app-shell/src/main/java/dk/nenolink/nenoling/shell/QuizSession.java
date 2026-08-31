package dk.nenolink.nenoling.shell;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import dk.nenolink.nenoling.content.AnswerOrder;
import dk.nenolink.nenoling.content.ContentModels.Answer;
import dk.nenolink.nenoling.content.ContentModels.Question;
import dk.nenolink.nenoling.content.ContentModels.Quiz;

/** Keeps one stable shuffled question order per quiz attempt and one stable answer order per presented question. */
public final class QuizSession {
    private final List<Question> questions;
    private final Random random;
    private int index;
    private int score;
    private boolean answered;
    private List<Answer> displayed = Collections.emptyList();

    public QuizSession(Quiz quiz) {
        this(quiz, new Random());
    }

    QuizSession(Quiz quiz, Random random) {
        if (quiz == null || quiz.questions.isEmpty()) throw new IllegalArgumentException("quiz with questions required");
        if (random == null) throw new IllegalArgumentException("random required");
        this.random = random;
        List<Question> shuffled = new ArrayList<>(quiz.questions);
        Collections.shuffle(shuffled, random);
        this.questions = Collections.unmodifiableList(shuffled);
        presentCurrent();
    }

    public Question question() { return questions.get(index); }
    public List<Answer> displayedAnswers() { return displayed; }
    public int questionNumber() { return index + 1; }
    public int totalQuestions() { return questions.size(); }
    public int score() { return score; }
    public boolean answered() { return answered; }
    public boolean finished() { return index >= questions.size(); }

    public boolean answer(Answer selected) {
        if (answered) return selected != null && selected.correct;
        answered = true;
        boolean correct = AnswerOrder.scoresCorrect(selected);
        if (correct) score++;
        return correct;
    }

    public boolean next() {
        if (!answered) return false;
        index++;
        if (index >= questions.size()) {
            displayed = Collections.emptyList();
            return false;
        }
        presentCurrent();
        return true;
    }

    private void presentCurrent() {
        answered = false;
        displayed = AnswerOrder.shuffleAnswers(questions.get(index).answers, random);
    }
}
