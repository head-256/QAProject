package com.dubhad.qaproject.command.impl;

import com.dubhad.qaproject.ProjectException;
import com.dubhad.qaproject.bean.AnswerBean;
import com.dubhad.qaproject.command.ActionCommand;
import com.dubhad.qaproject.command.CommandEnum;
import com.dubhad.qaproject.command.Router;
import com.dubhad.qaproject.logic.AnswerLogic;
import com.dubhad.qaproject.logic.QuestionLogic;
import com.dubhad.qaproject.resource.PathEnum;
import com.dubhad.qaproject.servlet.RequestWrapper;
import com.dubhad.qaproject.bean.UserBean;
import com.dubhad.qaproject.command.RequestParamAttr;
import com.dubhad.qaproject.logic.UserLogic;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class EditAnswerCommand implements ActionCommand {
    /**
     * {@inheritDoc}
     * Required request params: questionId, answerId, text
     * Required session attributes: user
     */
    @Override
    public Router execute(RequestWrapper request) {
        Router router = new Router();
        // UserBean currentUser = (UserBean) request.getSession().getAttribute(RequestParamAttr.USER);
        try(UserLogic userLogic = new UserLogic();
            QuestionLogic questionLogic = new QuestionLogic();
            AnswerLogic answerLogic = new AnswerLogic()){
            UserBean currentUser = userLogic.getUserFromContext();
            long questionId = Long.parseLong(request.getParameter(RequestParamAttr.QUESTION_ID));
            long answerId = Long.parseLong(request.getParameter(RequestParamAttr.ANSWER_ID));
            String text = request.getParameter(RequestParamAttr.TEXT).trim();
            boolean userAvailable = userLogic.isUserAvailable(currentUser.getId());
            boolean questionAvailable = questionLogic.answerActionAvailable(questionId);
            boolean answerAvailable = answerLogic.answerEditAvailable(answerId, currentUser);
            if(userAvailable && questionAvailable && answerAvailable){
                AnswerBean answerBean = new AnswerBean();
                answerBean.setId(answerId);
                answerBean.setText(text);
                answerLogic.edit(answerBean);
                router.setPage(PathEnum.INTERMEDIATE);
                request.setAttribute(RequestParamAttr.COMMAND.toLowerCase(), CommandEnum.SHOW_QUESTION.name());
                request.setAttribute(RequestParamAttr.QUESTION_ID, questionId);
            }
            else{
                router.setPage(PathEnum.ERROR_403);
                router.changeAction();
            }
        }
        catch (ProjectException | NumberFormatException e) {
            log.error(e);
            router.setPage(PathEnum.ERROR_500);
            router.changeAction();
        }
        return router;
    }
}
