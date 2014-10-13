(function () {
    window.ClassNameVerifier = (function () {
        return {
            saveState: function (representation, real_element) {
                representation.className = real_element.className;
            },
            checkChanges: function (representation, real_element) {
                if (representation.className !== real_element.className) {
                    return { target: real_element, type: "style" };
                }
                return null;
            }
        };
    }());
}());
