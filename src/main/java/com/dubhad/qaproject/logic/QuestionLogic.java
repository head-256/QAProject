package com.dubhad.qaproject.logic;

import com.dubhad.qaproject.bean.AnswerBean;
import com.dubhad.qaproject.bean.QuestionBean;
import com.dubhad.qaproject.bean.TagBean;
import com.dubhad.qaproject.bean.UserBean;
import com.dubhad.qaproject.dao.*;
import com.dubhad.qaproject.ProjectException;
import com.dubhad.qaproject.entity.*;
import lombok.extern.log4j.Log4j2;

import java.time.Instant;
import java.util.*;

/**
 * Class, that provides methods for question commands
 */
@SuppressWarnings("Duplicates")
@Log4j2
public class QuestionLogic extends AbstractLogic{
    private static final String TAG_AGGREGATOR_DELIMITER = ", ";
    private UserDao userDao;
    private QuestionDao questionDao;

    public QuestionLogic(){
        userDao = TransactionHelper.getFactory().getUserDao();
        questionDao = TransactionHelper.getFactory().getQuestionDao();
        getHelper().prepareDaos(userDao, questionDao);
    }

    /**
     * Edits fields, allowed for admin edit, in specified question and sets last edit date to now
     * @param questionBean question to edit
     * @throws ProjectException if any unexpected error occurred
     */
    public void editQuestionAdmin(QuestionBean questionBean) throws ProjectException {
        editQuestion(questionBean, false);
    }

    /**
     * Edits all fields in specified question and sets last edit date to now
     * @param questionBean question to edit
     * @throws ProjectException if any unexpected error occurred
     */
    public void editQuestion(QuestionBean questionBean) throws ProjectException {
        editQuestion(questionBean, true);
    }

    /**
     * Edits all fields in specified question and sets last edit date to now
     * @param questionBean question to edit
     * @param changeText flag, indicates, if text update is required. Should be false, if edit doesn't allow to change
     * text
     * @throws ProjectException if any unexpected error occurred
     */
    private void editQuestion(QuestionBean questionBean, boolean changeText) throws ProjectException {
        TagDao tagDao = TransactionHelper.getFactory().getTagDao();
        MarkDao markDao = TransactionHelper.getFactory().getMarkDao();
        getHelper().prepareDaos(tagDao, markDao);
        try{
            Optional<Question> questionOptional = questionDao.findEntityById(questionBean.getId());
            if(questionOptional.isPresent()){
                Optional<User> user = userDao.findEntityById(questionOptional.get().getUserId());
                if(!user.isPresent()){
                    throw new ProjectException("Unable to find user");
                }
                Question question = questionOptional.get();
                if(changeText) {
                    question.setText(questionBean.getText());
                }
                question.setTitle(questionBean.getTitle());
                question.setLastEditDate(Instant.now());
                questionDao.update(question);
                questionBean.setStatus(question.getStatus());
                questionBean.setLastEditDate(Date.from(question.getLastEditDate()));
                questionDao.deleteTagConnections(question);
                for(TagBean tagBean: questionBean.getTags()){
                    Tag tag = new Tag();
                    tag.setText(tagBean.getText());
                    tagDao.initTagIdByText(tag);
                    questionDao.createQuestionTagConnection(question, tag.getId());
                    tagBean.setId(tag.getId());
                }
                populateUser(questionBean, user.get().getId(), markDao);
            }
            else{
                throw new ProjectException("Unable to find question");
            }
            getHelper().commit();
            log.debug("Question edited successfully");
        }
        catch (ProjectException e){
            getHelper().rollback();
            throw e;
        }
    }

    /**
     * Check, whether specified user has rights to edit question with specified id
     * @param id id of the question
     * @param userBean user to check
     * @return true, if user has rights to edit question, false otherwise
     * @throws ProjectException if question with specified id can't be found
     */
    public boolean hasEditRights(long id, UserBean userBean) throws ProjectException {
        boolean result = false;
        Optional<Question> questionOptional = questionDao.findEntityById(id);
        if(questionOptional.isPresent()){
            if(questionOptional.get().getUserId() == userBean.getId()){
                result = true;
            }
        }
        else{
            throw new ProjectException("Unable to find question with id " + id);
        }
        return result;
    }

