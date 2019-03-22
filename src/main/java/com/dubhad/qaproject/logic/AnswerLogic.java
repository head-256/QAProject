package com.dubhad.qaproject.logic;

import com.dubhad.qaproject.bean.AnswerBean;
import com.dubhad.qaproject.ProjectException;
import com.dubhad.qaproject.bean.UserBean;
import com.dubhad.qaproject.dao.*;
import com.dubhad.qaproject.entity.Answer;
import com.dubhad.qaproject.entity.Mark;
import com.dubhad.qaproject.entity.Question;
import com.dubhad.qaproject.entity.User;
import lombok.extern.log4j.Log4j2;

import java.time.Instant;
import java.util.Date;
import java.util.Optional;

/**
 * Class, that provides methods for answer commands
 */
@Log4j2
public class AnswerLogic extends AbstractLogic{
    private AnswerDao answerDao;
    private QuestionDao questionDao;
    private UserDao userDao;

    public AnswerLogic(){
        answerDao = TransactionHelper.getFactory().getAnswerDao();
        questionDao = TransactionHelper.getFactory().getQuestionDao();
        userDao = TransactionHelper.getFactory().getUserDao();
        getHelper().prepareDaos(userDao, answerDao, questionDao);
    }

    /**
     * Gets AnswerBean with specified id, and fulfilled main info
     * @param id id of answer
     * @return answer with fulfilled main info
     * @throws ProjectException if unexpected exception occurred
     */
    public AnswerBean get(long id) throws ProjectException{
        AnswerBean answerBean = null;
        Optional<Answer> answerOptional = answerDao.findEntityById(id);
        if(answerOptional.isPresent()){
            Answer answer = answerOptional.get();
            answerBean = new AnswerBean();
            answerBean.setId(answer.getId());
            answerBean.setLastEditDate(Date.from(answer.getLastEditDate()));
            answerBean.setText(answer.getText());
            answerBean.setDeleted(answer.isDeleted());
            answerBean.setQuestionId(answer.getQuestionId());
        }
        return answerBean;
    }

    /**
     * Deletes answer with specified id
     * @param id answer id
     * @return true, if answer was deleted, false otherwise
     * @throws ProjectException if answer with specified id can't be found or any exception in dao layer occurred
     */
    public boolean delete(long id) throws ProjectException{
        boolean result;
        Optional<Answer> answerOptional = answerDao.findEntityById(id);
        if(answerOptional.isPresent()){
            answerOptional.get().setDeleted(true);
            result = answerDao.update(answerOptional.get());
        }
        else{
            throw new ProjectException("Unable to find answer with id " + id);
        }
        return result;
    }

    /**
     * Edits answer with id, equals to id of passed entity, sets text to specified and last edit time to now
     * @param answerBean answer to fetch info from
     * @return true, if answer edited successfully, false otherwise
     * @throws ProjectException if answer with specified id can't be found or any exception in dao layer occurred
     */
    public boolean edit(AnswerBean answerBean) throws ProjectException {
        boolean result;
        Optional<Answer> answerOptional = answerDao.findEntityById(answerBean.getId());
        if(answerOptional.isPresent()){
            answerOptional.get().setText(answerBean.getText());
            answerOptional.get().setLastEditDate(Instant.now());
            result = answerDao.update(answerOptional.get());
        }
        else{
            throw new ProjectException("Unable to find answer with id " + answerBean.getId());
        }
        return result;
    }

    /**
     * Check if answer can be edited by current user: user must be an author of answer and ratings of it should be 0.
     * @param id id of answer
     * @param userBean current user
     * @return true, if answer edit is available, false otherwise
     * @throws ProjectException if answer or user with specified id can't be found or any exception in dao layer
     * occurred
     */
    public boolean answerEditAvailable(long id, UserBean userBean) throws ProjectException{
        boolean result;
        MarkDao markDao = TransactionHelper.getFactory().getMarkDao();
        getHelper().prepareDao(markDao);
        Optional<Answer> answerOptional = answerDao.findEntityById(id);
        Optional<User> userOptional = userDao.findEntityById(userBean.getId());
        if(answerOptional.isPresent() && userOptional.isPresent()){
            boolean sameUser = answerOptional.get().getAuthorId() == userOptional.get().getId();
            // mark dao is used with && to prevent calculating marks if user differs from required
            result = sameUser
                    && !answerOptional.get().isDeleted()
                    && markDao.getPositiveAnswerRating(id) == 0
                    && markDao.getNegativeAnswerRating(id) == 0;
        }
        else{
            throw new ProjectException("Unable to find answer with id " + id + ", or user with id " + userBean.getId());
        }
        return result;
    }

