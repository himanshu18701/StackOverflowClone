package com.himanshu.stackoverflow.service;

import com.himanshu.stackoverflow.repository.AnswerRepository;
import com.himanshu.stackoverflow.repository.QuestionRepository;
import com.himanshu.stackoverflow.repository.UserAnswerRepository;
import com.himanshu.stackoverflow.repository.UserRepository;
import com.himanshu.stackoverflow.entity.Answer;
import com.himanshu.stackoverflow.entity.Question;
import com.himanshu.stackoverflow.entity.User;
import com.himanshu.stackoverflow.event.AnswerAddedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AnswerServiceImpl implements AnswerService {

    private final AnswerRepository answerRepository;
    private final QuestionRepository questionRepository;
    private final UserRepository userRepository;
    private final UserAnswerRepository userAnswerRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final AiCheckerService aiCheckerService;
    private final PlagiarismCheckerService plagiarismCheckerService;

    @Autowired
    public AnswerServiceImpl(AnswerRepository answerRepository,
                             QuestionRepository questionRepository,
                             UserRepository userRepository,
                             UserAnswerRepository userAnswerRepository,
                             ApplicationEventPublisher eventPublisher, PlagiarismCheckerService plagiarismCheckerService) {
        this.answerRepository = answerRepository;
        this.questionRepository = questionRepository;
        this.userRepository = userRepository;
        this.eventPublisher = eventPublisher;
        this.userAnswerRepository = userAnswerRepository;
        this.aiCheckerService = new AiCheckerService();
        this.plagiarismCheckerService = plagiarismCheckerService;
    }

    @Override
    public List<Answer> findAllAnswers(int questionId) {
        Question question = questionRepository.findById(questionId).orElse(null);
        return question.getAnswers();
    }

    @Override
    public Answer findAnswerById(int id) {
        return answerRepository.findById(id).orElse(null);
    }

    @Override
    public void deleteAnswer(int id) {
        answerRepository.deleteById(id);
    }


    @Override
    public ResponseEntity<String> saveOrUpdateAnswer(Answer answer, int questionId) {
        Question question = questionRepository.findById(questionId).orElse(null);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Answer existingAnswer = findAnswerById(answer.getId());
        User user = userRepository.findByEmail(auth.getName());
        answer.setUser(user);
        answer.setQuestion(question);
        if(existingAnswer == null) {
            answer.setCreatedAt(LocalDateTime.now());
        }else{
            answer.setCreatedAt(existingAnswer.getCreatedAt());
        }
        answer.setUpdatedAt(LocalDateTime.now());
        String text=extractTextFromHtml(answer.getContent());
        int plagScore = plagiarismCheckerService.checkPlagiarism(text);
        double aiScore = aiCheckerService.checkAi(text)*100;
        if(plagScore>30 ||  aiScore>40) {
            return new ResponseEntity<>("Error", HttpStatus.NOT_ACCEPTABLE);
        }
        answerRepository.save(answer);
        eventPublisher.publishEvent(new AnswerAddedEvent(this, question, answer));
        return new ResponseEntity<>("Success", HttpStatus.OK);
    }

    private String extractTextFromHtml(String html) {
        String plainText = html.replaceAll("<[^>]*>", "");
        return plainText.trim();
    }

    @Override
    public void updateVotes(int answerId) {
        Answer answer = answerRepository.findById(answerId).orElse(null);
        int votes = userAnswerRepository.getVotes(answerId, true);
        votes = votes - userAnswerRepository.getVotes(answerId, false);
        answer.setVotes(votes);
        answerRepository.save(answer);
    }
}