    /**
     * Checks, whether specified user has rights to close question with specified id
     * @param id question id
     * @param userBean user to check
     * @return true, if user has rights to close question, false otherwise
     * @throws ProjectException if question or user with specified id can't be found
     */
    public boolean hasCloseRights(long id, UserBean userBean) throws ProjectException {
        boolean result = false;
        Optional<Question> questionOptional = questionDao.findEntityById(id);
        Optional<User> userOptional = userDao.findEntityById(userBean.getId());
        if(questionOptional.isPresent() && userOptional.isPresent()){
            Question question = questionOptional.get();
            User user = userOptional.get();
            if(question.getUserId() == userBean.getId() || user.getRole().ordinal() >= UserRole.ADMIN.ordinal()){
                result = true;
            }
        }
        else{
            throw new ProjectException("Unable to find question with id " + id + " or user with id " + userBean.getId());
        }
        return result;
    }

    /**
     * Check, if any actions with answers to specified question are available
     * @param id question id
     * @return true, if actions are available, false otherwise
     * @throws ProjectException if question with specified id can't be found
     */
    public boolean answerActionAvailable(long id) throws ProjectException{
        return isQuestionOpen(id);
    }

    /**
     * Check, if any question is open
     * @param id question id
     * @return true, if question is open, false otherwise
     * @throws ProjectException if question with specified id can't be found
     */
    private boolean isQuestionOpen(long id) throws ProjectException {
        boolean result = false;
        Optional<Question> questionOptional = questionDao.findEntityById(id);
        if(questionOptional.isPresent()){
            if(questionOptional.get().getStatus() == QuestionStatus.OPEN){
                result = true;
            }
        }
        else{
            throw new ProjectException("Unable to find question with id " + id);
        }
        return result;
    }

    /**
     * Check, if any question can be viewed by specified user
     * @param id question id
     * @param userBean user to check permission
     * @return true, if question can be viewed, false otherwise
     * @throws ProjectException if question or user with specified id can't be found
     */
    public boolean questionViewAvailable(long id, UserBean userBean) throws ProjectException {
        boolean result = true;
        Optional<Question> questionOptional = questionDao.findEntityById(id);
        if(questionOptional.isPresent()){
            Question question = questionOptional.get();
            if(question.getStatus() == QuestionStatus.DELETED){
                if(userBean == null){
                    result = false;
                }
                else{
                    Optional<User> userOptional = userDao.findEntityById(userBean.getId());
                    if(userOptional.isPresent()){
                        if(userOptional.get().getRole().ordinal() < UserRole.ADMIN.ordinal()){
                            result = false;
                        }
                    }
                    else{
                        throw new ProjectException("Unable to find user with id " + userBean.getId());
                    }
                }
            }
        }
        else{
            throw new ProjectException("Unable to find question with id " + id);
        }
        return result;
    }

    /**
     * Closes specified question
     * @param id id of question
     * @throws ProjectException if question is already deleted or can't be found
     */
    public void closeQuestion(long id) throws ProjectException {
        Optional<Question> questionOptional = questionDao.findEntityById(id);
        if(questionOptional.isPresent()){
            if(questionOptional.get().getStatus() == QuestionStatus.OPEN){
                questionOptional.get().setStatus(QuestionStatus.CLOSED);
                questionDao.update(questionOptional.get());
            }
            else if(questionOptional.get().getStatus() == QuestionStatus.DELETED){
                throw new ProjectException("Unable to close deleted question");
            }
        }
        else{
            throw new ProjectException("Unable to find question with id " + id);
        }
    }

    /**
     * Deletes question
     * @param id id of question
     * @throws ProjectException if question can't be found
     */
    public void deleteQuestion(long id) throws ProjectException {
        AnswerDao answerDao = TransactionHelper.getFactory().getAnswerDao();
        getHelper().prepareDao(answerDao);
        Optional<Question> questionOptional = questionDao.findEntityById(id);
        try {
            if (questionOptional.isPresent()) {
                questionOptional.get().setStatus(QuestionStatus.DELETED);
                questionDao.update(questionOptional.get());
            } else {
                throw new ProjectException("Unable to find question with id " + id);
            }
            for (Answer answer : answerDao.findAnswersByQuestionId(id)) {
                answer.setDeleted(true);
                answerDao.update(answer);
            }
            getHelper().commit();
        }
        catch (ProjectException e){
            getHelper().rollback();
            throw e;
        }
    }

