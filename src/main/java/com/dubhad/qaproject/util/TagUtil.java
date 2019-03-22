package com.dubhad.qaproject.util;

import com.dubhad.qaproject.bean.TagBean;
import com.dubhad.qaproject.resource.Message;
import com.dubhad.qaproject.util.validator.TagValidator;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;

import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

/**
 * Class, that provides utility methods for tag parsing tasks
 */
@Log4j2
public class TagUtil {
    private static final String TAG_DELIMITER = ",";

    /**
     * Tries to parse aggregated tags, requires at least one tag, error message otherwise
     * @param tags string, that contains all tags, divided by commas
     * @param locale locale for error messages
     * @return ParseResult object, that contains not-null error message if any error occurred, otherwise - list of found
     * tags
     */
    public static ParseResult parseQuestionsTags(String tags, Locale locale){
        ParseResult parseResult = new ParseResult();
        if(tags != null) {
            String[] uncheckedTags = tags.split(TAG_DELIMITER);
            if (uncheckedTags.length > 0) {
                for (String tag : uncheckedTags) {
                    if (TagValidator.validateText(tag.trim())) {
                        TagBean tagBean = new TagBean();
                        tagBean.setText(tag.trim());
                        parseResult.tags.add(tagBean);
                    } else {
                        parseResult.errorMessage = Message.QUESTION_TAGS_INVALID.get(locale);
                        log.debug("Tag invalid: " + tag);
                        break;
                    }
                }
            } else {
                parseResult.errorMessage = Message.QUESTION_TAG_REQUIRED.get(locale);
            }
        }
        else{
            parseResult.errorMessage = Message.QUESTION_TAG_REQUIRED.get(locale);
        }
        return parseResult;
    }

    /**
     * Tries to parse aggregated tags
     * @param tags string, that contains all tags, divided by commas
     * @param locale locale for error messages
     * @return ParseResult object, that contains not-null error message if any error occurred, otherwise - list of found
     * tags
     */
    public static ParseResult parseSearchTags(String tags, Locale locale){
        ParseResult parseResult = new ParseResult();
        if(tags != null) {
            String[] uncheckedTags = tags.split(TAG_DELIMITER);
            if (uncheckedTags.length > 0) {
                for (String tag : uncheckedTags) {
                    if (TagValidator.validateText(tag.trim())) {
                        TagBean tagBean = new TagBean();
                        tagBean.setText(tag.trim());
                        parseResult.tags.add(tagBean);
                    } else {
                        parseResult.errorMessage = Message.QUESTION_TAGS_INVALID.get(locale);
                        break;
                    }
                }
            } else {
                parseResult.errorMessage = Message.QUESTION_TAG_REQUIRED.get(locale);
            }
        }
        return parseResult;
    }

    @Getter
    public static class ParseResult {
        private String errorMessage;
        private List<TagBean> tags = new LinkedList<>();
    }
}
