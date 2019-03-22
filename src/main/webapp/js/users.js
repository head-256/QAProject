let dataset = document.getElementById('jsVariables').dataset;
let make = dataset.make;
let unmake = dataset.unmake;
let ban = dataset.ban;
let unban = dataset.unban;
let available = dataset.available;
let banned = dataset.banned;

$(document).ready(function () {
    function make_admin(e){
        e.preventDefault();
        var $div = $(this).parent().parent();
        $.ajax({
            type: "post",
            url: "/qaproject/async?command=make_admin&user_id=" + $div.attr("id"),
            dataType: "text",
            success: function (response) {
                var json = $.parseJSON(response);
                if(json.success == '1'){
                    $div.find('.admin-btn').html(unmake);
                    $div.find('.privilege-td').html('1');
                    $div.find('.admin-btn').unbind().bind( "click", unmake_admin);
                }
                else{
                    $div.find('.admin-btn').html(make);
                    $div.find('.admin-btn').unbind().bind("click", make_admin);
                }
            }
        });
    }
    function unmake_admin(e){
        e.preventDefault();
        var $div = $(this).parent().parent();
        $.ajax({
            type: "post",
            url: "/qaproject/async?command=unmake_admin&user_id=" + $div.attr("id"),
            dataType: "text",
            success: function (response) {
                var json = $.parseJSON(response);
                $div.find('.admin-btn').removeClass("unmake-admin-btn make-admin-btn");
                if(json.success == 1){
                    $div.find('.admin-btn').html(make);
                    $div.find('.privilege-td').html('0');
                    $div.find('.admin-btn').unbind().bind("click", make_admin);
                }
                else{
                    $div.find('.admin-btn').html(unmake);
                    $div.find('.admin-btn').unbind().bind( "click", unmake_admin);
                }
            }
        });
    }

    function ban_user(e){
        e.preventDefault();
        var $div = $(this).parent().parent();
        $.ajax({
            type: "post",
            url: "/qaproject/async?command=ban_user&user_id=" + $div.attr("id"),
            dataType: "text",
            success: function (response) {
                var json = $.parseJSON(response);
                if(json.success == '1'){
                    $div.find('.ban-btn').html(unban);
                    $div.find('.ban-btn').unbind().bind( "click", unban_user);
                    $div.find('.status-td').html(banned);
                }
                else{
                    $div.find('.ban-btn').html(ban);
                    $div.find('.ban-btn').unbind().bind("click", ban_user);
                }
            }
        });
    }
    function unban_user(e){
        e.preventDefault();
        var $div = $(this).parent().parent();
        $.ajax({
            type: "post",
            url: "/qaproject/async?command=unban_user&user_id=" + $div.attr("id"),
            dataType: "text",
            success: function (response) {
                var json = $.parseJSON(response);
                if(json.success == '1'){
                    $div.find('.ban-btn').html(ban);
                    $div.find('.ban-btn').unbind().bind( "click", ban_user);
                    $div.find('.status-td').html(available);
                }
                else{
                    $div.find('.ban-btn').html(unban);
                    $div.find('.ban-btn').unbind().bind("click", unban_user);
                }
            }
        });
    }
    $(".make-admin-btn").unbind().bind( "click", make_admin)    
    $(".unmake-admin-btn").unbind().bind( "click", unmake_admin)   
    $(".ban-user-btn").unbind().bind( "click", ban_user)     
    $(".unban-user-btn").unbind().bind( "click", unban_user)     
});