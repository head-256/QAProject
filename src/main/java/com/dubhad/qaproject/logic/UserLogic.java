package com.dubhad.qaproject.logic;

import com.dubhad.qaproject.ProjectException;
import com.dubhad.qaproject.dao.*;
import com.dubhad.qaproject.entity.User;
import com.dubhad.qaproject.entity.UserRole;
import com.dubhad.qaproject.entity.UserStatus;
import com.dubhad.qaproject.util.FileUtil;
import com.dubhad.qaproject.util.SecurityUtil;
import com.dubhad.qaproject.bean.UserBean;
import lombok.extern.log4j.Log4j2;

import javax.servlet.http.Part;
import java.util.*;

@Log4j2
public class UserLogic extends AbstractLogic{
    private UserDao userDao;

    public UserLogic(){
        userDao = TransactionHelper.getFactory().getUserDao();
        getHelper().prepareDao(userDao);
    }

    /**
     * Deletes avatar of specified user, with specified base path
     * @param user user to delete avatar of
     * @param basePath path to real path of app
     * @throws ProjectException if unexpected exception in dao layer occurred
     */
    public void deleteAvatar(UserBean user, String basePath) throws ProjectException {
        Optional<User> userOptional = userDao.findEntityById(user.getId());
        try {
            if (userOptional.isPresent() && userOptional.get().getAvatarPath() != null) {
                new FileUtil().deleteAvatar(basePath, userOptional.get().getUsername(), userOptional.get().getAvatarPath());
                userOptional.get().setAvatarPath(null);
                userDao.update(userOptional.get());
                getHelper().commit();
            }
        }
        catch (ProjectException e){
            getHelper().rollback();
            throw e;
        }
    }

    /**
     * Changes user email and status to not confirmed and sends confirmation email
     * @param user user to change email and fetch email from
     * @param locale current locale of session for email
     * @param baseLink url to app to create confirmation link
     * @throws ProjectException if user with specified id can't be found
     */
    public void changeEmail(UserBean user, Locale locale, String baseLink)
            throws ProjectException {
        Optional<User> userOptional = userDao.findEntityById(user.getId());
        if(userOptional.isPresent()){
            try {
                userOptional.get().setEmail(user.getEmail());
                userDao.update(userOptional.get());
                getHelper().commit();
            }
            catch (ProjectException e){
                getHelper().rollback();
                throw e;
            }
        }
        else{
            throw new ProjectException("Unable to find user with id " + user.getId());
        }
    }

    /**
     * Changes password of specified user to specified one
     * @param user user to change password
     * @param password new password
     * @throws ProjectException if user with specified id can't be found
     */
    public void changePassword(UserBean user, String password) throws ProjectException {
        Optional<User> userOptional = userDao.findEntityById(user.getId());
        if(userOptional.isPresent()){
            String userSalt = userOptional.get().getPasswordSalt();
            String hashedPassword = SecurityUtil.md5(password, userSalt);
            userOptional.get().setPassword(hashedPassword);
            userDao.update(userOptional.get());
        }
        else{
            throw new ProjectException("Unable to find user with id " + user.getId());
        }
    }

    /**
     * Checks if specified user has rights to delete answers and questions
     * @param userBean user to fetch id and check privileges
     * @return true, if user has delete rights, false otherwise
     * @throws ProjectException if user with specified id can't be found
     */
    public boolean hasDeleteRights(UserBean userBean) throws ProjectException {
        return isAdmin(userBean);
    }

    /**
     * Checks if specified user has rights to edit part of question
     * @param userBean user to fetch id and check privileges
     * @return true, if user has rights to edit question, false otherwise
     * @throws ProjectException if user with specified id can't be found
     */
    public boolean hasGeneralQuestionEditRights(UserBean userBean) throws ProjectException {
        return isAdmin(userBean);
    }

    /**
     * Checks if specified user is admin
     * @param userBean user to fetch id and check privileges
     * @return true, if user is admin, false otherwise
     * @throws ProjectException if user with specified id can't be found
     */
    private boolean isAdmin(UserBean userBean) throws ProjectException {
        boolean result = false;
        Optional<User> userOptional = userDao.findEntityById(userBean.getId());
        if(userOptional.isPresent()){
            if(userOptional.get().getRole().ordinal() >= UserRole.ADMIN.ordinal()){
                result = true;
            }
        }
        else{
            throw new ProjectException("Unable to find user with id " + userBean.getId());
        }
        return result;
    }

