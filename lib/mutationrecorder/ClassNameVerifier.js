(function () {
    window.ClassNameVerifier = (function () {
        return {
            saveState: function (representation, real_element) {
                representation.className = real_element.className;
            }
        };
    }());
}());
