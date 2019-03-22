$(document).ready(function () {
    $(".close").bind(
        "click",
        function(e){
            $("#error-alert").css('display', 'none');
        }
    )
    $(".answer-pos-rating").unbind().bind(
        "click", 
        function (e) {
            e.preventDefault();
            var $div = $(this).parent();
            $.ajax({
                type: "post",
                url: "/qaproject/async?command=rate_positive&answer_id=" + $div.attr("id"),
                dataType: "text",
                success: function (response) {
                    var json = $.parseJSON(response);
                    if(!json.errorMessage){
                        if(!json.error){
                            $div.find('.answer-pos-rating').html('+' + json.positive);
                            $div.find('.answer-neg-rating').html('-' + json.negative);
                            $div.find('.answer-pos-rating').removeClass("btn-outline-success btn-success");
                            if(json.currentMark == 1){
                                $div.find('.answer-pos-rating').addClass("btn-success");
                            }
                            else{
                                $div.find('.answer-pos-rating').addClass("btn-outline-success");
                            }
                            $div.find('.answer-neg-rating').removeClass("btn-outline-danger btn-danger");
                            if(json.currentMark == -1){
                                $div.find('.answer-neg-rating').addClass("btn-danger");
                            }
                            else{
                                $div.find('.answer-neg-rating').addClass("btn-outline-danger");
                            }
                            if(json.positive == 0 && json.negative == 0){
                                $div.parent().parent().parent().find('.answer-edit-btn').show();
                            }
                            else{
                                $div.parent().parent().parent().find('.answer-edit-btn').hide();
                            }
                        }
                    }
                    else{
                        $("#error-alert").css('display', 'block');
                        $("#error-label").html(json.errorMessage);
                    }
                }
            });
        }
    );
    $(".answer-edit-btn").unbind().bind(
        "click", 
        function (e) {
            e.preventDefault();
            var $div = $(this).parent().parent().parent().parent().parent().parent().find(".answer");
            $div.find(".answer-text").hide();    
            $div.find(".answer-edit").show();    
        }
    );
    $(".cancel-answer-edit").unbind().bind(
        "click", 
        function (e) {
            e.preventDefault();
            var $div = $(this).parent().parent().parent();
            $div.find(".answer-text").show();    
            $div.find(".answer-edit").hide();    
        }
    );
});

$(document).ready(function () {
    $(".answer-neg-rating").unbind().bind(
        "click", 
        function (e) {
            e.preventDefault();
            var $div = $(this).parent();
            $.ajax({
                type: "post",
                url: "/qaproject/async?command=rate_negative&answer_id=" + $div.attr("id"),
                dataType: "text",
                success: function (response) {
                    var json = $.parseJSON(response);
                    if(!json.errorMessage){
                        if(!json.error){
                            $div.find('.answer-pos-rating').html('+' + json.positive);
                            $div.find('.answer-neg-rating').html('-' + json.negative);
                            $div.find('.answer-pos-rating').removeClass("btn-outline-success btn-success");
                            if(json.currentMark == 1){
                                $div.find('.answer-pos-rating').addClass("btn-success");
                            }
                            else{
                                $div.find('.answer-pos-rating').addClass("btn-outline-success");
                            }
                            $div.find('.answer-neg-rating').removeClass("btn-outline-danger btn-danger");
                            if(json.currentMark == -1){
                                $div.find('.answer-neg-rating').addClass("btn-danger");
                            }
                            else{
                                $div.find('.answer-neg-rating').addClass("btn-outline-danger");
                            }
                            if(json.positive == 0 && json.negative == 0){
                                $div.parent().parent().parent().find('.answer-edit-btn').show();
                            }
                            else{
                                $div.parent().parent().parent().find('.answer-edit-btn').hide();
                            }
                        }
                    }
                    else{
                        $("#error-alert").css('display', 'block');
                        $("#error-label").html(json.errorMessage);
                    }
                }
            });
        });
    }
);