    /**
     * Checks if specified user is super admin
     * @param userBean user to fetch id and check privileges
     * @return true, if user is super admin, false otherwise
     * @throws ProjectException if user with specified id can't be found
     */
    private boolean isSuperAdmin(UserBean userBean) throws ProjectException {
        boolean result = false;
        Optional<User> userOptional = userDao.findEntityById(userBean.getId());
        if(userOptional.isPresent()){
            if(userOptional.get().getRole() == UserRole.SUPERADMIN){
                result = true;
            }
        }
        else{
            throw new ProjectException("Unable to find user with id " + userBean.getId());
        }
        return result;
    }

    /**
     * Checks if specified user has specified status
     * @param userBean user to fetch id and check status
     * @param status status to check on equality with user's
     * @return true, if user has specified status, false otherwise
     * @throws ProjectException if user with specified id can't be found
     */
    public boolean checkUserStatus(UserBean userBean, UserStatus status) throws ProjectException {
        boolean result;
        switch (status){
            case AVAILABLE:
                result = isUserAvailable(userBean.getId());
                break;
            default:
                result = false;
        }
        return result;
    }

    public boolean checkUserRole(UserBean userBean, UserRole role) throws ProjectException {
        boolean result;
        switch (role){
            case ADMIN:
                result = isAdmin(userBean);
                break;
            case SUPERADMIN:
                result = isSuperAdmin(userBean);
                break;
            default:
                result = true;
        }
        return result;
    }

    /**
     * Checks if specified user is available
     * @param user user to fetch id and check status
     * @return true, if user has status "available", false otherwise
     * @throws ProjectException if user with specified id can't be found
     */
    public boolean isUserAvailable(UserBean user) throws ProjectException {
        return isUserAvailable(user.getId());
    }

    /**
     * Checks if specified user is available
     * @param id id of user to  check status
     * @return true, if user has status "available", false otherwise
     * @throws ProjectException if user with specified id can't be found
     */
    public boolean isUserAvailable(long id) throws ProjectException {
        boolean result = false;
        Optional<User> userOptional = userDao.findEntityById(id);
        if(userOptional.isPresent()) {
            result = userOptional.get().getStatus() == UserStatus.AVAILABLE;
        }
        return result;
    }


    /**
     * Unban specified user
     * @param currentUser current user
     * @param userId id of user to unban
     * @throws ProjectException if user can't be found or current user role is too low or specified user's status is not
     * banned
     */
    public void unbanUser(UserBean currentUser, long userId) throws ProjectException{
        Optional<User> userOptional = userDao.findEntityById(userId);
        Optional<User> currentUserOptional = userDao.findEntityById(currentUser.getId());
        if (userOptional.isPresent() && currentUserOptional.isPresent()) {
            if(currentUserOptional.get().getRole() != UserRole.USER) {
                User user = userOptional.get();
                if (user.getStatus() == UserStatus.BANNED) {
                    user.setStatus(UserStatus.AVAILABLE);
                    userDao.update(user);
                    getHelper().commit();
                } else {
                    throw new ProjectException("Unexpected user status: " + user.getStatus());
                }
            }
            else {
                throw new ProjectException("User role is too low");
            }
        } else {
            throw new ProjectException("Unable to find user");
        }
    }

    /**
     * Bans user with specified id, if current user role is bigger
     * @param currentUser current user
     * @param userId id of user to ban
     * @throws ProjectException if user with specified id can't be found, or current user privileges level too low, or
     * specified user's status is not available
     */
    public void banUser(UserBean currentUser, long userId) throws ProjectException{
        Optional<User> userOptional = userDao.findEntityById(userId);
        Optional<User> currentUserOptional = userDao.findEntityById(currentUser.getId());
        if(userOptional.isPresent() && currentUserOptional.isPresent()) {
            User user = userOptional.get();
            if(currentUserOptional.get().getRole().ordinal() > user.getRole().ordinal()) {
                if (user.getStatus() == UserStatus.AVAILABLE) {
                    user.setStatus(UserStatus.BANNED);
                    userDao.update(user);
                    log.debug("Banned user: " + user.getStatus());
                    getHelper().commit();
                } else {
                    throw new ProjectException("Unexpected user status: " + user.getStatus());
                }
            }
            else {
                throw new ProjectException("User role is too low");
            }
        }
        else {
            throw new ProjectException("Unable to find user");
        }
    }

