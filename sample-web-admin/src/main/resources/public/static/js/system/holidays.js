$(function() {
    var options = {
        rules: {
            holidayName: {
                required: true,
                maxlength: 50
            }
        }
    };

    // 초기화
    $("#form1").submit(function(e) {
        e.preventDefault();
    }).validate(options);
});
