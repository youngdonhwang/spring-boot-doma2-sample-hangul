$(function() {
    var options = {
        rules: {
            firstName: {
                required: true,
                maxlength: 50
            },
            lastName: {
                required: true,
                maxlength: 50
            },
            password: {
                required: true,
                rangelength: [8, 50]
            },
            passwordConfirm: {
                equalTo: "#password",
                rangelength: [8, 50]
            },
            email: {
                email: true
            },
            tel: {
                digits: true
            }
        }
    };

    // 초기화
    $("#form1").submit(function(e) {
        e.preventDefault();
    }).validate(options);
});