    /**
     * Removes admin status from specified user.
     * @param currentUser current user
     * @param userId id of user to remove admin role
     * @throws ProjectException if specified users can't be found or current user role is too low
     */
    public void unmakeAdmin(UserBean currentUser, long userId) throws ProjectException{
        Optional<User> userOptional = userDao.findEntityById(userId);
        Optional<User> currentUserOptional = userDao.findEntityById(currentUser.getId());
        if (userOptional.isPresent() && currentUserOptional.isPresent()) {
            if(currentUserOptional.get().getRole() == UserRole.SUPERADMIN) {
                User user = userOptional.get();
                if (user.getRole() == UserRole.ADMIN) {
                    user.setRole(UserRole.USER);
                    userDao.update(user);
                    getHelper().commit();
                } else {
                    throw new ProjectException("Unexpected user role: " + user.getRole());
                }
            }
            else {
                throw new ProjectException("User role is too low");
            }
        } else {
            throw new ProjectException("Unable to find user");
        }
    }

    /**
     * Gives admin status to specified user.
     * @param currentUser current user
     * @param userId id of user to give admin role
     * @throws ProjectException if specified users can't be found or current user role is too low
     */
    public void makeAdmin(UserBean currentUser, long userId) throws ProjectException{
        Optional<User> userOptional = userDao.findEntityById(userId);
        Optional<User> currentUserOptional = userDao.findEntityById(currentUser.getId());
        if (userOptional.isPresent() && currentUserOptional.isPresent()) {
            if(currentUserOptional.get().getRole() != UserRole.USER) {
                User user = userOptional.get();
                if (user.getRole() == UserRole.USER) {
                    user.setRole(UserRole.ADMIN);
                    userDao.update(user);
                    getHelper().commit();
                } else {
                    throw new ProjectException("Unexpected user role: " + user.getRole());
                }
            }
            else {
                throw new ProjectException("User role is too low");
            }
        } else {
            throw new ProjectException("Unable to find user");
        }
    }

    /**
     * Gets count of all users
     * @return number of found users
     * @throws ProjectException in the case of unexpected error
     */
    public long getCount() throws ProjectException {
        long count;
        count = userDao.getCount();
        return count;
    }

    /**
     * Gets users with specified limit and offset
     * @param offset offset of users slice
     * @param limit size of slice
     * @return list of found users
     * @throws ProjectException if unexpected error occurred
     */
    public List<UserBean> getUsers(long offset, int limit) throws ProjectException {
        MarkDao markDao = TransactionHelper.getFactory().getMarkDao();
        getHelper().prepareDao(markDao);
        List<UserBean> userBeans = new LinkedList<>();
        List<User> users = userDao.findUsers(offset, limit);
        for(User user : users){
            userBeans.add(copyUser(user));
        }
        return userBeans;
    }

    /**
     * Gets user with specified id
     * @param userId id of user
     * @return user object if it was found
     * @throws ProjectException if user not found
     */
    public UserBean getUser(long userId) throws ProjectException {
        MarkDao markDao = TransactionHelper.getFactory().getMarkDao();
        QuestionDao questionDao = TransactionHelper.getFactory().getQuestionDao();
        AnswerDao answerDao = TransactionHelper.getFactory().getAnswerDao();
        getHelper().prepareDaos(markDao, questionDao, answerDao);
        UserBean userBean;
        Optional<User> userOptional = userDao.findEntityById(userId);
        if(userOptional.isPresent()){
            userBean = copyUser(userOptional.get());
            userBean.setPositiveRating(markDao.getPositiveUserRating(userId));
            userBean.setNegativeRating(markDao.getNegativeUserRating(userId));
        }
        else{
            throw new ProjectException("User does not exist");
        }
        return userBean;
    }

