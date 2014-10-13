(function () {
    window.CSSStyleVerifier = (function () {
        return {
            saveState: function (representation, real_element) {
                representation["style"] = {};
                for (var j in real_element.style) {
                    if (real_element.style[j] !== null)
                        representation.style[j] = real_element.style[j].toString();
                    else
                        representation.style[j] = "";
                };
            },
            checkChanges: function (representation, real_element) {
                for (var j in real_element.style) {
                    if ((real_element.style[j] === null && representation.style[j] !== "") ||
                        (real_element.style[j] !== null && representation.style[j] !==
                                                            real_element.style[j].toString())) {
                        return {
                            target: real_element,
                            type: "style"
                        };
                    }
                };
                return null;
            }
        };
    }());
}());