    /**
     * Creates question from specified bean
     * @param questionBean question bean to fetch info from
     * @param currentUser author of question
     * @throws ProjectException if user can't be found
     */
    public void createQuestion(QuestionBean questionBean, UserBean currentUser)
            throws ProjectException {
        TagDao tagDao = TransactionHelper.getFactory().getTagDao();
        MarkDao markDao = TransactionHelper.getFactory().getMarkDao();
        getHelper().prepareDaos(tagDao, markDao);
        try{
            Optional<User> user = userDao.findEntityById(currentUser.getId());
            if(!user.isPresent()){
                throw new ProjectException("Unable to find user");
            }
            Question question = new Question();
            question.setText(questionBean.getText());
            question.setTitle(questionBean.getTitle());
            question.setCreationDate(Instant.now());
            question.setLastEditDate(Instant.now());
            question.setStatus(QuestionStatus.OPEN);
            question.setUserId(user.get().getId());
            questionDao.create(question);
            questionBean.setId(question.getId());
            questionBean.setStatus(question.getStatus());
            questionBean.setLastEditDate(Date.from(question.getLastEditDate()));
            for(TagBean tagBean: questionBean.getTags()){
                Tag tag = new Tag();
                tag.setText(tagBean.getText());
                tagDao.initTagIdByText(tag);
                questionDao.createQuestionTagConnection(question, tag.getId());
                tagBean.setId(tag.getId());
            }
            populateUser(questionBean, user.get().getId(), markDao);
            getHelper().commit();
            log.debug("Question created successfully");
        }
        catch (ProjectException e){
            getHelper().rollback();
            throw e;
        }
    }

    /**
     * Gets list of all users questions with permissions of that user
     * @param user user to get questions
     * @return list of all found questions
     * @throws ProjectException if user can't be found
     */
    public List<QuestionBean> getUserQuestions(UserBean user) throws ProjectException {
        return getUserQuestions(user.getId(), user);
    }

    /**
     * Gets list of all users questions with permissions of that current user
     * @param id id of user to get questions
     * @param currentUser user to get permissions from
     * @return list of all found questions
     * @throws ProjectException if unexpected exception is thrown
     */
    public List<QuestionBean> getUserQuestions(long id, UserBean currentUser) throws ProjectException{
        AnswerDao answerDao = TransactionHelper.getFactory().getAnswerDao();
        TagDao tagDao = TransactionHelper.getFactory().getTagDao();
        MarkDao markDao = TransactionHelper.getFactory().getMarkDao();
        getHelper().prepareDaos(answerDao, tagDao, markDao);
        HashMap<Long, UserBean> users = new HashMap<>();
        List<QuestionBean> questions = new ArrayList<>();
        List<Question> questionsLoaded;
        if(currentUser != null && currentUser.getPrivilegeLevel() >= 1) {
            questionsLoaded = questionDao.findQuestionsByUserId(id);
        }
        else{
            questionsLoaded = questionDao.findNotDeletedQuestionsByUserId(id);
        }
        for(Question questionLoaded: questionsLoaded){
            QuestionBean question = new QuestionBean();
            if(users.containsKey(questionLoaded.getUserId())) {
                question.setUser(users.get(questionLoaded.getUserId()));
            }
            else {
                populateUser(question, questionLoaded.getUserId(), markDao);
                users.put(question.getUser().getId(), question.getUser());
            }
            //No text, no tags, no attachments
            question.setId(questionLoaded.getId());
            question.setTitle(questionLoaded.getTitle());
            question.setLastEditDate(Date.from(questionLoaded.getLastEditDate()));
            question.setStatus(questionLoaded.getStatus());
            if(currentUser != null && currentUser.getPrivilegeLevel() >= 1) {
                question.setAnswersCount(answerDao.getAnswersCountByQuestionId(questionLoaded.getId()));
            }
            else {
                question.setAnswersCount(answerDao.getNotDeletedAnswersCountByQuestionId(questionLoaded.getId()));
            }
            questions.add(question);
        }
        return questions;
    }

