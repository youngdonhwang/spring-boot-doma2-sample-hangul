$(function() {
    var options = {
        rules: {
            categoryKey: {
                required: true,
                maxlength: 100
            },
            categoryValue: {
                required: true,
                maxlength: 100
            }
        }
    };

    // 초기화
    $("#form1").submit(function(e) {
        e.preventDefault();
    }).validate(options);
});
