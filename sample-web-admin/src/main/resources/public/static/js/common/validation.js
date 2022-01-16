$(function() {
    // メッセージを上書きする
    $.extend($.validator.messages, {
        minlength: $.validator.format("{0}문자이상의 문자를 입력해주세요"),
        maxlength: $.validator.format("{0}문자이하의 문자를 입력해주세요"),
        rangelength: $.validator.format("{0}문자에서 {1}문자의 값을 입력해주세요,"),
        required: "값을 입력해주세요",
        equalTo: "올바른 값을 입력해주세요.",
        email: "올바른 메일 주소를 입력해주세요.",
        digits: "숫자값을 입력해주세요."
    });

    $.validator.setDefaults({
        // 서버쪽 밸리데이션과 동일한 모양으로 한다
        errorElement: "span",
        errorClass: "help-block",
        // 오류 메시지의 출력처
        errorPlacement: function(error, element) {
            $(element).next(".help-block").remove();
            $(element).closest(".form-group").addClass("has-error");
            error.insertAfter(element);
        },
        success: function(error, element) {
            $(element).closest(".form-group").removeClass("has-error");
            $(element).next(".help-block").remove();
            $(error).remove();
        },
        onkeyup: function(element, event ) {
            // Avoid revalidate the field when pressing one of the following keys
			// Shift       => 16
			// Ctrl        => 17
			// Alt         => 18
			// Caps lock   => 20
			// End         => 35
			// Home        => 36
			// Left arrow  => 37
			// Up arrow    => 38
			// Right arrow => 39
			// Down arrow  => 40
			// Insert      => 45
			// Num lock    => 144
			// AltGr key   => 225
			var excludedKeys = [
				16, 17, 18, 20, 35, 36, 37,
				38, 39, 40, 45, 144, 225
			];

			if (event.which === 9 && this.elementValue(element) === "" || $.inArray(event.keyCode, excludedKeys) !== -1) {
				return;
			} else if (element.name in this.submitted || element.name in this.invalid) {
				this.element(element);
			}

            $(element).valid();
        },
        onfocusout: function(element) {
            if (!this.checkable(element) && (element.name in this.submitted || !this.optional(element))) {
                this.element(element);
            }

            $(element).valid();
        },
        invalidHandler: function(form, validator) {
            $("#modal-validation").modal();
        },
        submitHandler: function(form){
            form.submit();
        }
    });

    $.validator.methods.email = function(value, element) {
        return isOptionalInput(this, value, element) || this.optional(element) || /^([\w-\.]+@([\w-]+\.)+[\w-]{2,4})?$/.test(value);
    };

    function isOptionalInput(_this, value, element) {
        var rules = $(element).rules();
        var required = (_this.objectLength(rules)) ? rules.required : false;

        if (!required && _this.elementValue(element) === "") {
            // 필수 입력이 아닌 경우
            return true;
        }

        return false;
    }
});
