$(function() {
    var options = {
        rules: {
            subject: {
                required: true,
                maxlength: 100
            },
            templateBody: {
                required: true
            }
        }
    };

    // 초기화
    $("#form1").submit(function(e) {
        e.preventDefault();
    }).validate(options);
});