    /**
     * Creates answer with specified user id, question id and text
     * @param authorId id of author
     * @param questionId id of question
     * @param text text of answer
     * @throws ProjectException if specified user or question can't be found or unexpected error in dao layer occurred
     */
    public void createAnswer(long authorId, long questionId, String text) throws ProjectException {
        try{
            Optional<User> userOptional = userDao.findEntityById(authorId);
            Optional<Question> questionOptional = questionDao.findEntityById(questionId);
            if(!userOptional.isPresent()){
                throw new ProjectException("Can't find user: " + authorId);
            }
            if(!questionOptional.isPresent()){
                throw new ProjectException("Can't find question: " + questionId);
            }
            Answer answer = new Answer();
            answer.setAuthorId(authorId);
            answer.setQuestionId(questionId);
            answer.setText(text);
            answer.setCreationDate(Instant.now());
            answer.setLastEditDate(Instant.now());
            answer.setDeleted(false);
            answerDao.create(answer);
            getHelper().commit();
        }
        catch (ProjectException e){
            getHelper().rollback();
            throw e;
        }
    }

    /**
     * Rates answer by user with specified user and answer id, rate, and returns AnswerBean with current mark and
     * both positive and negative ratings
     * @param answerId id of answer to be rated
     * @param userId id of user, who put mark
     * @param rate rate, true if positive, false if negative
     * @return AnswerBean with current mark and both positive and negative ratings
     * @throws ProjectException if specified user or question can't be found or unexpected error in dao layer occurred
     */
    public AnswerBean rate(long answerId, long userId, boolean rate) throws ProjectException {
        MarkDao markDao = TransactionHelper.getFactory().getMarkDao();
        getHelper().prepareDao(markDao);
        getHelper().prepareDaos(markDao);
        AnswerBean answerBean;
        try{
            Optional<User> userOptional = userDao.findEntityById(userId);
            Optional<Answer> answerOptional = answerDao.findEntityById(answerId);
            if(!userOptional.isPresent()){
                throw new ProjectException("Unable to find user");
            }
            if(!answerOptional.isPresent()){
                throw new ProjectException("Unable to find answer");
            }
            Mark mark = new Mark();
            mark.setAnswerId(answerId);
            mark.setUserId(userId);
            mark.setValue(rate);
            int currentMark = markDao.putMark(mark);
            answerBean = new AnswerBean();
            answerBean.setCurrentUserMark(currentMark);
            answerBean.setPositiveRating(markDao.getPositiveAnswerRating(answerId));
            answerBean.setNegativeRating(markDao.getNegativeAnswerRating(answerId));
        }
        finally {
            getHelper().endTransaction();
        }
        return answerBean;
    }

    /**
     * Rates positive answer by user with specified user and answer id, rate, and returns AnswerBean with current mark
     * and both positive and negative ratings
     * @param answerId id of answer to be rated
     * @param userId id of user, who put mark
     * @return AnswerBean with current mark and both positive and negative ratings
     * @throws ProjectException if specified user or question can't be found or unexpected error in dao layer occurred
     */
    public AnswerBean ratePositive(long answerId, long userId) throws ProjectException {
        return rate(answerId, userId, true);
    }

    /**
     * Rates negative answer by user with specified user and answer id, rate, and returns AnswerBean with current mark
     * and both positive and negative ratings
     * @param answerId id of answer to be rated
     * @param userId id of user, who put mark
     * @return AnswerBean with current mark and both positive and negative ratings
     * @throws ProjectException if specified user or question can't be found or unexpected error in dao layer occurred
     */
    public AnswerBean rateNegative(long answerId, long userId) throws ProjectException {
        return rate(answerId, userId, false);
    }
}