    /**
     * Uploads avatar for specified user with specified base path to avatar folder
     * @param userId id of user
     * @param basePath path to parent folder of avatars folder
     * @param parts parts, that contain avatar
     * @return extension of uploaded avatar
     * @throws ProjectException if user can't be found ot  avatar upload failed
     */
    public String uploadAvatar(long userId, String basePath, Collection<Part> parts) throws ProjectException {
        FileUtil fileUtil = new FileUtil();
        String result;
        Optional<User> userOptional = userDao.findEntityById(userId);
        if(userOptional.isPresent()){
            String username = userOptional.get().getUsername();
            result = fileUtil.saveUploadedAvatar(parts, basePath, username);
            if(result != null){
                boolean deletePrevious = !Objects.equals(result, userOptional.get().getAvatarPath());
                if(deletePrevious){
                    fileUtil.deleteAvatar(basePath, username, userOptional.get().getAvatarPath());
                }
                userOptional.get().setAvatarPath(result);
                userDao.update(userOptional.get());
                getHelper().commit();
            }
            else {
                throw new ProjectException("Unable to save picture");
            }
        }
        else{
            throw new ProjectException("User does not exist");
        }
        return result;
    }

    /**
     * Creates user with specified email, username and password and sends confirmation email
     * @param email desired email
     * @param username desired username
     * @param password desired password
     * @return user object, if user created successfully
     * @throws ProjectException if any exception occurred
     */
    public UserBean createUser(String email, String username, String password) throws ProjectException {
        UserBean userBean;
        try{
            User user = new User();
            String salt = SecurityUtil.getSalt();
            String saltedPassword = SecurityUtil.md5(password, salt);
            user.setPassword(saltedPassword);
            user.setPasswordSalt(salt);
            user.setEmail(email);
            user.setUsername(username);
            user.setStatus(UserStatus.AVAILABLE);
            user.setRole(UserRole.USER);
            user.setAvatarPath(null);
            userDao.create(user);

            userBean = copyUser(user);

            getHelper().commit();
            log.info("User created successfully");
        }
        catch (ProjectException e){
            getHelper().rollback();
            throw e;
        }
        return userBean;
    }

    /**
     * Check, whether specified email is not taken by anyone
     * @param email email to check
     * @return true, if email is available, false otherwise
     * @throws ProjectException is any unexpected exception occurred
     */
    public boolean checkEmailAvailable(String email) throws ProjectException {
        Optional<User> userOptional = userDao.findEntityByEmail(email);
        return !userOptional.isPresent();
    }

    /**
     * Check, whether specified username is not taken by anyone
     * @param username username to check
     * @return true, if username is available, false otherwise
     * @throws ProjectException is any unexpected exception occurred
     */
    public boolean checkUsernameAvailable(String username) throws ProjectException {
        Optional<User> userOptional = userDao.findEntityByUsername(username);
        return !userOptional.isPresent();
    }

    /**
     * Checks, if pair username-password is correct
     * @param username account username
     * @param password account password
     * @return Optional object, that contains user, if username-password pair was correct, empty otherwise
     * @throws ProjectException if any unexpected exception occurred
     */
    public Optional<UserBean> validateLogin(String username, String password) throws ProjectException {
        UserBean userBean = null;
        Optional<User> userOptional = userDao.findEntityByUsername(username);
        if(userOptional.isPresent()){
            //Check password
            String saltedPassword = password + userOptional.get().getPasswordSalt();
            String md5 = SecurityUtil.md5(saltedPassword);
            if(Objects.equals(md5, userOptional.get().getPassword())){
                userBean = new UserBean();
                userBean.setId(userOptional.get().getId());
                userBean.setUsername(userOptional.get().getUsername());
                userBean.setStatus(userOptional.get().getStatus());
                userBean.setPrivilegeLevel(userOptional.get().getRole().ordinal());
            }
        }
        return Optional.ofNullable(userBean);
    }

    private UserBean copyUser(User entity){
        UserBean userBean = new UserBean();
        userBean.setId(entity.getId());
        userBean.setUsername(entity.getUsername());
        userBean.setEmail(entity.getEmail());
        userBean.setAvatarPath(entity.getAvatarPath());
        userBean.setPrivilegeLevel(entity.getRole().ordinal());
        userBean.setStatus(entity.getStatus());
        return userBean;
    }

    public void saveUserToContext(UserBean user) throws ProjectException {
        User entity = new User();
        entity.setId(user.getId());
        entity.setUsername(user.getUsername());
        entity.setEmail(user.getEmail());
        entity.setAvatarPath(user.getAvatarPath());
        entity.setRole(UserRole.values()[user.getPrivilegeLevel()]);
        entity.setStatus(user.getStatus());
        userDao.saveUserToContext(entity);
    }

    public UserBean getUserFromContext() throws ProjectException {
        User entity = userDao.getUserFromContext();
        if (entity == null) {
            return null;
        }
        return copyUser(entity);
    }
}