    /**
     * Counts questions, that match pattern, specified in passed parameter, with privileges of current user
     * @param questionBean question to fetch pattern from
     * @param currentUser user to fetch privileges from
     * @return count of questions, that match pattern
     * @throws ProjectException if any unexpected error occurred
     */
    public long getCount(QuestionBean questionBean, UserBean currentUser) throws ProjectException{
        long count;
        if(questionBean.getTitle() != null || !questionBean.getTags().isEmpty()) {
            LinkedList<Tag> tags = new LinkedList<>();
            for (TagBean tagBean : questionBean.getTags()) {
                Tag tag = new Tag();
                tag.setText(tagBean.getText());
                tags.add(tag);
            }
            if(currentUser != null && currentUser.getPrivilegeLevel() >= 1) {
                count = questionDao.getQuestionsCount(questionBean.getTitle(), tags);
            }
            else{
                count = questionDao.getNotDeletedQuestionsCount(questionBean.getTitle(), tags);
            }
        }
        else{
            count = questionDao.getCount();
        }
        return count;
    }

    /**
     * Gets page of specified size of questions, that match pattern, specified in passed parameter,
     * with privileges of current user
     * @param questionBean question to fetch pattern from
     * @param currentUser user to fetch privileges from
     * @param offset offset of questions slice
     * @param limit size of questions slice
     * @return count of questions, that match pattern
     * @throws ProjectException if any unexpected error occurred
     */
    public List<QuestionBean> getQuestionsPage(long offset, int limit, QuestionBean questionBean, UserBean currentUser)
            throws ProjectException{
        HashMap<Long, UserBean> users = new HashMap<>();
        AnswerDao answerDao = TransactionHelper.getFactory().getAnswerDao();
        TagDao tagDao = TransactionHelper.getFactory().getTagDao();
        MarkDao markDao = TransactionHelper.getFactory().getMarkDao();
        getHelper().prepareDaos(answerDao, tagDao, markDao);
        List<QuestionBean> questions = new ArrayList<>();
        List<Question> questionsLoaded;
        if(questionBean.getTitle() != null  || !questionBean.getTags().isEmpty()) {
            LinkedList<Tag> tags = new LinkedList<>();
            for(TagBean tagBean: questionBean.getTags()){
                Tag tag = new Tag();
                tag.setText(tagBean.getText());
                tags.add(tag);
            }
            if(currentUser != null && currentUser.getPrivilegeLevel() >= 1) {
                questionsLoaded = questionDao.findLatestQuestions(offset, limit, questionBean.getTitle(), tags);
            }
            else{
                questionsLoaded = questionDao.findLatestNotDeletedQuestions(offset, limit, questionBean.getTitle(), tags);
            }
        }
        else {
            if(currentUser != null && currentUser.getPrivilegeLevel() >= 1) {
                questionsLoaded = questionDao.findLatestQuestions(offset, limit);
            }
            else{
                questionsLoaded = questionDao.findLatestNotDeletedQuestions(offset, limit);
            }
        }
        for(Question questionLoaded: questionsLoaded){
            QuestionBean question = copyQuestion(questionLoaded);
            if(users.containsKey(questionLoaded.getUserId())) {
                question.setUser(users.get(questionLoaded.getUserId()));
            }
            else {
                populateUser(question, questionLoaded.getUserId(), markDao);
                users.put(question.getUser().getId(), question.getUser());
            }
            populateTags(question, tagDao);
            if(currentUser != null && currentUser.getPrivilegeLevel() >= 1) {
                question.setAnswersCount(answerDao.getAnswersCountByQuestionId(questionLoaded.getId()));
            }
            else {
                question.setAnswersCount(answerDao.getNotDeletedAnswersCountByQuestionId(questionLoaded.getId()));
            }
            questions.add(question);
        }
        return questions;
    }

    /**
     * Gets filled question with specified id
     * @param questionId id of question
     * @param currentUser current user
     * @return found question
     * @throws ProjectException if question can't be found
     */
    public QuestionBean getQuestionFull(long questionId, UserBean currentUser) throws ProjectException{
        AnswerDao answerDao = TransactionHelper.getFactory().getAnswerDao();
        TagDao tagDao = TransactionHelper.getFactory().getTagDao();
        MarkDao markDao = TransactionHelper.getFactory().getMarkDao();
        getHelper().prepareDaos(answerDao, tagDao, markDao);
        QuestionBean question;
        Optional<Question> questionOptional = questionDao.findEntityById(questionId);
        if(questionOptional.isPresent()) {
            question = copyQuestion(questionOptional.get());
            populateUser(question, questionOptional.get().getUserId(), markDao);
            boolean showDeletedAnswers = currentUser != null && currentUser.getPrivilegeLevel() >= 1;
            populateAnswers(question, answerDao, markDao, showDeletedAnswers);
            populateTags(question, tagDao);
            StringBuilder tagsAggregated = new StringBuilder();
            for(int i = 0; i < question.getTags().size(); ++i){
                TagBean tagBean = question.getTags().get(i);
                if(i > 0){
                    tagsAggregated.append(TAG_AGGREGATOR_DELIMITER);
                }
                tagsAggregated.append(tagBean.getText());
            }
            question.setTagsAggregated(tagsAggregated.toString());
            // populating current user's marks
            if(currentUser != null) {
                for (AnswerBean answerBean : question.getAnswers()) {
                    Optional<Mark> mark = markDao.findEntityByUserAndAnswerId(currentUser.getId(), answerBean.getId());
                    if (mark.isPresent()) {
                        answerBean.setCurrentUserMark(mark.get().isValue() ? 1 : -1);
                    }
                }
            }
        }
        else{
            throw new ProjectException("Unable to find question with id " + questionId);
        }
        return question;
    }

