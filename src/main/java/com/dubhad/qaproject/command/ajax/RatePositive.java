package com.dubhad.qaproject.command.ajax;

import com.dubhad.qaproject.ProjectException;
import com.dubhad.qaproject.bean.AnswerBean;
import com.dubhad.qaproject.bean.UserBean;
import com.dubhad.qaproject.command.AjaxCommand;
import com.dubhad.qaproject.command.AjaxCommandEnum;
import com.dubhad.qaproject.command.RequestParamAttr;
import com.dubhad.qaproject.logic.AnswerLogic;
import com.dubhad.qaproject.logic.QuestionLogic;
import com.dubhad.qaproject.logic.UserLogic;
import com.dubhad.qaproject.resource.Message;
import com.dubhad.qaproject.servlet.RequestWrapper;
import lombok.extern.log4j.Log4j2;

import javax.servlet.jsp.jstl.core.Config;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Puts positive mark to specified answer
 * Required user status: available, role: any
 * @see AjaxCommandEnum
 */
@Log4j2
public class RatePositive implements AjaxCommand {
    /**
     * {@inheritDoc}
     * Required request params: userId, answerId
     * Required session attributes: user
     */
    @Override
    public Map<String, String> execute(RequestWrapper request) {
        Map<String, String> dataMap = new HashMap<>();
        try(QuestionLogic questionLogic = new QuestionLogic();
            AnswerLogic answerLogic = new AnswerLogic();
            UserLogic userLogic = new UserLogic()) {
            // UserBean user = (UserBean) request.getSession().getAttribute(RequestParamAttr.USER);
            UserBean user = userLogic.getUserFromContext();
            Locale locale = (Locale) Config.get(request.getSession(), Config.FMT_LOCALE);
            long userId = user.getId();
            long answerId = Long.parseLong(request.getParameter(RequestParamAttr.ANSWER_ID));
            AnswerBean answerBean = answerLogic.get(answerId);
            if(!answerBean.isDeleted() && questionLogic.answerActionAvailable(answerBean.getQuestionId())) {
                answerBean = answerLogic.ratePositive(answerId, userId);
                dataMap.put(RequestParamAttr.POSITIVE, String.valueOf(answerBean.getPositiveRating()));
                dataMap.put(RequestParamAttr.NEGATIVE, String.valueOf(answerBean.getNegativeRating()));
                dataMap.put(RequestParamAttr.CURRENT_MARK, String.valueOf(answerBean.getCurrentUserMark()));
            }
            else{
                dataMap.put(RequestParamAttr.ERROR, Message.AJAX_ANSWER_RATE_ERROR.get(locale));
            }
        } catch (ProjectException | NumberFormatException e) {
            log.error(e);
        }
        return dataMap;
    }
}