    private QuestionBean copyQuestion(Question question){
        QuestionBean questionBean = new QuestionBean();
        questionBean.setId(question.getId());
        questionBean.setTitle(question.getTitle());
        questionBean.setText(question.getText());
        questionBean.setStatus(question.getStatus());
        questionBean.setLastEditDate(Date.from(question.getLastEditDate()));
        return questionBean;
    }

    private void populateTags(QuestionBean question, TagDao tagDao) throws ProjectException {
        List<Tag> tagsDao = tagDao.findTagsByQuestionId(question.getId());
        for (Tag t : tagsDao) {
            TagBean tag = new TagBean();
            tag.setId(t.getId());
            tag.setText(t.getText());
            question.getTags().add(tag);
        }
    }

    private void populateAnswers(QuestionBean question, AnswerDao answerDao, MarkDao markDao, boolean addDeleted)
            throws ProjectException {
        HashMap<Long, UserBean> users = new HashMap<>();
        List<Answer> answersDao;
        if(addDeleted){
            answersDao = answerDao.findAnswersByQuestionId(question.getId());
        }
        else{
            answersDao = answerDao.findNotDeletedAnswersByQuestionId(question.getId());
        }
        for(Answer answer : answersDao){
            AnswerBean answerBean = new AnswerBean();
            answerBean.setId(answer.getId());
            answerBean.setLastEditDate(Date.from(answer.getLastEditDate()));
            answerBean.setText(answer.getText());
            long positiveRating = markDao.getPositiveAnswerRating(answer.getId());
            long negativeRating = markDao.getNegativeAnswerRating(answer.getId());
            answerBean.setPositiveRating(positiveRating);
            answerBean.setNegativeRating(negativeRating);
            answerBean.setDeleted(answer.isDeleted());
            question.getAnswers().add(answerBean);

            if(users.containsKey(answer.getAuthorId())) {
                answerBean.setUser(users.get(answer.getAuthorId()));
            }
            else{
                Optional<User> userOptional = userDao.findEntityById(answer.getAuthorId());
                if(userOptional.isPresent()){
                    User user = userOptional.get();
                    UserBean userBean = new UserBean();
                    userBean.setAvatarPath(user.getAvatarPath());
                    userBean.setId(user.getId());
                    userBean.setUsername(user.getUsername());
                    userBean.setPositiveRating(markDao.getPositiveUserRating(userBean.getId()));
                    userBean.setNegativeRating(markDao.getNegativeUserRating(userBean.getId()));
                    users.put(userOptional.get().getId(), userBean);
                    answerBean.setUser(userBean);
                }
                else {
                    throw new ProjectException("Unable to find user");
                }
            }
        }
    }

    private void populateUser(QuestionBean question, long userId, MarkDao markDao) throws ProjectException {
        UserBean user = new UserBean();
        Optional<User> userOptional = userDao.findEntityById(userId);
        if (userOptional.isPresent()) {
            user.setUsername(userOptional.get().getUsername());
            user.setAvatarPath(userOptional.get().getAvatarPath());
            user.setId(userOptional.get().getId());
            long positiveRating = markDao.getPositiveUserRating(user.getId());
            long negativeRating = markDao.getNegativeUserRating(user.getId());
            user.setNegativeRating(negativeRating);
            user.setPositiveRating(positiveRating);
            question.setUser(user);
        } else {
            throw new ProjectException("Unable to find user with id " + userId);
        }
    }
}
